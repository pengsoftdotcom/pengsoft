package com.pengsoft.basedata.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.QOrganization;
import com.pengsoft.support.repository.TreeEntityRepository;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * update the admin of the organization.
     * 
     * @param organization
     * @param admin
     * @param createdBy
     * @param updatedBy
     * @param controlledBy
     * @param belongsTo
     */
    @Modifying
    @Query("update Organization o set o.admin = ?2, o.createdBy = ?3, o.updatedBy = ?4, o.updatedAt = now(), o.controlledBy = ?5, o.belongsTo = ?6 where o = ?1")
    void setAdmin(@NotNull Organization organization, Person admin, String createdBy, String updatedBy,
            String controlledBy, String belongsTo);

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
     * Returns all organizations with given admin.
     *
     * @param admin The {@link Organization}'s admin
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<Organization> findAllByAdmin(@NotNull Person admin);

    /**
     * Returns the number of organizations with given admin id.
     *
     * @param adminId The {@link Organization}'s admin
     */
    // @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value =
    // "true"), forCounting = false)
    long countByAdmin(@NotNull Person admin);

    /**
     * 返回可选的客户分页数据
     * 
     * @param supplier 供应商
     * @param pageable 分页参数
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    @Query("from Organization o where o.id not in (select sc.consumer.id from SupplierConsumer sc where sc.supplier = ?1) and o.id != ?1")
    Page<Organization> findPageOfAvailableConsumers(Organization supplier, Pageable pageable);

    /**
     * 返回所有可选的客户
     * 
     * @param supplier 供应商
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    @Query("from Organization o where o.id not in (select sc.consumer.id from SupplierConsumer sc where sc.supplier = ?1) and o.id != ?1")
    List<Organization> findAllAvailableConsumers(Organization supplier);

    /**
     * 返回可选的供应商分页数据
     * 
     * @param consumer 客户
     * @param pageable 分页参数
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    @Query("from Organization o where o.id not in (select sc.supplier.id from SupplierConsumer sc where sc.consumer = ?1) and o.id != ?1")
    Page<Organization> findPageOfAvailableSuppliers(Organization consumer, Pageable pageable);

    /**
     * 返回所有可选的供应商
     * 
     * @param consumer 客户
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    @Query("from Organization o where o.id not in (select sc.supplier.id from SupplierConsumer sc where sc.consumer = ?1) and o.id != ?1")
    List<Organization> findAllAvailableSuppliers(Organization consumer);

}
