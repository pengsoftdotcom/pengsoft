package com.pengsoft.support.facade;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.pengsoft.support.domain.Entity;
import com.pengsoft.support.exception.Exceptions;
import com.pengsoft.support.service.EntityService;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.Getter;

/**
 * The implementer of {@link EntityFacade}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class EntityFacadeImpl<S extends EntityService<T, ID>, T extends Entity<ID>, ID extends Serializable>
        implements EntityFacade<S, T, ID> {

    @Getter
    @Inject
    private Exceptions exceptions;

    @Getter
    @Inject
    private S service;

    @Override
    public Class<T> getEntityClass() {
        return service.getEntityClass();
    }

    @Override
    public Class<ID> getIdClass() {
        return service.getIdClass();
    }

    @Override
    public T save(final T entity) {
        return service.save(entity);
    }

    @Override
    public List<T> save(final List<T> entities) {
        return service.save(entities);
    }

    @Override
    public void delete(final T entity) {
        service.delete(entity);
    }

    @Override
    public void delete(final List<T> entities) {
        service.delete(entities);
    }

    @Override
    public Optional<T> findOne(final ID id) {
        return service.findOne(id);
    }

    @Override
    public Optional<T> findOne(final Predicate predicate) {
        return service.findOne(predicate);
    }

    @Override
    public Page<T> findPage(final Predicate predicate, final Pageable pageable) {
        return service.findPage(predicate, pageable);
    }

    @Override
    public List<T> findAll(final Predicate predicate, final Sort sort) {
        return service.findAll(predicate, sort);
    }

    @Override
    public boolean exists(final Predicate predicate) {
        return service.exists(predicate);
    }

    @Override
    public long count(final Predicate predicate) {
        return service.count(predicate);
    }

}
