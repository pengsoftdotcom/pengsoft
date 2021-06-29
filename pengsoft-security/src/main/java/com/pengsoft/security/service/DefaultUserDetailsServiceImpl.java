package com.pengsoft.security.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.pengsoft.security.domain.DefaultGrantedAuthority;
import com.pengsoft.security.domain.DefaultUserDetails;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.domain.RoleAuthority;
import com.pengsoft.security.domain.User;
import com.pengsoft.security.domain.UserRole;
import com.pengsoft.security.util.SecurityUtils;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link DefaultUserDetailsService}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Service
public class DefaultUserDetailsServiceImpl implements DefaultUserDetailsService {

    @Lazy
    @Inject
    private UserService userService;

    protected List<GrantedAuthority> getAllAuthorities(final Role role) {
        final var roles = new ArrayList<Role>();
        roles.add(role);
        final var deque = new ArrayDeque<Role>();
        role.getChildren().forEach(root -> {
            deque.push(root);
            while (!deque.isEmpty()) {
                final var parent = deque.pop();
                roles.add(parent);
                parent.getChildren().forEach(deque::push);
            }
        });
        return roles.stream().map(Role::getRoleAuthorities).flatMap(List::stream).map(RoleAuthority::getAuthority)
                .map(DefaultGrantedAuthority::new).distinct().collect(Collectors.toList());
    }

    @Override
    public DefaultUserDetails setPrimaryRole(final Role role) {
        final var userDetails = SecurityUtils.getUserDetails();
        userDetails.setPrimaryRole(role);
        userService.findOne(SecurityUtils.getUserId()).ifPresent(userDetails::setUser);
        userService.setPrimaryRole(userDetails.getUser(), role);
        userDetails.setAuthorities(getAllAuthorities(role));
        return userDetails;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) {
        var optional = userService.findOneByUsername(username);
        if (optional.isPresent()) {
            return buildUserDetails(optional.get());
        } else {
            optional = userService.findOneByMobile(username);
        }

        if (optional.isPresent()) {
            return buildUserDetails(optional.get());
        } else {
            optional = userService.findOneByEmail(username);
        }

        if (optional.isPresent()) {
            return buildUserDetails(optional.get());
        } else {
            optional = userService.findOneByMpOpenId(username);
        }

        if (optional.isPresent()) {
            return buildUserDetails(optional.get());
        } else {
            throw new UsernameNotFoundException("'" + username + "' not found");
        }
    }

    private UserDetails buildUserDetails(final User user) {
        final var roles = user.getUserRoles().stream().map(UserRole::getRole).collect(Collectors.toList());
        return user.getUserRoles().stream().filter(UserRole::isPrimary).map(UserRole::getRole).findFirst()
                .map(role -> new DefaultUserDetails(user, roles, role, getAllAuthorities(role)))
                .orElse(new DefaultUserDetails(user, roles));
    }

}
