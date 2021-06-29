package com.pengsoft.security.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.pengsoft.security.domain.Authority;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.domain.RoleAuthority;
import com.pengsoft.security.repository.RoleAuthorityRepository;
import com.pengsoft.security.repository.RoleRepository;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.domain.Entity;
import com.pengsoft.support.service.TreeEntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link RoleService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class RoleServiceImpl extends TreeEntityServiceImpl<RoleRepository, Role, String> implements RoleService {

    @Inject
    private RoleAuthorityRepository roleAuthorityRepository;

    @Override
    public Role save(final Role role) {
        findOneByCode(role.getCode()).ifPresent(source -> {
            if (EntityUtils.ne(source, role)) {
                throw getExceptions().constraintViolated("code", "exists", role.getCode());
            }
        });
        return super.save(role);
    }

    @Override
    public Role saveEntityAdmin(final Class<? extends Entity<? extends Serializable>> entityClass) {
        final var admin = createRoleIfNotExists(null, Role.ADMIN);
        final var moduleAdminCode = SecurityUtils.getModuleAdminCode(entityClass);
        final var moduleAdmin = createRoleIfNotExists(admin, moduleAdminCode);
        final var entityAdminCode = SecurityUtils.getEntityAdminCode(entityClass);
        return createRoleIfNotExists(moduleAdmin, entityAdminCode);
    }

    private Role createRoleIfNotExists(final Role parent, final String code) {
        final Optional<Role> optional = findOneByCode(code);
        if (optional.isEmpty()) {
            return super.save(new Role(parent, code));
        } else {
            return optional.get();
        }
    }

    @Override
    public void grantAuthorities(final Role role, final List<Authority> authorities) {
        final var source = role.getRoleAuthorities();
        final var target = authorities.stream().map(authority -> new RoleAuthority(role, authority))
                .collect(Collectors.toList());
        final var deleted = source
                .stream().filter(
                        s -> target.stream()
                                .noneMatch(t -> EntityUtils.eq(s.getRole(), t.getRole())
                                        && EntityUtils.eq(s.getAuthority(), t.getAuthority())))
                .collect(Collectors.toList());
        roleAuthorityRepository.deleteAll(deleted);
        source.removeAll(deleted);
        final var created = target
                .stream().filter(
                        t -> source.stream()
                                .noneMatch(s -> EntityUtils.eq(t.getRole(), s.getRole())
                                        && EntityUtils.eq(t.getAuthority(), s.getAuthority())))
                .collect(Collectors.toList());
        roleAuthorityRepository.saveAll(created);
        source.addAll(created);
        super.save(role);
    }

    @Override
    public Optional<Role> findOneByCode(final String code) {
        return getRepository().findOneByCode(code);
    }

}
