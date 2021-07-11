package com.pengsoft.basedata.api;

import com.pengsoft.basedata.domain.SupplierConsumer;
import com.pengsoft.basedata.facade.SupplierConsumerFacade;
import com.pengsoft.support.api.EntityApi;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link SupplierConsumer}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping("api/supplier-consumer")
public class SupplierConsumerApi extends EntityApi<SupplierConsumerFacade, SupplierConsumer, String> {

    @PostMapping("save-supplier")
    public SupplierConsumer saveSupplier(@RequestBody final SupplierConsumer supplierConsumer) {
        return getService().saveSupplier(supplierConsumer);
    }

    @PostMapping("save-consumer")
    public SupplierConsumer saveConsumer(@RequestBody final SupplierConsumer supplierConsumer) {
        return getService().saveConsumer(supplierConsumer);
    }

}
