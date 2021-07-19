package com.pengsoft.security.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.pengsoft.security.domain.Authority;
import com.pengsoft.security.domain.Role;
import com.pengsoft.support.domain.Entity;
import com.pengsoft.support.service.TreeEntityService;

/**
 * The service interface of {@link Role}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface RoleService extends TreeEntityService<Role, String> {

    /**
     * Save the entity admin by the given entity class
     *
     * @param entityClass The entity class
     * @return The entity admin.
     */
    Role saveEntityAdmin(Class<? extends Entity<? extends Serializable>> entityClass);

    /**
     * Copy the authorities of source role to the target role.
     * 
     * @param source The {@link Role} that will be copied.
     * @param target The {@link Role} that will be copy to.
     */
    void copyAuthorities(@NotNull Role source, @NotNull Role target);

    /**
     * Grant authorities.
     *
     * @param role        The {@link Role}
     * @param authorities The authorities to be granted.
     */
    void grantAuthorities(@NotNull Role role, List<Authority> authorities);

    /**
     * Returns an {@link Optional} of a {@link Role} with given code.
     *
     * @param code {@link Authority}'s code
     */
    Optional<Role> findOneByCode(@NotBlank String code);

}
