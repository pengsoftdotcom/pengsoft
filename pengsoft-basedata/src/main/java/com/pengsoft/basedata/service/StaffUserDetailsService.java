package com.pengsoft.basedata.service;

import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.PersonUserDetails;
import com.pengsoft.basedata.domain.StaffUserDetails;
import com.pengsoft.security.domain.DefaultUserDetails;
import com.pengsoft.security.service.DefaultUserDetailsService;

/**
 * User details service for {@link }
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface StaffUserDetailsService extends DefaultUserDetailsService {

    /**
     * Set the primary job.
     *
     * @param job The primary job.
     * @return {@link StaffUserDetails}
     */
    DefaultUserDetails setPrimaryJob(@NotNull Job job);

    /**
     * set the current organization
     *
     * @param organization The current organization.
     * @Return {@link PersonUserDetails}
     */
    DefaultUserDetails setOrganization(@NotNull Organization organization);

}
