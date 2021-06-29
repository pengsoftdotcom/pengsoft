package com.pengsoft.basedata.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.QPerson;
import com.pengsoft.security.repository.OwnedRepository;
import com.pengsoft.support.repository.EntityRepository;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link Person} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface PersonRepository extends EntityRepository<QPerson, Person, String>, OwnedRepository {

    @Override
    default void customize(final QuerydslBindings bindings, final QPerson root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.name).first(StringPath::contains);
        bindings.bind(root.nickname).first(StringPath::contains);
        bindings.bind(root.mobile).first(StringPath::contains);
    }

    /**
     * Returns an {@link Optional} of a {@link Person} with given mobile.
     *
     * @param mobile {@link Person}'s mobile
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Person> findOneByMobile(@NotBlank String mobile);

    /**
     * Returns an {@link Optional} of a {@link Person} with given user id.
     *
     * @param userId The id of {@link Person}'s user.
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Person> findOneByUserId(@NotBlank String userId);

}
