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
import com.pengsoft.security.domain.User;
import com.pengsoft.security.domain.UserRole;
import com.pengsoft.security.service.RoleService;
import com.pengsoft.security.service.UserService;
import com.pengsoft.support.facade.EntityFacadeImpl;
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
public class OrganizationFacadeImpl extends EntityFacadeImpl<OrganizationService, Organization, String>
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
    public Organization save(Organization target) {
        Organization source = null;
        Person sourceAdmin = null;
        Person targetAdmin = target.getAdmin();
        User sourceUser = null;
        User targetUser = null;

        if (targetAdmin != null) {
            if (StringUtils.isBlank(targetAdmin.getId())) {
                personService.findOneByMobile(targetAdmin.getMobile())
                        .ifPresent(person -> getExceptions().entityNotExists(targetAdmin.getMobile()));
                targetUser = userService.save(new User(targetAdmin.getMobile(), UUID.randomUUID().toString()));
                userService.grantRoles(targetUser, List.of(roleService.findOneByCode(ORGANIZATION_ADMIN)
                        .orElseThrow(() -> getExceptions().entityNotExists(ORGANIZATION_ADMIN))));
                targetAdmin.setUser(targetUser);
                targetAdmin.setCreatedBy(targetUser.getId());
            } else {
                sourceAdmin = personService.findOne(targetAdmin.getId())
                        .orElseThrow(() -> getExceptions().entityNotExists(targetAdmin.getId()));
                BeanUtils.copyProperties(sourceAdmin, targetAdmin, "id", "mobile", "user", "version");
            }
            personService.save(targetAdmin);
        }

        if (StringUtils.isNotBlank(target.getId())) {
            source = findOne(target.getId()).orElseThrow(() -> getExceptions().entityNotExists(target.getId()));
            sourceAdmin = source.getAdmin();
            if (sourceAdmin != null && targetAdmin != null && EntityUtils.ne(sourceAdmin, targetAdmin)
                    && countByAdmin(sourceAdmin) == 1) {
                sourceUser = sourceAdmin.getUser();
                sourceUser.getUserRoles().removeIf(userRole -> userRole.getRole().getCode().equals(ORGANIZATION_ADMIN));
                userService.grantRoles(sourceUser,
                        sourceUser.getUserRoles().stream().map(UserRole::getRole).collect(Collectors.toList()));
            }
        }

        target.setAdmin(targetAdmin);
        if (targetUser != null) {
            target.setCreatedBy(targetUser.getId());
        }
        return super.save(target);
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
