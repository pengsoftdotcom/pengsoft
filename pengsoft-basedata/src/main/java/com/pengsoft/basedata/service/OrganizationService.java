package com.pengsoft.basedata.service;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.support.service.TreeEntityService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The service interface of {@link Organization}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface OrganizationService extends TreeEntityService<Organization, String> {

    /**
     * set the admin of the organization.
     * 
     * @param organization
     * @param admin
     */
    void setAdmin(@NotNull Organization organization, Person admin);

    /**
     * Returns an {@link Optional} of a {@link Organization} with given code.
     *
     * @param code {@link Organization}'s code
     */
    Optional<Organization> findOneByCode(@NotBlank String code);

    /**
     * Returns an {@link Optional} of a {@link Organization} with given name.
     *
     * @param name {@link Organization}'s name
     */
    Optional<Organization> findOneByName(String name);

    /**
     * Returns all organizations with given admin.
     *
     * @param admin {@link Organization}'s admin
     */
    List<Organization> findAllByAdmin(@NotNull Person admin);

    /**
     * Returns the number of organizations with given admin id.
     *
     * @param adminId The {@link Organization}'s admin
     */
    long countByAdmin(@NotNull Person admin);

    /**
     * 返回可选的客户分页数据
     * 
     * @param supplier 供应商
     * @param pageable 分页参数
     */
    Page<Organization> findPageOfAvailableConsumers(Organization supplier, Pageable pageable);

    /**
     * 返回所有可选的客户
     * 
     * @param supplier 供应商
     */
    List<Organization> findAllAvailableConsumers(Organization supplier);

    /**
     * 返回可选的供应商分页数据
     * 
     * @param consumer 客户
     * @param pageable 分页参数
     */
    Page<Organization> findPageOfAvailableSuppliers(Organization consumer, Pageable pageable);

    /**
     * 返回所有可选的供应商
     * 
     * @param consumer 客户
     */
    List<Organization> findAllAvailableSuppliers(Organization consumer);

}
