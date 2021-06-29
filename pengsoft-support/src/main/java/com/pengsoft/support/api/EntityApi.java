package com.pengsoft.support.api;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.pengsoft.support.domain.Enable;
import com.pengsoft.support.domain.Entity;
import com.pengsoft.support.domain.Sortable;
import com.pengsoft.support.exception.Exceptions;
import com.pengsoft.support.service.EnableService;
import com.pengsoft.support.service.EntityService;
import com.pengsoft.support.service.SortService;
import com.pengsoft.support.util.QueryDslUtils;
import com.querydsl.core.types.Predicate;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.Getter;
import lombok.SneakyThrows;

/**
 * The web api of {@link Entity}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Validated
public class EntityApi<S extends EntityService<T, ID>, T extends Entity<ID>, ID extends Serializable> {

    @Getter
    @Inject
    private Exceptions exceptions;

    @Getter
    @Inject
    private S service;

    @Inject
    private EnableService enableService;

    @Inject
    private SortService sortService;

    @PostMapping("save")
    public void save(@RequestBody final T entity) {
        service.save(entity);
    }

    @DeleteMapping("delete")
    public void delete(final Predicate predicate) {
        service.delete(service.findAll(predicate, Sort.unsorted()));
    }

    @PutMapping("enable")
    public void enable(@RequestParam("id") final List<T> enables) {
        final var domainClass = service.getEntityClass();
        Assert.isTrue(Enable.class.isAssignableFrom(domainClass), domainClass.getName() + " is not a Enable");
        enableService.enable(enables.stream().map(Enable.class::cast).collect(Collectors.toList()));
    }

    @PutMapping("disable")
    public void disable(@RequestParam("id") final List<T> enables) {
        final var domainClass = service.getEntityClass();
        Assert.isTrue(Enable.class.isAssignableFrom(domainClass), domainClass.getName() + " is not a Enable");
        enableService.disable(enables.stream().map(Enable.class::cast).collect(Collectors.toList()));
    }

    @PutMapping("sort")
    public void sort(@RequestBody final Map<ID, Long> sortInfo) {
        final var entityClass = service.getEntityClass();
        Assert.isTrue(Sortable.class.isAssignableFrom(entityClass), entityClass.getName() + " is not a Sortable");
        final Predicate predicate;
        final var idClass = service.getIdClass();
        if (String.class.equals(idClass)) {
            predicate = QueryDslUtils.getIdStringPath(entityClass)
                    .in(sortInfo.keySet().stream().map(String.class::cast).collect(Collectors.toList()));
        } else if (Long.class.equals(idClass)) {
            predicate = QueryDslUtils.getIdNumberPath(entityClass)
                    .in(sortInfo.keySet().stream().map(Long.class::cast).collect(Collectors.toList()));
        } else {
            throw new NotImplementedException("not implemented for id class: " + idClass.getName());
        }
        final var beans = service.findAll(predicate, Sort.unsorted());
        sortService.sort(beans.stream()
                .collect(Collectors.toMap(Sortable.class::cast, sortable -> sortInfo.get(sortable.getId()))));
    }

    @GetMapping("find-one")
    @SneakyThrows
    public T findOne(@RequestParam(value = "id", required = false) final T entity) {
        return Optional.ofNullable(entity).orElse(service.getEntityClass().getDeclaredConstructor().newInstance());
    }

    @GetMapping("find-page")
    public Page<T> findPage(final Predicate predicate, final Pageable pageable) {
        return service.findPage(predicate, pageable);
    }

    @GetMapping("find-all")
    public List<T> findAll(final Predicate predicate, final Sort sort) {
        return service.findAll(predicate, sort);
    }

}
