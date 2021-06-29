package com.pengsoft.support.util;

import java.io.Serializable;
import java.util.Optional;

import com.pengsoft.support.domain.Entity;
import com.pengsoft.support.domain.TreeEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * Query DSL utility
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Slf4j
public class QueryDslUtils {

    private static final String GET_PATH_WRONG = "get %s path wrong from entity class: %s";

    private static final String PATH_ID = "id";

    private static final String PATH_PARENT = "parent";

    private static final String PATH_PARENT_ID = "parent.id";

    private QueryDslUtils() {

    }

    /**
     * Returns the query object class.
     *
     * @param entityClass The entity class
     */
    @SuppressWarnings("unchecked")
    public static <Q extends EntityPathBase<T>, T extends Entity<ID>, ID extends Serializable> Class<Q> getQueryClass(
            final Class<T> entityClass) {
        final int index = entityClass.getName().lastIndexOf('.');
        try {
            return (Class<Q>) Class.forName(
                    entityClass.getName().substring(0, index + 1) + "Q" + entityClass.getName().substring(index + 1));
        } catch (final Exception e) {
            log.error("query class not found for {}", entityClass.getName());
        }
        return null;
    }

    /**
     * Returns the query object's root.
     *
     * @param entityClass The entity class
     */
    public static <T extends Entity<ID>, ID extends Serializable> Object getRoot(final Class<T> entityClass) {
        final var queryClass = getQueryClass(entityClass);
        Assert.notNull(queryClass, String.format("get query class wrong from entity class: %s", entityClass.getName()));
        try {
            return queryClass.getConstructor(String.class)
                    .newInstance(StringUtils.uncapitalize(entityClass.getSimpleName()));
        } catch (final Exception e) {
            log.error("no constructor found for {}", entityClass.getName());
        }
        return null;
    }

    /**
     * Returns the field {@link Path} of the query object's root.
     *
     * @param entityClass The entity class
     */
    public static <T extends Entity<ID>, ID extends Serializable> Object getPath(final Class<T> entityClass,
            final String field) {
        final var queryClass = getQueryClass(entityClass);
        Assert.notNull(queryClass, String.format("get query class wrong from entity class: %s", entityClass.getName()));
        final var root = getRoot(entityClass);
        Object path = null;
        try {
            if (field.contains(StringUtils.PACKAGE_SEPARATOR)) {
                final var parts = field.split(StringUtils.ESCAPES + StringUtils.PACKAGE_SEPARATOR);
                path = queryClass.getField(parts[0]).get(root);
                for (int i = 1; i < parts.length; i++) {
                    path = path.getClass().getField(parts[i]).get(path);
                }
            } else {
                path = queryClass.getField(field).get(root);
            }
        } catch (final Exception e) {
            log.error("get {} path from {} error", field, entityClass.getName());
        }
        return path;
    }

    /**
     * Returns id string path.
     *
     * @param entityClass The entity class
     */
    public static <T extends Entity<ID>, ID extends Serializable> StringPath getIdStringPath(
            final Class<T> entityClass) {
        return (StringPath) getPath(entityClass, PATH_ID);
    }

    /**
     * Returns id number path.
     *
     * @param entityClass The entity class
     */
    @SuppressWarnings("unchecked")
    public static <T extends Entity<ID>, ID extends Serializable> NumberPath<Long> getIdNumberPath(
            final Class<T> entityClass) {
        return (NumberPath<Long>) getPath(entityClass, PATH_ID);
    }

    /**
     * Returns parent path.
     *
     * @param entityClass The entity class
     */
    @SuppressWarnings("unchecked")
    public static <T extends TreeEntity<T, ID>, ID extends Serializable> EntityPathBase<T> getParentPath(
            final Class<T> entityClass) {
        return (EntityPathBase<T>) getPath(entityClass, PATH_PARENT);
    }

    /**
     * Returns parentIds path.
     *
     * @param entityClass The entity class
     */
    public static <T extends Entity<ID>, ID extends Serializable> StringPath getParentIdsPath(
            final Class<T> entityClass) {
        return (StringPath) getPath(entityClass, PATH_ID);
    }

    /**
     * Returns the {@link Predicate} that the 'parent_id' field is null
     *
     * @param entityClass The entity class
     */
    public static <T extends TreeEntity<T, ID>, ID extends Serializable> Predicate byParent(
            final Class<T> entityClass) {
        return byParent(null, entityClass);
    }

    /**
     * Returns the merged {@link Predicate} of the input predicate and the result of
     * {@link #byParent(Class)}.
     *
     * @param predicate   {@link Predicate}
     * @param entityClass The entity class
     */
    public static <T extends TreeEntity<T, ID>, ID extends Serializable> Predicate byParent(final Predicate predicate,
            final Class<T> entityClass) {
        if (contains(predicate, entityClass, PATH_PARENT_ID)) {
            return predicate;
        } else {
            final var parentPath = getParentPath(entityClass);
            Assert.notNull(parentPath, String.format(GET_PATH_WRONG, PATH_PARENT, entityClass.getName()));
            if (isBlank(predicate)) {
                return parentPath.isNull();
            } else {
                return merge(parentPath.isNull(), predicate);
            }
        }
    }

    /**
     * Returns the {@link Predicate} that parentIds path not start with given
     * parentIds.
     *
     * @param entityClass The entity class
     * @param parentIds   The entity parentIds
     */
    public static <T extends TreeEntity<T, ID>, ID extends Serializable> Predicate byParent(final Class<T> entityClass,
            final String parentIds) {
        return byParent(null, entityClass, parentIds);
    }

    /**
     * Returns the {@link Predicate} that parentIds path not start with given
     * parentIds.
     *
     * @param predicate   The input predicate
     * @param entityClass The entity class
     * @param parentIds   The entity parentIds
     */
    public static <T extends TreeEntity<T, ID>, ID extends Serializable> Predicate byParent(final Predicate predicate,
            final Class<T> entityClass, final String parentIds) {
        if (StringUtils.isBlank(parentIds)) {
            return byParent(predicate, entityClass);
        } else {
            final var parentIdPath = getParentIdsPath(entityClass);
            Assert.notNull(parentIdPath, String.format(GET_PATH_WRONG, PATH_PARENT_ID, entityClass.getName()));
            return byParent(merge(predicate, parentIdPath.notLike(parentIds + "%")), entityClass);
        }
    }

    /**
     * Returns whether the {@link Predicate} is null.
     *
     * @param predicate {@link Predicate}
     */
    public static boolean isBlank(final Predicate predicate) {
        if (predicate == null) {
            return true;
        }
        if (predicate instanceof BooleanBuilder) {
            return !((BooleanBuilder) predicate).hasValue();
        }
        return false;
    }

    /**
     * Returns the opposite result of method {@link #isBlank(Predicate)}.
     *
     * @param predicate {@link Predicate}
     */
    public static boolean isNotBlank(final Predicate predicate) {
        return !isBlank(predicate);
    }

    /**
     * Returns whether the {@link Predicate} contains search field.
     *
     * @param entityClass The entity class
     * @param field       The entity field
     */
    public static boolean contains(final Predicate predicate, final Class<?> entityClass, final String field) {
        if (isNotBlank(predicate) && StringUtils.isNotBlank(field)) {
            return predicate.toString().contains(
                    StringUtils.uncapitalize(entityClass.getSimpleName()) + StringUtils.PACKAGE_SEPARATOR + field);
        }
        return false;
    }

    /**
     * Returns the merged {@link Predicate} with 'and'.
     *
     * @param sourcePredicate source {@link Predicate}
     * @param targetPredicate target {@link Predicate}
     */
    public static Predicate merge(final Predicate sourcePredicate, final Predicate targetPredicate) {
        return merge(sourcePredicate, targetPredicate, true);
    }

    /**
     * Returns the merged {@link Predicate} with 'and' or 'or'
     *
     * @param sourcePredicate source {@link Predicate}
     * @param targetPredicate target {@link Predicate}
     * @param conjunction     if true, use 'and'
     */
    public static Predicate merge(final Predicate sourcePredicate, final Predicate targetPredicate,
            final boolean conjunction) {
        var source = Optional.ofNullable(sourcePredicate).map(BooleanBuilder::new).orElseGet(BooleanBuilder::new);
        var target = Optional.ofNullable(targetPredicate).map(BooleanBuilder::new).orElseGet(BooleanBuilder::new);
        if (conjunction) {
            target = source.and(target);
        } else {
            target = source.or(target);
        }
        return target;
    }

}
