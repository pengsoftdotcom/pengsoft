package com.pengsoft.basedata.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.QOrganization;
import com.pengsoft.support.repository.TreeEntityRepository;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link Organization} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface OrganizationRepository
        extends TreeEntityRepository<QOrganization, Organization, String>, OwnedExtRepository {

    @Override
    default void customize(final QuerydslBindings bindings, final QOrganization root) {
        TreeEntityRepository.super.customize(bindings, root);
        bindings.bind(root.code).first(StringPath::contains);
        bindings.bind(root.name).first(StringPath::contains);
        bindings.bind(root.admin.name).first(StringPath::contains);
        bindings.bind(root.admin.nickname).first(StringPath::contains);
        bindings.bind(root.admin.mobile).first(StringPath::contains);
    }

    /**
     * update the organization's belongsTo property.
     *
     * @param belongsTo The organization's belongsTo
     */
    @Modifying
    @Query("update Organization set belongsTo = ?1 where id = ?1")
    void updateBelongsTo(String belongsTo);

    /**
     * Returns an {@link Optional} of a {@link Organization} with given code.
     *
     * @param code {@link Organization}'s code
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Organization> findOneByCode(@NotBlank String code);

    /**
     * Returns an {@link Optional} of a {@link Organization} with given name.
     *
     * @param name {@link Organization}'s name
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Organization> findOneByName(@NotBlank String name);

    /**
     * Returns all organizations with given admin id.
     *
     * @param adminId The id of {@link Organization}'s admin
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<Organization> findAllByAdminId(@NotBlank String adminId);

}
