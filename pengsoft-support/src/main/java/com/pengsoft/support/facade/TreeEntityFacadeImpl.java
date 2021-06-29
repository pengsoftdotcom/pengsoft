package com.pengsoft.support.facade;

import java.io.Serializable;
import java.util.List;

import com.pengsoft.support.domain.TreeEntity;
import com.pengsoft.support.service.TreeEntityService;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Sort;

/**
 * The implementer of {@link TreeEntityFacade}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class TreeEntityFacadeImpl<S extends TreeEntityService<T, ID>, T extends TreeEntity<T, ID>, ID extends Serializable>
        extends EntityFacadeImpl<S, T, ID> implements TreeEntityFacade<S, T, ID> {

    @Override
    public List<T> findAllByParent(final Predicate predicate, final Sort sort) {
        return getService().findAllByParent(predicate, sort);
    }

    @Override
    public List<T> findAllExcludeSelfAndItsChildrenByParent(final T self, final Predicate predicate, final Sort sort) {
        return getService().findAllExcludeSelfAndItsChildrenByParent(self, predicate, sort);
    }

    @Override
    public List<T> findAllExcludeSelfAndItsChildren(final T self, final Predicate predicate, final Sort sort) {
        return getService().findAllExcludeSelfAndItsChildren(self, predicate, sort);
    }

    @Override
    public List<T> findAllByParentIdsStartsWith(final String parentIds) {
        return getService().findAllByParentIdsStartsWith(parentIds);
    }

}
