package com.pengsoft.basedata.api;

import java.util.List;

import com.pengsoft.basedata.domain.SupplierConsumer;
import com.pengsoft.basedata.facade.SupplierConsumerFacade;
import com.pengsoft.security.annotation.Authorized;
import com.pengsoft.support.api.EntityApi;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Authorized
    @Override
    public SupplierConsumer findOne(@RequestParam(value = "id", required = false) SupplierConsumer entity) {
        return super.findOne(entity);
    }

    @Authorized
    @Override
    public Page<SupplierConsumer> findPage(Predicate predicate, Pageable pageable) {
        return super.findPage(predicate, pageable);
    }

    @Authorized
    @Override
    public List<SupplierConsumer> findAll(Predicate predicate, Sort sort) {
        return super.findAll(predicate, sort);
    }

}
