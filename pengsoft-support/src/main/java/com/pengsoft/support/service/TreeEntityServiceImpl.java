package com.pengsoft.support.service;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.List;

import com.pengsoft.support.domain.TreeEntity;
import com.pengsoft.support.repository.TreeEntityRepository;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.support.util.QueryDslUtils;
import com.pengsoft.support.util.StringUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.domain.Sort;

/**
 * The implementer of {@link TreeEntityService} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class TreeEntityServiceImpl<R extends TreeEntityRepository<?, T, ID>, T extends TreeEntity<T, ID>, ID extends Serializable>
        extends EntityServiceImpl<R, T, ID> implements TreeEntityService<T, ID> {

    @Override
    public T save(final T entity) {
        var parentChanged = false;
        final T source = entity.getId() == null ? null : findOne(entity.getId()).orElse(null);
        if (source != null) {
            entity.setChildren(source.getChildren());
            parentChanged = EntityUtils.ne(source.getParent(), entity.getParent());
            // check the original parent if is a leaf node.
            if (parentChanged && source.getParent() != null) {
                source.getParent().setLeaf(source.getParent().getChildren().size() == 1);
                super.save(source.getParent());
            }
        }

        if (entity.getParent() != null) {
            // set the current parent as a non-leaf node.
            entity.setParent(findOne(entity.getParent().getId())
                    .orElseThrow(() -> getExceptions().entityNotFound(entity.getParent().getId().toString())));
            entity.getParent().setLeaf(false);
            super.save(entity.getParent());

            // change parent ids and depth.
            if (StringUtils.isBlank(entity.getParent().getParentIds())) {
                entity.setParentIds(entity.getParent().getId().toString());
            } else {
                entity.setParentIds(
                        StringUtils.join(new Object[] { entity.getParent().getParentIds(), entity.getParent().getId() },
                                StringUtils.GLOBAL_SEPARATOR));
            }
            entity.setDepth(entity.getParent().getDepth() + 1);
        }
        super.save(entity);

        if (entity.getParentIds().contains(entity.getId().toString())) {
            throw getExceptions().constraintViolated("parent", "self_and_its_children_not_allowed");
        }

        if (parentChanged) {
            updateTheParentIdsAndDepthOfChildNodes(entity);
        }

        return entity;
    }

    private void updateTheParentIdsAndDepthOfChildNodes(final T target) {
        final var deque = new ArrayDeque<>(target.getChildren());
        var parent = target;
        while (!deque.isEmpty()) {
            final T child = deque.pop();
            if (StringUtils.isBlank(parent.getParentIds())) {
                child.setParentIds(parent.getId().toString());
            } else {
                child.setParentIds(StringUtils.join(new Object[] { parent.getParentIds(), parent.getId() },
                        StringUtils.GLOBAL_SEPARATOR));
            }
            child.setDepth(parent.getDepth() + 1);
            super.save(child);
            parent = child;
            deque.addAll(parent.getChildren());
        }
    }

    @Override
    public void delete(final T entity) {
        findOne(entity.getId()).ifPresent(node -> {
            super.delete(node);
            if (node.getParent() != null) {
                final var parent = node.getParent();
                parent.getChildren().removeIf(child -> EntityUtils.eq(child, node));
                parent.setLeaf(parent.getChildren().isEmpty());
                super.save(parent);
            }
        });
    }

    @Override
    public void delete(List<T> entities) {
        getRepository().deleteAll(entities);
    }

    @Override
    public List<T> findAllByParent(final Predicate predicate, final Sort sort) {
        return findAll(QueryDslUtils.byParent(predicate, getEntityClass()), getDefaultSort());
    }

    @Override
    public List<T> findAllExcludeSelfAndItsChildrenByParent(final T self, final Predicate predicate, final Sort sort) {
        return findAllByParent(getPredicateOfExcludeSelfAndItsChildren(self, predicate), sort);
    }

    private Predicate getPredicateOfExcludeSelfAndItsChildren(final T self, Predicate predicate) {
        if (self != null) {
            final var idPath = QueryDslUtils.getIdStringPath(getEntityClass());
            predicate = QueryDslUtils.merge(idPath.ne(self.getId().toString()), predicate);

            final var parentIdsPath = (StringPath) QueryDslUtils.getPath(getEntityClass(), "parentIds");
            if (StringUtils.isBlank(self.getParentIds())) {
                predicate = QueryDslUtils.merge(parentIdsPath.notLike(self.getId().toString() + "%"), predicate);
            } else {
                predicate = QueryDslUtils.merge(parentIdsPath
                        .notLike(StringUtils.join(new String[] { self.getParentIds(), self.getId().toString() },
                                StringUtils.GLOBAL_SEPARATOR) + "%"),
                        predicate);
            }
        }
        return predicate;
    }

    @Override
    public List<T> findAllExcludeSelfAndItsChildren(final T self, final Predicate predicate, final Sort sort) {
        return findAll(getPredicateOfExcludeSelfAndItsChildren(self, predicate), sort);
    }

    @Override
    public List<T> findAllByParentIdsStartsWith(final String parentIds) {
        return getRepository().findAllByParentIdsStartsWith(parentIds);
    }

}
