package com.pengsoft.basedata.facade;

import java.util.List;
import java.util.Optional;
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
import com.pengsoft.support.facade.TreeEntityFacadeImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.support.util.StringUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Inject
    private PersonService personService;

    @Inject
    private UserService userService;

    @Inject
    private RoleService roleService;

    @Override
    public void setAdmin(Organization organization, Person targetAdmin) {
        Person sourceAdmin = null;
        User sourceUser = null;
        User targetUser = null;
        if (targetAdmin != null) {
            if (StringUtils.isBlank(targetAdmin.getId())) {
                var mobile = targetAdmin.getMobile();
                personService.findOneByMobile(mobile)
                        .ifPresent(person -> getExceptions().constraintViolated("mobile", mobile));
                targetUser = new User(targetAdmin.getMobile(), UUID.randomUUID().toString());
                targetUser.setMobile(mobile);
                targetUser = userService.saveWithoutValidation(targetUser);
                targetAdmin.setUser(targetUser);
                targetAdmin.setCreatedBy(targetUser.getId());
            } else {
                var id = targetAdmin.getId();
                sourceAdmin = personService.findOne(id).orElseThrow(() -> getExceptions().entityNotExists(id));
                BeanUtils.copyProperties(targetAdmin, sourceAdmin);
                targetAdmin = sourceAdmin;
            }
            userService.grantRoles(targetAdmin.getUser(), List.of(roleService.findOneByCode(Role.ORG_ADMIN)
                    .orElseThrow(() -> getExceptions().entityNotExists(Role.ORG_ADMIN))));
            personService.save(targetAdmin);

        }
        sourceAdmin = organization.getAdmin();
        if (sourceAdmin != null && EntityUtils.ne(sourceAdmin, targetAdmin) && countByAdmin(sourceAdmin) == 1) {
            sourceUser = sourceAdmin.getUser();
            sourceUser.getUserRoles().removeIf(userRole -> userRole.getRole().getCode().equals(Role.ORG_ADMIN));
            userService.grantRoles(sourceUser,
                    sourceUser.getUserRoles().stream().map(UserRole::getRole).collect(Collectors.toList()));
        }
        getService().setAdmin(organization, targetAdmin);
    }

    @Override
    public Optional<Organization> findOneByCode(String code) {
        return getService().findOneByCode(code);
    }

    @Override
    public Optional<Organization> findOneByName(String name) {
        return getService().findOneByName(name);
    }

    @Override
    public List<Organization> findAllByAdmin(final Person admin) {
        return getService().findAllByAdmin(admin);
    }

    @Override
    public long countByAdmin(Person admin) {
        return getService().countByAdmin(admin);
    }

    @Override
    public Page<Organization> findPageOfAvailableConsumers(Organization supplier, Pageable pageable) {
        return getService().findPageOfAvailableConsumers(supplier, pageable);
    }

    @Override
    public List<Organization> findAllAvailableConsumers(Organization supplier) {
        return getService().findAllAvailableConsumers(supplier);
    }

    @Override
    public Page<Organization> findPageOfAvailableSuppliers(Organization consumer, Pageable pageable) {
        return getService().findPageOfAvailableSuppliers(consumer, pageable);
    }

    @Override
    public List<Organization> findAllAvailableSuppliers(Organization consumer) {
        return getService().findAllAvailableSuppliers(consumer);
    }

}
