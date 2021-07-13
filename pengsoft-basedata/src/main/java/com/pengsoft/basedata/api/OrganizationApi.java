package com.pengsoft.basedata.api;

import java.util.List;

import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.facade.OrganizationFacade;
import com.pengsoft.security.annotation.Authorized;
import com.pengsoft.support.api.EntityApi;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link Organization}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping("api/organization")
public class OrganizationApi extends EntityApi<OrganizationFacade, Organization, String> {

    @Authorized
    @GetMapping("find-page-of-available-consumers")
    public Page<Organization> findPageOfAvailableConsumers(@RequestParam("supplier.id") Organization supplier,
            Pageable pageable) {
        return getService().findPageOfAvailableConsumers(supplier, pageable);
    }

    @Authorized
    @GetMapping("find-all-available-consumers")
    public List<Organization> findAllAvailableConsumers(@RequestParam("supplier.id") Organization supplier) {
        return getService().findAllAvailableConsumers(supplier);
    }

    @Authorized
    @GetMapping("find-page-of-available-suppliers")
    public Page<Organization> findPageOfAvailableSuppliers(@RequestParam("consumer.id") Organization consumer,
            Pageable pageable) {
        return getService().findPageOfAvailableSuppliers(consumer, pageable);
    }

    @GetMapping("find-all-available-suppliers")
    public List<Organization> findAllAvailableSuppliers(@RequestParam("consumer.id") Organization consumer) {
        return getService().findAllAvailableSuppliers(consumer);
    }

    @Authorized
    @Override
    public Organization findOne(@RequestParam(value = "id", required = false) Organization entity) {
        return super.findOne(entity);
    }

    @Authorized
    @Override
    public Page<Organization> findPage(final Predicate predicate, final Pageable pageable) {
        return super.findPage(predicate, pageable);
    }

    @Authorized
    @Override
    public List<Organization> findAll(final Predicate predicate, final Sort sort) {
        return super.findAll(predicate, sort);
    }

}
