package com.pengsoft.security.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.security.domain.Authority;
import com.pengsoft.security.domain.QAuthority;
import com.pengsoft.support.repository.EntityRepository;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link Authority} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface AuthorityRepository extends EntityRepository<QAuthority, Authority, String> {

    @Override
    default void customize(final QuerydslBindings bindings, final QAuthority root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.code).first(StringPath::contains);
    }

    /**
     * Returns an {@link Optional} of a {@link Authority} with given code.
     *
     * @param code {@link Authority}'s code
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Authority> findOneByCode(@NotBlank String code);

}
