package com.pengsoft.security.facade;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;

import com.pengsoft.security.annotation.Authorized;
import com.pengsoft.security.domain.Authority;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.service.AuthorityService;
import com.pengsoft.security.service.RoleService;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.domain.Enable;
import com.pengsoft.support.domain.Entity;
import com.pengsoft.support.domain.Sortable;
import com.pengsoft.support.exception.MissingConfigurationException;
import com.pengsoft.support.facade.EntityFacadeImpl;
import com.pengsoft.support.util.StringUtils;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The implementer of {@link AuthorityFacade}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Service
public class AuthorityFacadeImpl extends EntityFacadeImpl<AuthorityService, Authority, String>
        implements AuthorityFacade {

    @Inject
    private RoleService roleService;

    @Override
    public void saveEntityAdminAuthorities(final Class<? extends Entity<? extends Serializable>> entityClass) {
        final Class<?> apiClass = getApiClass(entityClass);
        if (!apiClass.isAnnotationPresent(Authorized.class)) {
            final var entityAdmin = getEntityAdmin(entityClass);
            final var authorities = getEntityAuthorities(entityClass, apiClass);
            roleService.grantAuthorities(entityAdmin, authorities);
        }
    }

    private List<Authority> getEntityAuthorities(final Class<? extends Entity<? extends Serializable>> entityClass,
            final Class<?> apiClass) {
        final var authorityCodePrefix = SecurityUtils.getEntityAdminAuthorityCodePrefixFromEntityClass(entityClass)
                + StringUtils.GLOBAL_SEPARATOR;
        final var authorityCodes = new ArrayList<String>();
        authorityCodes.addAll(getAuthorityCodesFromApi(apiClass, RequestMapping.class, authorityCodePrefix));
        authorityCodes.addAll(getAuthorityCodesFromApi(apiClass, GetMapping.class, authorityCodePrefix));
        authorityCodes.addAll(getAuthorityCodesFromApi(apiClass, PostMapping.class, authorityCodePrefix));
        authorityCodes.addAll(getAuthorityCodesFromApi(apiClass, PutMapping.class, authorityCodePrefix));
        authorityCodes.addAll(getAuthorityCodesFromApi(apiClass, DeleteMapping.class, authorityCodePrefix));
        authorityCodes.removeAll(getAuthorityCodesNotNeeded(entityClass, apiClass, authorityCodePrefix));
        return authorityCodes.stream().map(authorityCode -> {
            final var optional = findOneByCode(authorityCode);
            final Authority authority;
            if (optional.isPresent()) {
                authority = optional.get();
            } else {
                authority = save(new Authority(authorityCode));
            }
            return authority;
        }).collect(Collectors.toList());
    }

    private Role getEntityAdmin(final Class<? extends Entity<? extends Serializable>> entityClass) {
        final var entityAdminCode = SecurityUtils.getEntityAdminRoleCode(entityClass);
        return roleService.findOneByCode(entityAdminCode).orElseThrow(
                () -> new MissingConfigurationException("'" + entityClass.getName() + "' entity admin not found"));
    }

    private Class<?> getApiClass(final Class<? extends Entity<? extends Serializable>> entityClass) {
        final Class<?> apiClass;
        try {
            apiClass = Class.forName(RegExUtils.replaceFirst(entityClass.getName(), ".domain.", ".api.") + "Api");
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException("get api class from '" + entityClass.getName() + "' error", e);
        }
        return apiClass;
    }

    private ArrayList<String> getAuthorityCodesNotNeeded(
            final Class<? extends Entity<? extends Serializable>> entityClass, final Class<?> apiClass,
            final String authorityCodePrefix) {
        final var authorityCodes = new ArrayList<String>();
        if (!Sortable.class.isAssignableFrom(entityClass)) {
            authorityCodes.add(authorityCodePrefix + "sort");
        }
        if (!Enable.class.isAssignableFrom(entityClass)) {
            authorityCodes.add(authorityCodePrefix + "enable");
            authorityCodes.add(authorityCodePrefix + "disable");
        }
        authorityCodes.addAll(getAuthorityCodesFromApi(apiClass, Authorized.class, authorityCodePrefix));
        return authorityCodes;
    }

    private List<String> getAuthorityCodesFromApi(final Class<?> apiClass,
            final Class<? extends Annotation> mappingClass, final String authorityCodePrefix) {
        return MethodUtils.getMethodsListWithAnnotation(apiClass, mappingClass, true, true).stream()
                .map(method -> authorityCodePrefix + StringUtils.camelCaseToSnakeCase(method.getName(), false))
                .collect(Collectors.toList());

    }

    @Override
    public Optional<Authority> findOneByCode(@NotBlank final String code) {
        return getService().findOneByCode(code);
    }

}
