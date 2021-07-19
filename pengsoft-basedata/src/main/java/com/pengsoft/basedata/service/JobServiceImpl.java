package com.pengsoft.basedata.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.JobRole;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.repository.JobRepository;
import com.pengsoft.basedata.repository.JobRoleRepository;
import com.pengsoft.basedata.repository.StaffRepository;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.service.TreeEntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link JobService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class JobServiceImpl extends TreeEntityServiceImpl<JobRepository, Job, String> implements JobService {

    @Inject
    private JobRoleRepository jobRoleRepository;

    @Inject
    private StaffRepository staffRepository;

    @Override
    public Job save(final Job job) {
        final var department = job.getDepartment();
        final var parent = Optional.ofNullable(job.getParent()).orElse(null);
        getRepository().findOneByDepartmentAndParentAndName(department, parent, job.getName()).ifPresent(source -> {
            if (EntityUtils.ne(source, job)) {
                throw getExceptions().constraintViolated("name", "exists", job.getName());
            }
        });
        super.save(job);
        getRepository().flush();
        final var admin = job.getDepartment().getOrganization().getAdmin();
        if (admin != null) {
            final var createdBy = admin.getUser().getId();
            final var updatedBy = SecurityUtils.getUserId();
            final var staff = staffRepository.findOneByPersonAndPrimaryTrue(admin).orElse(null);
            final var controlledBy = Optional.ofNullable(staff).map(Staff::getJob).map(Job::getDepartment)
                    .map(Department::getId).orElse(null);
            final var belongsTo = Optional.ofNullable(staff).map(Staff::getJob).map(Job::getDepartment)
                    .map(Department::getOrganization).map(Organization::getId).orElse(null);
            getRepository().updateBelonging(List.of(job), createdBy, updatedBy, controlledBy, belongsTo);
        }
        return job;
    }

    @Override
    public void grantRoles(final Job job, final List<Role> roles) {
        final var source = job.getJobRoles();
        final var target = roles.stream().map(role -> new JobRole(job, role)).collect(Collectors.toList());
        final var deleted = source.stream()
                .filter(s -> target.stream().noneMatch(
                        t -> EntityUtils.eq(s.getJob(), t.getJob()) && EntityUtils.eq(s.getRole(), t.getRole())))
                .collect(Collectors.toList());
        jobRoleRepository.deleteAll(deleted);
        source.removeAll(deleted);
        final var created = target.stream()
                .filter(t -> source.stream().noneMatch(
                        s -> EntityUtils.eq(t.getJob(), s.getJob()) && EntityUtils.eq(t.getRole(), s.getRole())))
                .collect(Collectors.toList());
        jobRoleRepository.saveAll(created);
        source.addAll(created);
        super.save(job);
    }

}
