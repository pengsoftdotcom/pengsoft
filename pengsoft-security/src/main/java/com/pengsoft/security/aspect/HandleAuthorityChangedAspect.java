package com.pengsoft.security.aspect;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.security.annotation.AuthorityChanged;
import com.pengsoft.security.config.properties.OAuthConfigurationProperties;
import com.pengsoft.security.domain.Authority;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.domain.RoleAuthority;
import com.pengsoft.security.domain.User;
import com.pengsoft.security.domain.UserRole;
import com.pengsoft.security.repository.RoleAuthorityRepository;
import com.pengsoft.security.repository.UserRoleRepository;
import com.querydsl.core.types.Predicate;

import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 处理权限变更切面
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
@Aspect
public class HandleAuthorityChangedAspect {

    @Lazy
    @Inject
    private TokenStore tokenStore;

    @Lazy
    @Inject
    private OAuthConfigurationProperties properties;

    @Inject
    private UserRoleRepository userRoleRepository;

    @Inject
    private RoleAuthorityRepository roleAuthorityRepository;

    @AfterReturning(pointcut = "@annotation(authorityChanged)", returning = "result")
    public void handle(final JoinPoint jp, final AuthorityChanged authorityChanged, final Object result) {
        if (result != null) {
            final var userDetails = (UserDetails) result;
            properties.getClients().forEach(client -> tokenStore
                    .findTokensByClientIdAndUserName(client.getId(), userDetails.getUsername()).forEach(token -> {
                        final var authentication = tokenStore.readAuthentication(token);
                        final var userAuthentication = new RememberMeAuthenticationToken(userDetails.getUsername(),
                                userDetails, userDetails.getAuthorities());
                        tokenStore.storeAccessToken(token,
                                new OAuth2Authentication(authentication.getOAuth2Request(), userAuthentication));
                    }));
        } else {
            final var args = jp.getArgs();
            final var arg = args[0];
            if (arg instanceof Predicate) {
                properties.getClients().forEach(client -> tokenStore.findTokensByClientId(client.getId())
                        .forEach(tokenStore::removeAccessToken));
            } else {
                final var users = new ArrayList<User>();
                if (arg instanceof User) {
                    users.add((User) arg);
                } else if (arg instanceof Role) {
                    final var roles = new ArrayList<Role>();
                    addAllRoles(roles, (Role) arg);
                    CollectionUtils.addAll(users, getAllUsersByRoles(roles));
                } else if (arg instanceof Authority) {
                    final var authority = (Authority) arg;
                    final var roles = new ArrayList<Role>();
                    roleAuthorityRepository.findAllByAuthorityIn(authority).stream().map(RoleAuthority::getRole)
                            .forEach(role -> addAllRoles(roles, role));
                    CollectionUtils.addAll(users, getAllUsersByRoles(roles));
                }
                users.forEach(
                        user -> properties.getClients()
                                .forEach(client -> tokenStore
                                        .findTokensByClientIdAndUserName(client.getId(), user.getUsername())
                                        .forEach(tokenStore::removeAccessToken)));
            }
        }
    }

    private List<User> getAllUsersByRoles(List<Role> roles) {
        final var userRoles = userRoleRepository.findAllByRoleIn(roles.toArray(Role[]::new));
        return userRoles.stream().map(UserRole::getUser).collect(Collectors.toList());
    }

    private void addAllRoles(List<Role> roles, Role role) {
        while (role != null) {
            roles.add(role);
            role = role.getParent();
        }
    }

}
