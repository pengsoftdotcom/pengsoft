package com.pengsoft.basedata.facade;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.QSupplierConsumer;
import com.pengsoft.basedata.domain.SupplierConsumer;
import com.pengsoft.basedata.service.OrganizationService;
import com.pengsoft.basedata.service.SupplierConsumerService;
import com.pengsoft.support.facade.EntityFacadeImpl;
import com.pengsoft.support.util.EntityUtils;

import org.springframework.stereotype.Service;

/**
 * The implementer of {@link SupplierConsumerFacade}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Service
public class SupplierConsumerFacadeImpl extends EntityFacadeImpl<SupplierConsumerService, SupplierConsumer, String>
        implements SupplierConsumerFacade {

    @Inject
    private OrganizationService organizationService;

    @Override
    public SupplierConsumer save(SupplierConsumer target) {
        if (EntityUtils.eq(target.getSupplier(), target.getConsumer())) {
            throw new IllegalArgumentException("The supplier and consumer cannot be the same.");
        }
        final var root = QSupplierConsumer.supplierConsumer;
        final var source = findOne(root.supplier.eq(target.getSupplier()).and(root.supplier.eq(target.getConsumer())));
        if (source.isPresent()) {
            return source.get();
        } else {
            return super.save(target);
        }
    }

    @Override
    public SupplierConsumer saveSupplier(SupplierConsumer supplierConsumer) {
        organizationService.save(supplierConsumer.getSupplier());
        return super.save(supplierConsumer);
    }

    @Override
    public SupplierConsumer saveConsumer(SupplierConsumer supplierConsumer) {
        organizationService.save(supplierConsumer.getConsumer());
        return super.save(supplierConsumer);
    }

}
