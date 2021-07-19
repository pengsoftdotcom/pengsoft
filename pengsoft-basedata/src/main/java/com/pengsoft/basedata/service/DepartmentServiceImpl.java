package com.pengsoft.basedata.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.repository.DepartmentRepository;
import com.pengsoft.basedata.repository.StaffRepository;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.service.TreeEntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link DepartmentService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class DepartmentServiceImpl extends TreeEntityServiceImpl<DepartmentRepository, Department, String>
        implements DepartmentService {

    @Inject
    private StaffRepository staffRepository;

    @Override
    public Department save(final Department department) {
        final var organization = department.getOrganization();
        final var parent = Optional.ofNullable(department.getParent()).orElse(null);
        getRepository().findOneByOrganizationAndParentAndName(organization, parent, department.getName())
                .ifPresent(source -> {
                    if (EntityUtils.ne(source, department)) {
                        throw getExceptions().constraintViolated("name", "exists", department.getName());
                    }
                });
        if (StringUtils.isBlank(department.getShortName())) {
            department.setShortName(department.getName());
        }
        super.save(department);
        getRepository().flush();
        final var admin = department.getOrganization().getAdmin();
        if (admin != null) {
            final var createdBy = admin.getUser().getId();
            final var updatedBy = SecurityUtils.getUserId();
            final var staff = staffRepository.findOneByPersonAndPrimaryTrue(admin).orElse(null);
            final var controlledBy = Optional.ofNullable(staff).map(Staff::getJob).map(Job::getDepartment)
                    .map(Department::getId).orElse(null);
            final var belongsTo = Optional.ofNullable(staff).map(Staff::getJob).map(Job::getDepartment)
                    .map(Department::getOrganization).map(Organization::getId).orElse(null);
            getRepository().updateBelonging(List.of(department), createdBy, updatedBy, controlledBy, belongsTo);
        }
        return department;
    }

}
