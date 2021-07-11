package com.pengsoft.security.api;

import javax.inject.Inject;

import com.pengsoft.security.annotation.AuthorityChanged;
import com.pengsoft.security.annotation.Authorized;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.domain.User;
import com.pengsoft.security.service.DefaultUserDetailsService;
import com.pengsoft.security.service.UserService;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.exception.Exceptions;

import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link UserDetails}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Authorized
@RestController
@RequestMapping("api/user-details")
public class UserDetailsApi {

    @Inject
    private Exceptions exceptions;

    @Inject
    private UserService userService;

    @Inject
    private DefaultUserDetailsService service;

    @GetMapping("current")
    public UserDetails current() {
        return SecurityUtils.getUserDetails();
    }

    @AuthorityChanged
    @PostMapping("set-primary-role")
    public UserDetails setPrimaryRole(@RequestParam("id") final Role role) {
        return service.setPrimaryRole(role);
    }

    @PostMapping("change-password")
    public void changePassword(final String oldPassword, final String newPassword) {
        userService.changePassword(SecurityUtils.getUserId(), oldPassword, newPassword);
    }

    @PostMapping("reset-password")
    public void resetPassword(final String username, final String password) {
        var user = userService.findOneByMobile(username).orElseThrow(() -> exceptions.entityNotExists(username));
        userService.resetPassword(user.getId(), password);
    }

    @AuthorityChanged
    @PostMapping("sign-out")
    public void signOut(@CurrentSecurityContext(expression = "authentication.principal.user") User user) {

    }

}
