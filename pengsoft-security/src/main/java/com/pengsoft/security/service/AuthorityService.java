package com.pengsoft.security.service;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import com.pengsoft.security.domain.Authority;
import com.pengsoft.support.service.EntityService;

/**
 * The service interface of {@link Authority}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface AuthorityService extends EntityService<Authority, String> {

    /**
     * Returns an {@link Optional} of a {@link Authority} with given code.
     *
     * @param code {@link Authority}'s code
     */
    Optional<Authority> findOneByCode(@NotBlank String code);

}
