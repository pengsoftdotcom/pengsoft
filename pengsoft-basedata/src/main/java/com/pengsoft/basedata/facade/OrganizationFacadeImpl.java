package com.pengsoft.basedata.facade;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.service.OrganizationService;
import com.pengsoft.basedata.service.PersonService;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.domain.User;
import com.pengsoft.security.domain.UserRole;
import com.pengsoft.security.service.RoleService;
import com.pengsoft.security.service.UserService;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.facade.TreeEntityFacadeImpl;
import com.pengsoft.support.util.StringUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link OrganizationFacade}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Service
public class OrganizationFacadeImpl extends TreeEntityFacadeImpl<OrganizationService, Organization, String>
        implements OrganizationFacade {

    public static final String BASEDATA_ORGANIZATION_ADMIN = "basedata_organization_admin";

    public static final String ORGANIZATION_ADMIN = "organization_admin";

    @Inject
    private PersonService personService;

    @Inject
    private UserService userService;

    @Inject
    private RoleService roleService;

    @Override
    public Organization save(final Organization organization) {
        if (organization.getAdmin() != null) {
            final var person = personService.findOneByMobile(organization.getAdmin().getMobile())
                    .orElse(organization.getAdmin());
            if (StringUtils.isBlank(person.getId())) {
                final var user = userService.findOneByMobile(person.getMobile())
                        .orElse(new User(person.getMobile(), UUID.randomUUID().toString()));
                if (StringUtils.isBlank(user.getId())) {
                    userService.saveWithoutValidation(user);
                }
                person.setUser(user);
            } else {
                BeanUtils.copyProperties(organization.getAdmin(), person, "id", "mobile", "user", "version");
            }

            if (!SecurityUtils.hasAnyRole(ORGANIZATION_ADMIN)
                    && SecurityUtils.hasAnyRole(Role.ADMIN, BASEDATA_ORGANIZATION_ADMIN)) {
                final var createdBy = person.getUser().getId();
                person.setCreatedBy(createdBy);
                organization.setCreatedBy(createdBy);
            }

            organization.setAdmin(personService.save(person));
            final var roles = person.getUser().getUserRoles().stream().map(UserRole::getRole)
                    .collect(Collectors.toList());
            if (roles.stream().noneMatch(role -> ORGANIZATION_ADMIN.equals(role.getCode()))) {
                final var role = roleService.findOneByCode(ORGANIZATION_ADMIN).orElse(new Role(ORGANIZATION_ADMIN));
                if (StringUtils.isBlank(role.getId())) {
                    roleService.save(role);
                }
                roles.add(role);
                userService.grantRoles(person.getUser(), roles);
            }
        }
        super.save(organization);
        organization.setBelongsTo(organization.getId());
        return super.save(organization);
    }

    @Override
    public List<Organization> findAllByAdmin(final Person admin) {
        return getService().findAllByAdmin(admin);
    }

}
