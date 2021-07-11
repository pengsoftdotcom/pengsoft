package com.pengsoft.basedata.api;

import java.util.List;

import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.facade.OrganizationFacade;
import com.pengsoft.support.api.EntityApi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("find-page-of-available-consumers")
    public Page<Organization> findPageOfAvailableConsumers(@RequestParam("supplier.id") Organization supplier,
            Pageable pageable) {
        return getService().findPageOfAvailableConsumers(supplier, pageable);
    }

    @GetMapping("find-all-available-consumers")
    public List<Organization> findAllAvailableConsumers(@RequestParam("supplier.id") Organization supplier) {
        return getService().findAllAvailableConsumers(supplier);
    }

    @GetMapping("find-page-of-available-suppliers")
    public Page<Organization> findPageOfAvailableSuppliers(@RequestParam("consumer.id") Organization consumer,
            Pageable pageable) {
        return getService().findPageOfAvailableSuppliers(consumer, pageable);
    }

    @GetMapping("find-all-available-suppliers")
    public List<Organization> findAllAvailableSuppliers(@RequestParam("consumer.id") Organization consumer) {
        return getService().findAllAvailableSuppliers(consumer);
    }

}
