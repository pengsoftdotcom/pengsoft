package com.pengsoft.basedata.repository;

import com.pengsoft.basedata.domain.QSupplierConsumer;
import com.pengsoft.basedata.domain.SupplierConsumer;
import com.pengsoft.support.repository.EntityRepository;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link SupplierConsumer} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface SupplierConsumerRepository
        extends EntityRepository<QSupplierConsumer, SupplierConsumer, String>, OwnedExtRepository {

    @Override
    default void customize(final QuerydslBindings bindings, final QSupplierConsumer root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.supplier.code).first(StringPath::contains);
        bindings.bind(root.supplier.name).first(StringPath::contains);
        bindings.bind(root.supplier.admin.name).first(StringPath::contains);
        bindings.bind(root.supplier.admin.nickname).first(StringPath::contains);
        bindings.bind(root.supplier.admin.mobile).first(StringPath::contains);
        bindings.bind(root.consumer.code).first(StringPath::contains);
        bindings.bind(root.consumer.name).first(StringPath::contains);
        bindings.bind(root.consumer.admin.name).first(StringPath::contains);
        bindings.bind(root.consumer.admin.nickname).first(StringPath::contains);
        bindings.bind(root.consumer.admin.mobile).first(StringPath::contains);
    }

}
