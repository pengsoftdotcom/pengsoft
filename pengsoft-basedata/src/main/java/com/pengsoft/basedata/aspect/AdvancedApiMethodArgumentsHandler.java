package com.pengsoft.basedata.aspect;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Named;

import com.pengsoft.basedata.domain.OwnedExt;
import com.pengsoft.basedata.repository.OwnedExtRepository;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.security.aspect.ApiMethodArgumentsHandler;
import com.pengsoft.security.aspect.DefaultApiMethodArgumentsHandler;
import com.pengsoft.security.domain.Owned;
import com.pengsoft.support.domain.Entity;
import com.pengsoft.support.exception.MissingConfigurationException;
import com.pengsoft.support.util.QueryDslUtils;
import com.pengsoft.support.util.StringUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;

/**
 * The implementer of {@link ApiMethodArgumentsHandler}, add implementation for
 * {@link OwnedExt}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Named
public class AdvancedApiMethodArgumentsHandler<T extends Entity<ID>, ID extends Serializable>
        extends DefaultApiMethodArgumentsHandler<T, ID> {

    public AdvancedApiMethodArgumentsHandler(final ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    public Predicate replace(final Class<T> entityClass, final Predicate predicate) {
        if (isAdmin(entityClass)) {
            return predicate;
        } else {
            if (OwnedExt.class.isAssignableFrom(entityClass)) {
                final var primaryJob = SecurityUtilsExt.getPrimaryJob();
                if (primaryJob != null) {
                    if (primaryJob.isOrganizationChief()) {
                        return QueryDslUtils.merge(predicate,
                                getBelongsToPredicate(entityClass, SecurityUtilsExt.getPrimaryOrganizationId()));
                    }
                    if (primaryJob.isDepartmentChief()) {
                        return QueryDslUtils.merge(predicate,
                                getControlledByPredicate(entityClass, SecurityUtilsExt.getPrimaryDepartmentId()));
                    }
                }
            }
            return super.replace(entityClass, predicate);
        }
    }

    private BooleanExpression getBelongsToPredicate(final Class<T> entityClass, final String primaryOrganizationId) {
        final var belongsToPath = (StringPath) QueryDslUtils.getPath(entityClass, "belongsTo");
        return belongsToPath.eq(primaryOrganizationId);
    }

    private BooleanExpression getControlledByPredicate(final Class<T> entityClass, final String primaryDepartmentId) {
        final var controlledBy = (StringPath) QueryDslUtils.getPath(entityClass, "controlledBy");
        return controlledBy.eq(primaryDepartmentId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean check(final Owned entity) {
        final var id = ((T) entity).getId();
        final var primaryJob = SecurityUtilsExt.getPrimaryJob();
        if (id != null && OwnedExt.class.isAssignableFrom(entity.getClass()) && primaryJob != null) {
            final var primaryDepartmentId = SecurityUtilsExt.getPrimaryDepartmentId();
            final var controlledBy = ((OwnedExt) entity).getControlledBy();
            if (primaryJob.isDepartmentChief() && StringUtils.equals(primaryDepartmentId, controlledBy)) {
                return true;
            }
            final var primaryOrganizationId = SecurityUtilsExt.getPrimaryOrganizationId();
            final var belongsTo = ((OwnedExt) entity).getBelongsTo();
            if (primaryJob.isOrganizationChief() && StringUtils.equals(primaryOrganizationId, belongsTo)) {
                return true;
            }
        }
        return super.check(entity);
    }

    @Override
    public boolean check(final Class<T> entityClass, final Collection<ID> ids) {
        if (isAdmin(entityClass)) {
            return true;
        } else {
            final var primaryJob = SecurityUtilsExt.getPrimaryJob();
            if (OwnedExt.class.isAssignableFrom(entityClass) && primaryJob != null) {
                final var repository = (OwnedExtRepository) getRepositories().getRepositoryFor(entityClass).orElseThrow(
                        () -> new MissingConfigurationException("no repository for class: " + entityClass.getName()));
                var matched = false;
                final var stringIds = ids.stream().map(String.class::cast).collect(Collectors.toList());
                if (primaryJob.isOrganizationChief()) {
                    matched = repository.countByIdInAndBelongsTo(stringIds,
                            SecurityUtilsExt.getPrimaryOrganizationId()) == ids.size();
                }

                if (primaryJob.isDepartmentChief()) {
                    matched = repository.countByIdInAndControlledBy(stringIds,
                            SecurityUtilsExt.getPrimaryDepartmentId()) == ids.size();
                }

                if (matched) {
                    return true;
                }
            }
            return super.check(entityClass, ids);
        }
    }

}
