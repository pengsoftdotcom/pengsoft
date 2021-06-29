package com.pengsoft.security.service;

import javax.validation.constraints.NotNull;

import com.pengsoft.security.domain.DefaultUserDetails;
import com.pengsoft.security.domain.Role;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;

/**
 * The default {@link UserDetailsService}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Validated
public interface DefaultUserDetailsService extends UserDetailsService {

    /**
     * Set the primary role.
     *
     * @param role The primary role.
     * @return {@link DefaultUserDetails}
     */
    DefaultUserDetails setPrimaryRole(@NotNull Role role);

}
