package com.pengsoft.security.aspect;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Named;

import com.pengsoft.security.domain.Owned;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.repository.OwnedRepository;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.domain.Entity;
import com.pengsoft.support.exception.MissingConfigurationException;
import com.pengsoft.support.util.QueryDslUtils;
import com.pengsoft.support.util.StringUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.support.Repositories;

import lombok.Getter;

/**
 * Default implementer of {@link ApiMethodArgumentsHandler}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
public class DefaultApiMethodArgumentsHandler<T extends Entity<ID>, ID extends Serializable>
        implements ApiMethodArgumentsHandler<T, ID> {

    @Getter
    private final Repositories repositories;

    public DefaultApiMethodArgumentsHandler(final ApplicationContext applicationContext) {
        repositories = new Repositories(applicationContext);
    }

    @Override
    public Predicate replace(final Class<T> entityClass, final Predicate predicate) {
        if (SecurityUtils.hasAnyRole(Role.ADMIN, getModuleAdminRoleCode(entityClass),
                getEntityAdminRoleCode(entityClass))) {
            return predicate;
        } else {
            return ((StringPath) QueryDslUtils.getPath(entityClass, "createdBy")).eq(SecurityUtils.getUserId())
                    .and(predicate);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean check(final Owned entity) {
        final var id = ((T) entity).getId();
        final var userId = SecurityUtils.getUserId();
        if (id == null) {
            return true;
        } else {
            final var entityClass = (Class<T>) entity.getClass();
            if (SecurityUtils.hasAnyRole(Role.ADMIN, getModuleAdminRoleCode(entityClass),
                    getEntityAdminRoleCode(entityClass))) {
                return true;
            } else {
                return StringUtils.equals(userId, entity.getCreatedBy());
            }
        }
    }

    @Override
    public boolean check(final Class<T> entityClass, final Collection<ID> ids) {
        if (SecurityUtils.hasAnyRole(Role.ADMIN, getModuleAdminRoleCode(entityClass),
                getEntityAdminRoleCode(entityClass))) {
            return true;
        } else {
            return repositories.getRepositoryFor(entityClass).map(OwnedRepository.class::cast)
                    .map(repository -> repository.countByIdInAndCreatedBy(
                            ids.stream().map(String.class::cast).collect(Collectors.toList()),
                            SecurityUtils.getUserId()) == ids.size())
                    .orElseThrow(() -> new MissingConfigurationException(
                            "no repository for class: " + entityClass.getName()));
        }
    }

    protected String getModuleAdminRoleCode(final Class<T> entityClass) {
        final var moduleCode = SecurityUtils.getModuleCodeFromEntityClass(entityClass);
        return StringUtils.join(new String[] { moduleCode, Role.ADMIN }, StringUtils.UNDERLINE);
    }

    protected String getEntityAdminRoleCode(final Class<T> entityClass) {
        final var moduleCode = SecurityUtils.getModuleCodeFromEntityClass(entityClass);
        final var entityCode = SecurityUtils.getEntityCodeFromEntityClass(entityClass);
        return StringUtils.join(new String[] { moduleCode, entityCode, Role.ADMIN }, StringUtils.UNDERLINE);
    }

}
