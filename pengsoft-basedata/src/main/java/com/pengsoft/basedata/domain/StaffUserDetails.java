package com.pengsoft.basedata.domain;

import java.util.Collection;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pengsoft.basedata.json.DepartmentJsonSerializer;
import com.pengsoft.basedata.json.JobCollectionJsonSerializer;
import com.pengsoft.basedata.json.JobJsonSerializer;
import com.pengsoft.security.domain.Role;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

/**
 * The implementer of {@link UserDetails} for {@link Staff}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
public class StaffUserDetails extends PersonUserDetails {

    private static final long serialVersionUID = -4262963739413486380L;

    @JsonSerialize(using = JobCollectionJsonSerializer.class)
    private Collection<Job> jobs;

    @JsonSerialize(using = JobJsonSerializer.class)
    private Job currentJob;

    @JsonSerialize(using = JobJsonSerializer.class)
    private Job primaryJob;

    @JsonSerialize(using = DepartmentJsonSerializer.class)
    private Department department;

    public StaffUserDetails(final Staff staff, final Collection<Job> jobs, final Collection<Organization> organizations,
            final Collection<Role> roles, final Collection<? extends GrantedAuthority> authorities) {
        super(staff.getPerson(), organizations, roles, null, authorities);
        setJobs(jobs);
        setPrimaryJob(staff.getJob());
        setCurrentJob(staff.getJob());
        setDepartment(staff.getDepartment());
        setOrganization(staff.getOrganization());
    }

}
