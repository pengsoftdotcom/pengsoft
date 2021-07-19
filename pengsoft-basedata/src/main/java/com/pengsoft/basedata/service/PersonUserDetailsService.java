package com.pengsoft.basedata.service;

import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.PersonUserDetails;
import com.pengsoft.security.service.DefaultUserDetailsService;

/**
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface PersonUserDetailsService extends DefaultUserDetailsService {

    /**
     * set the current organization
     *
     * @param organization The current organization.
     * @Return {@link PersonUserDetails}
     */
    PersonUserDetails setPrimaryOrganization(@NotNull Organization organization);

    /**
     * save person info
     *
     * @param person The person info.
     * @Return {@link PersonUserDetails}
     */
    PersonUserDetails savePerson(@NotNull Person person);

}
