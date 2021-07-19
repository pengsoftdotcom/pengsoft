package com.pengsoft.basedata.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.repository.StaffRepository;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.exception.BusinessException;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link StaffService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class StaffServiceImpl extends EntityServiceImpl<StaffRepository, Staff, String> implements StaffService {

    @Override
    public Staff save(final Staff staff) {
        final var leftQuantity = staff.getJob().getQuantity() - getRepository().countByJob(staff.getJob())
                - (StringUtils.isBlank(staff.getId()) ? 1 : 0);
        if (leftQuantity < 0) {
            throw new BusinessException("staff.save.exceeded", staff.getJob().getName());
        }
        getRepository().findOneByPersonAndJob(staff.getPerson(), staff.getJob()).ifPresent(source -> {
            if (EntityUtils.ne(source, staff)) {
                throw new BusinessException("staff.save.unique", staff.getPerson().getName(), staff.getJob().getName());
            }
        });
        final var department = staff.getJob().getDepartment();
        staff.setDepartment(department);
        super.save(staff);
        if (staff.isPrimary()) {
            setPrimaryJob(staff.getPerson(), staff.getJob());
        }
        getRepository().flush();
        final var admin = department.getOrganization().getAdmin();
        if (admin != null) {
            final var createdBy = admin.getUser().getId();
            final var updatedBy = SecurityUtils.getUserId();
            final var adminStaff = getRepository().findOneByPersonAndPrimaryTrue(admin).orElse(null);
            final var controlledBy = Optional.ofNullable(adminStaff).map(Staff::getJob).map(Job::getDepartment)
                    .map(Department::getId).orElse(null);
            final var belongsTo = Optional.ofNullable(adminStaff).map(Staff::getJob).map(Job::getDepartment)
                    .map(Department::getOrganization).map(Organization::getId).orElse(null);
            getRepository().updateBelonging(List.of(staff), createdBy, updatedBy, controlledBy, belongsTo);
        }
        return staff;
    }

    @Override
    public void setPrimaryJob(final Person person, final Job job) {
        findAllByPerson(person).forEach(staff -> {
            if (EntityUtils.eq(staff.getJob(), job)) {
                staff.setPrimary(true);
                super.save(staff);
            } else {
                if (staff.isPrimary()) {
                    staff.setPrimary(false);
                    super.save(staff);
                }
            }
        });
    }

    @Override
    public Optional<Staff> findOneByPersonAndPrimaryTrue(final Person person) {
        return getRepository().findOneByPersonAndPrimaryTrue(person);
    }

    @Override
    public List<Staff> findAllByPerson(final Person person) {
        return getRepository().findAllByPerson(person);
    }

    @Override
    public List<Staff> findAllByJobIn(final List<Job> jobs) {
        return getRepository().findAllByJobIdIn(jobs.stream().map(Job::getId).collect(Collectors.toList()));
    }

    @Override
    protected Sort getDefaultSort() {
        return Sort.by(Direction.ASC, "job.parentIds");
    }

}
