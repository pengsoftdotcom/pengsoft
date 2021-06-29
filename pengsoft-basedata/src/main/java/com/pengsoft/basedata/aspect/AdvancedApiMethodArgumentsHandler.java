package com.pengsoft.basedata.aspect;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.OwnedExt;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.facade.OrganizationFacade;
import com.pengsoft.basedata.repository.OwnedExtRepository;
import com.pengsoft.basedata.service.JobService;
import com.pengsoft.basedata.service.StaffService;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.security.aspect.ApiMethodArgumentsHandler;
import com.pengsoft.security.aspect.DefaultApiMethodArgumentsHandler;
import com.pengsoft.security.domain.Owned;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.domain.User;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.domain.Entity;
import com.pengsoft.support.exception.MissingConfigurationException;
import com.pengsoft.support.util.QueryDslUtils;
import com.pengsoft.support.util.StringUtils;
import com.querydsl.core.BooleanBuilder;
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

    public static final String ORGANIZATION_ADMIN = "organization_admin";

    @Inject
    private OrganizationFacade organizationFacade;

    @Inject
    private JobService jobService;

    @Inject
    private StaffService staffService;

    public AdvancedApiMethodArgumentsHandler(final ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    public Predicate replace(final Class<T> entityClass, final Predicate predicate) {
        if (SecurityUtilsExt.hasAnyRole(Role.ADMIN, getModuleAdminRoleCode(entityClass),
                getEntityAdminRoleCode(entityClass))) {
            return predicate;
        } else {
            if (OwnedExt.class.isAssignableFrom(entityClass)) {
                final var result = new BooleanBuilder();
                Organization organization = null;
                if (isOrganizationAdmin()) {
                    organization = SecurityUtilsExt.getOrganization();
                } else {
                    organization = SecurityUtilsExt.getOrganization();
                    final var job = SecurityUtilsExt.getCurrentJob();
                    if (job != null) {
                        result.or(getCreatedByPredicate(entityClass, job));
                    }
                }
                result.or(getBelongsToPredicate(entityClass, organization));
                if (QueryDslUtils.isNotBlank(result)) {
                    return QueryDslUtils.merge(predicate, result);
                }
            }
            return super.replace(entityClass, predicate);
        }
    }

    private BooleanExpression getBelongsToPredicate(final Class<T> entityClass, final Organization organization) {
        final var organizations = organizationFacade.findAllByParentIdsStartsWith(
                organization.getParentIds() + StringUtils.GLOBAL_SEPARATOR + organization.getId());
        organizations.add(organization);
        final var belongsToPath = (StringPath) QueryDslUtils.getPath(entityClass, "belongsTo");
        return belongsToPath.in(organizations.stream().map(Organization::getId).collect(Collectors.toList()));
    }

    private BooleanExpression getCreatedByPredicate(final Class<T> entityClass, final Job job) {
        final var jobs = jobService
                .findAllByParentIdsStartsWith(job.getParentIds() + StringUtils.GLOBAL_SEPARATOR + job.getId());
        jobs.add(job);
        final var staffs = staffService.findAllByJobIn(jobs);
        final var createdByPath = (StringPath) QueryDslUtils.getPath(entityClass, "createdBy");
        final var createdBy = staffs.stream().map(Staff::getPerson).map(Person::getUser).map(User::getId).distinct()
                .collect(Collectors.toList());
        return createdByPath.in(createdBy);
    }

    private boolean isOrganizationAdmin() {
        final var role = SecurityUtils.getCurrentRole();
        return role != null && StringUtils.equals(ORGANIZATION_ADMIN, role.getCode());
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean check(final Owned entity) {
        final var id = ((T) entity).getId();
        if (id != null && OwnedExt.class.isAssignableFrom(entity.getClass())
                && SecurityUtilsExt.getCurrentJob() != null) {
            final var job = SecurityUtilsExt.getCurrentJob();
            if (job.isOrganizationChief()
                    && StringUtils.equals(SecurityUtilsExt.getOrganizationId(), ((OwnedExt) entity).getBelongsTo())) {
                return true;
            }
            if (job.isDepartmentChief() && StringUtils.equals(SecurityUtilsExt.getOrganizationId(),
                    ((OwnedExt) entity).getControlledBy())) {
                return true;
            }
        }
        return super.check(entity);
    }

    @Override
    public boolean check(final Class<T> entityClass, final Collection<ID> ids) {
        if (SecurityUtilsExt.hasAnyRole(Role.ADMIN, getModuleAdminRoleCode(entityClass),
                getEntityAdminRoleCode(entityClass))) {
            return true;
        } else {
            if (OwnedExt.class.isAssignableFrom(entityClass) && SecurityUtilsExt.getCurrentJob() != null) {
                final var repository = (OwnedExtRepository) getRepositories().getRepositoryFor(entityClass).orElseThrow(
                        () -> new MissingConfigurationException("no repository for class: " + entityClass.getName()));
                final var job = SecurityUtilsExt.getCurrentJob();
                var matched = false;
                final var stringIds = ids.stream().map(String.class::cast).collect(Collectors.toList());
                if (job.isOrganizationChief()) {
                    matched = repository.countByIdInAndBelongsTo(stringIds, SecurityUtilsExt.getOrganizationId()) == ids
                            .size();
                }

                if (job.isDepartmentChief()) {
                    matched = repository.countByIdInAndControlledBy(stringIds,
                            SecurityUtilsExt.getDepartmentId()) == ids.size();
                }

                if (matched) {
                    return true;
                }
            }
            return super.check(entityClass, ids);
        }
    }

}
