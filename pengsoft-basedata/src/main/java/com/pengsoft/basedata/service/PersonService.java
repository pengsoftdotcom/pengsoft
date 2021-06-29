package com.pengsoft.basedata.service;

import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Person;
import com.pengsoft.security.domain.User;
import com.pengsoft.support.service.EntityService;

/**
 * The service interface of {@link Person}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface PersonService extends EntityService<Person, String> {

    /**
     * Returns an {@link Optional} of a {@link Person} with given mobile.
     *
     * @param mobile {@link Person}'s mobile
     */
    Optional<Person> findOneByMobile(@NotBlank String mobile);

    /**
     * Returns an {@link Optional} of a {@link Person} with given user.
     *
     * @param user {@link Person}'s user.
     */
    Optional<Person> findOneByUser(@NotNull User user);

}
