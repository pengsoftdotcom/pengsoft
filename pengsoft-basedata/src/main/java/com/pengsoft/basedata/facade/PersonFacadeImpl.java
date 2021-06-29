package com.pengsoft.basedata.facade;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.service.PersonService;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.domain.User;
import com.pengsoft.security.service.RoleService;
import com.pengsoft.security.service.UserService;
import com.pengsoft.support.exception.MissingConfigurationException;
import com.pengsoft.support.facade.EntityFacadeImpl;

import org.springframework.stereotype.Service;

/**
 * The implementer of {@link PersonFacade}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Service
public class PersonFacadeImpl extends EntityFacadeImpl<PersonService, Person, String> implements PersonFacade {

    @Inject
    private UserService userService;

    @Inject
    private RoleService roleService;

    @Override
    public Person save(final Person person) {
        final var user = userService.save(new User(person.getMobile(), UUID.randomUUID().toString()));
        final var roles = roleService.findOneByCode(Role.USER).map(List::of)
                .orElseThrow(() -> new MissingConfigurationException("No role user configured."));
        userService.grantRoles(user, roles);
        person.setUser(user);
        return super.save(person);
    }

    @Override
    public Optional<Person> findOneByMobile(final String mobile) {
        return getService().findOneByMobile(mobile);
    }

    @Override
    public Optional<Person> findOneByUser(final User user) {
        return getService().findOneByUser(user);
    }

}
