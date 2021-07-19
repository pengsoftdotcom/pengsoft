package com.pengsoft.basedata.domain;

import java.util.Collection;
import java.util.List;

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
    private Job primaryJob;

    @JsonSerialize(using = DepartmentJsonSerializer.class)
    private Department primaryDepartment;

    public StaffUserDetails(final Staff staff, final List<Job> jobs, final List<Role> roles,
            final List<GrantedAuthority> authorities) {
        super(staff.getPerson(), null, roles, null, authorities);
        setJobs(jobs);
        setPrimaryJob(staff.getJob());
        setPrimaryDepartment(staff.getDepartment());
        setPrimaryOrganization(staff.getDepartment().getOrganization());
    }

}
