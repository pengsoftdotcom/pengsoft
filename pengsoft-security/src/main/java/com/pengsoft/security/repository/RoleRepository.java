package com.pengsoft.security.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.security.domain.QRole;
import com.pengsoft.security.domain.Role;
import com.pengsoft.support.repository.TreeEntityRepository;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link Role} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface RoleRepository extends TreeEntityRepository<QRole, Role, String> {

    @Override
    default void customize(final QuerydslBindings bindings, final QRole root) {
        TreeEntityRepository.super.customize(bindings, root);
        bindings.bind(root.code).first(StringPath::contains);
    }

    /**
     * Returns an {@link Optional} of a {@link Role} with given code.
     *
     * @param code {@link Role}'s code
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Role> findOneByCode(@NotBlank String code);

}
