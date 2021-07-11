package com.pengsoft.basedata.facade;

import com.pengsoft.basedata.domain.SupplierConsumer;
import com.pengsoft.basedata.service.SupplierConsumerService;
import com.pengsoft.support.facade.EntityFacade;

/**
 * The facade interface of {@link SupplierConsumer}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface SupplierConsumerFacade
        extends EntityFacade<SupplierConsumerService, SupplierConsumer, String>, SupplierConsumerService {

    /**
     * 保存供应商
     * 
     * @param supplierConsumer 供应商客户
     */
    SupplierConsumer saveSupplier(SupplierConsumer supplierConsumer);

    /**
     * 保存客户
     * 
     * @param supplierConsumer 供应商客户
     */
    SupplierConsumer saveConsumer(SupplierConsumer supplierConsumer);

}
