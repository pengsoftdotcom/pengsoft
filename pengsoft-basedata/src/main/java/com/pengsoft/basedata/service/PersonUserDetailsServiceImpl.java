package com.pengsoft.basedata.service;

import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.PersonUserDetails;
import com.pengsoft.security.domain.DefaultUserDetails;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.service.DefaultUserDetailsServiceImpl;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.util.StringUtils;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link PersonUserDetailsService}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Service
public class PersonUserDetailsServiceImpl extends DefaultUserDetailsServiceImpl implements PersonUserDetailsService {

    @Inject
    private PersonService personService;

    @Inject
    private OrganizationService organizationService;

    @Override
    public PersonUserDetails setPrimaryOrganization(final Organization organization) {
        final var userDetails = (PersonUserDetails) SecurityUtils.getUserDetails();
        final var role = userDetails.getPrimaryRole();
        if (role != null && StringUtils.equals(Role.ORG_ADMIN, role.getCode())) {
            userDetails.setPrimaryOrganization(organization);
        }
        return userDetails;
    }

    @Override
    public PersonUserDetails savePerson(@NotNull Person person) {
        final var userDetails = (PersonUserDetails) SecurityUtils.getUserDetails();
        userDetails.setPerson(personService.save(person));
        return userDetails;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        final var userDetails = (DefaultUserDetails) super.loadUserByUsername(username);
        return personService.findOneByUser(userDetails.getUser()).map(person -> {
            final var roles = userDetails.getRoles();
            final var authorities = userDetails.getAuthorities();
            List<Organization> organizations = null;
            if (roles.stream().anyMatch(role -> role.getCode().equals(Role.ORG_ADMIN))) {
                organizations = organizationService.findAllByAdmin(person);
            }
            final var primaryRole = userDetails.getPrimaryRole();
            return new PersonUserDetails(person, organizations, roles, primaryRole, authorities);
        }).map(DefaultUserDetails.class::cast).orElse(userDetails);
    }

}
