package com.pengsoft.basedata.service;

import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.JobRole;
import com.pengsoft.basedata.domain.PersonUserDetails;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.domain.StaffUserDetails;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.security.domain.DefaultUserDetails;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.util.EntityUtils;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link StaffUserDetailsService}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class StaffUserDetailsServiceImpl extends PersonUserDetailsServiceImpl implements StaffUserDetailsService {

    @Inject
    private StaffService staffService;

    @Inject
    private PersonService personService;

    @Override
    public DefaultUserDetails setPrimaryJob(final Job job) {
        final var userDetails = (StaffUserDetails) SecurityUtils.getUserDetails();
        if (userDetails.getJobs().stream().anyMatch(j -> EntityUtils.eq(j, job))) {
            personService.findOne(SecurityUtilsExt.getPerson().getId()).ifPresent(userDetails::setPerson);
            staffService.setPrimaryJob(SecurityUtilsExt.getPerson(), job);
            userDetails.setPrimaryJob(job);
            userDetails.setPrimaryDepartment(job.getDepartment());
            userDetails.setPrimaryOrganization(job.getDepartment().getOrganization());
            final var organization = userDetails.getPrimaryOrganization();
            final var admin = organization.getAdmin();
            if (admin == null || EntityUtils.ne(admin, userDetails.getPerson())) {
                userDetails.getRoles().clear();
            }
            final var roles = job.getJobRoles().stream().map(JobRole::getRole).collect(Collectors.toList());
            userDetails.getRoles().addAll(roles);
            final var authorities = new ArrayList<GrantedAuthority>();
            userDetails.getRoles().forEach(role -> authorities.addAll(getAllAuthorities(role)));
            userDetails.setAuthorities(authorities.stream().distinct().collect(Collectors.toList()));
        }
        return userDetails;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final var userDetails = (DefaultUserDetails) super.loadUserByUsername(username);
        if (userDetails instanceof PersonUserDetails) {
            final var person = ((PersonUserDetails) userDetails).getPerson();
            final var roles = userDetails.getRoles();
            final var authorities = userDetails.getAuthorities();
            return staffService.findOneByPersonAndPrimaryTrue(person).map(staff -> {
                final var staffs = staffService.findAllByPerson(person);
                final var jobs = staffs.stream().map(Staff::getJob).collect(Collectors.toList());
                final var job = staff.getJob();
                roles.addAll(job.getJobRoles().stream().map(JobRole::getRole).collect(Collectors.toList()));
                roles.forEach(role -> authorities.addAll(getAllAuthorities(role)));
                return new StaffUserDetails(staff, jobs, roles, authorities);
            }).map(StaffUserDetails.class::cast).orElse((StaffUserDetails) userDetails);
        } else {
            return userDetails;
        }
    }

}
