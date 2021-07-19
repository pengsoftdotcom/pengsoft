package com.pengsoft.basedata.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.repository.DepartmentRepository;
import com.pengsoft.basedata.repository.JobRepository;
import com.pengsoft.basedata.repository.OrganizationRepository;
import com.pengsoft.basedata.repository.PostRepository;
import com.pengsoft.basedata.repository.StaffRepository;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.service.TreeEntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.support.util.StringUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link OrganizationService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class OrganizationServiceImpl extends TreeEntityServiceImpl<OrganizationRepository, Organization, String>
        implements OrganizationService {

    @Inject
    private PostRepository postRepository;

    @Inject
    private DepartmentRepository departmentRepository;

    @Inject
    private JobRepository jobRepository;

    @Inject
    private StaffRepository staffRepository;

    @Override
    public void setAdmin(Organization organization, Person admin) {
        String createdBy = null;
        String updatedBy = null;
        String controlledBy = null;
        String belongsTo = null;
        if (admin != null) {
            createdBy = admin.getUser().getId();
            var optional = staffRepository.findOneByPersonAndPrimaryTrue(admin);
            if (optional.isPresent()) {
                var job = optional.get().getJob();
                controlledBy = job.getDepartment().getId();
                belongsTo = job.getDepartment().getOrganization().getId();
            } else {
                belongsTo = organization.getId();
            }
        } else {
            createdBy = SecurityUtils.getUserId();
        }
        updatedBy = SecurityUtils.getUserId();
        getRepository().setAdmin(organization, admin, createdBy, updatedBy, controlledBy, belongsTo);

        final var posts = postRepository.findAllByOrganization(organization);
        if (CollectionUtils.isNotEmpty(posts)) {
            postRepository.updateBelonging(posts, createdBy, updatedBy, controlledBy, belongsTo);
        }

        final var departments = departmentRepository.findAllByOrganization(organization);
        if (CollectionUtils.isNotEmpty(departments)) {
            departmentRepository.updateBelonging(departments, createdBy, updatedBy, controlledBy, belongsTo);
        }

        final var jobs = jobRepository.findAllByDepartmentOrganization(organization);
        if (CollectionUtils.isNotEmpty(jobs)) {
            jobRepository.updateBelonging(jobs, createdBy, updatedBy, controlledBy, belongsTo);
        }

        final var staffs = staffRepository.findAllByJobDepartmentOrganization(organization);
        if (CollectionUtils.isNotEmpty(staffs)) {
            staffRepository.updateBelonging(staffs, createdBy, updatedBy, controlledBy, belongsTo);
        }
    }

    @Override
    public Organization save(final Organization organization) {
        getRepository().findOneByCode(organization.getCode()).ifPresent(source -> {
            if (EntityUtils.ne(source, organization)) {
                throw getExceptions().constraintViolated("code", "exists", organization.getCode());
            }
        });
        getRepository().findOneByName(organization.getName()).ifPresent(source -> {
            if (EntityUtils.ne(source, organization)) {
                throw getExceptions().constraintViolated("name", "exists", organization.getCode());
            }
        });
        if (StringUtils.isBlank(organization.getShortName())) {
            organization.setShortName(organization.getName());
        }
        return super.save(organization);
    }

    @Override
    public Optional<Organization> findOneByCode(String code) {
        return getRepository().findOneByCode(code);
    }

    @Override
    public Optional<Organization> findOneByName(String name) {
        return getRepository().findOneByName(name);
    }

    @Override
    public List<Organization> findAllByAdmin(final Person admin) {
        return getRepository().findAllByAdmin(admin);
    }

    @Override
    public long countByAdmin(Person admin) {
        return getRepository().countByAdmin(admin);
    }

    @Override
    public Page<Organization> findPageOfAvailableConsumers(Organization supplier, Pageable pageable) {
        return getRepository().findPageOfAvailableConsumers(supplier, pageable);
    }

    @Override
    public List<Organization> findAllAvailableConsumers(Organization supplier) {
        return getRepository().findAllAvailableConsumers(supplier);
    }

    @Override
    public Page<Organization> findPageOfAvailableSuppliers(Organization consumer, Pageable pageable) {
        return getRepository().findPageOfAvailableSuppliers(consumer, pageable);
    }

    @Override
    public List<Organization> findAllAvailableSuppliers(Organization consumer) {
        return getRepository().findAllAvailableSuppliers(consumer);
    }

}
