package com.pengsoft.basedata.facade;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.service.PersonService;
import com.pengsoft.basedata.service.StaffService;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.domain.User;
import com.pengsoft.security.service.UserService;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.facade.EntityFacadeImpl;
import com.pengsoft.support.util.StringUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link StaffFacade}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Service
public class StaffFacadeImpl extends EntityFacadeImpl<StaffService, Staff, String> implements StaffFacade {

    @Inject
    private PersonService personService;

    @Inject
    private UserService userService;

    @Override
    public Staff save(final Staff staff) {
        final var person = personService.findOneByMobile(staff.getPerson().getMobile()).orElse(staff.getPerson());
        if (StringUtils.isBlank(person.getId())) {
            final var user = userService.findOneByMobile(person.getMobile())
                    .orElse(new User(person.getMobile(), UUID.randomUUID().toString()));
            if (StringUtils.isBlank(user.getId())) {
                userService.save(user);
            }
            person.setUser(user);
        } else {
            BeanUtils.copyProperties(staff.getPerson(), person, "id", "mobile", "user", "version");
        }
        if (!SecurityUtils.hasAnyRole(OrganizationFacadeImpl.ORGANIZATION_ADMIN)
                && SecurityUtils.hasAnyRole(Role.ADMIN, OrganizationFacadeImpl.BASEDATA_ORGANIZATION_ADMIN)) {
            final var organization = staff.getDepartment().getOrganization();
            person.setCreatedBy(organization.getCreatedBy());
            staff.setCreatedBy(organization.getCreatedBy());
            staff.setBelongsTo(organization.getId());
        }
        staff.setPerson(personService.save(person));
        return super.save(staff);
    }

    @Override
    public void setPrimaryJob(final Person person, final Job job) {
        getService().setPrimaryJob(person, job);
    }

    @Override
    public Optional<Staff> findOneByPersonAndPrimaryTrue(final Person person) {
        return getService().findOneByPersonAndPrimaryTrue(person);
    }

    @Override
    public List<Staff> findAllByPerson(final Person person) {
        return getService().findAllByPerson(person);
    }

    @Override
    public List<Staff> findAllByJobIn(final List<Job> jobs) {
        return getService().findAllByJobIn(jobs);
    }

}
