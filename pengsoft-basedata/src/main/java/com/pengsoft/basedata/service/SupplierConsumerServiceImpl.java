package com.pengsoft.basedata.service;

import com.pengsoft.basedata.domain.SupplierConsumer;
import com.pengsoft.basedata.repository.SupplierConsumerRepository;
import com.pengsoft.support.service.EntityServiceImpl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link SupplierConsumerService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class SupplierConsumerServiceImpl extends EntityServiceImpl<SupplierConsumerRepository, SupplierConsumer, String>
        implements SupplierConsumerService {

}
