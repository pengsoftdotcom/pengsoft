package com.pengsoft.security.api;

import java.util.List;
import java.util.Optional;

import com.pengsoft.security.annotation.AuthorityChanged;
import com.pengsoft.security.domain.Authority;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.domain.RoleAuthority;
import com.pengsoft.security.service.RoleService;
import com.pengsoft.support.api.TreeEntityApi;
import com.querydsl.core.types.Predicate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link Role}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping("api/role")
public class RoleApi extends TreeEntityApi<RoleService, Role, String> {

    @AuthorityChanged
    @PostMapping("grant-authorities")
    public void grantAuthorities(@RequestParam("role.id") final Role role,
            @RequestParam(value = "authority.id", defaultValue = "") final List<Authority> authorities) {
        getService().grantAuthorities(role, authorities);
    }

    @AuthorityChanged
    @Override
    public void save(@RequestBody Role entity) {
        super.save(entity);
    }

    @AuthorityChanged
    @Override
    public void delete(Predicate predicate) {
        super.delete(predicate);
    }

    @GetMapping("find-all-role-authorities-by-role")
    public List<RoleAuthority> findAllRoleAuthoritiesByRole(@RequestParam("id") final Role role) {
        return Optional.ofNullable(role).map(Role::getRoleAuthorities).orElse(List.of());
    }

}
