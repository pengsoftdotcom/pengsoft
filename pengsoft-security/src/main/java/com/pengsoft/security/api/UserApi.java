package com.pengsoft.security.api;

import java.util.List;
import java.util.Optional;

import com.pengsoft.security.annotation.AuthorityChanged;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.domain.User;
import com.pengsoft.security.domain.UserRole;
import com.pengsoft.security.service.UserService;
import com.pengsoft.support.api.EntityApi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link User}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping("api/user")
public class UserApi extends EntityApi<UserService, User, String> {

    @PostMapping("reset-password")
    public void resetPassword(final String id, final String password) {
        getService().resetPassword(id, password);
    }

    @AuthorityChanged
    @PostMapping("grant-roles")
    public void grantRoles(@RequestParam("user.id") final User user,
            @RequestParam(value = "role.id", defaultValue = "") final List<Role> roles) {
        getService().grantRoles(user, roles);
    }

    @AuthorityChanged
    @PostMapping("set-primary-role")
    public void setPrimaryRole(@RequestParam("user.id") final User user, @RequestParam("role.id") final Role role) {
        getService().setPrimaryRole(user, role);
    }

    @GetMapping("find-all-user-roles-by-user")
    public List<UserRole> findAllUserRolesByUser(@RequestParam("id") final User user) {
        return Optional.ofNullable(user).map(User::getUserRoles).orElse(List.of());
    }

}
