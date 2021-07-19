package com.pengsoft.basedata.api;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.service.PersonUserDetailsService;
import com.pengsoft.security.annotation.AuthorityChanged;
import com.pengsoft.security.annotation.Authorized;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Authorized
@RestController
@RequestMapping("api/user-details")
public class PersonUserDetailsApi {

    @Inject
    private PersonUserDetailsService service;

    @AuthorityChanged
    @PostMapping("set-primary-organization")
    public UserDetails setPrimaryOrganization(@RequestParam("id") final Organization organization) {
        return service.setPrimaryOrganization(organization);
    }

    @AuthorityChanged
    @PostMapping("save-person")
    public UserDetails savePerson(@RequestBody Person person) {
        return service.savePerson(person);
    }

}
