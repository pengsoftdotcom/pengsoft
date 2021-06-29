package com.pengsoft.system.repository;

import com.pengsoft.support.repository.EntityRepository;
import com.pengsoft.system.domain.Message;
import com.pengsoft.system.domain.QMessage;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link Message} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface MessageRepository extends EntityRepository<QMessage, Message, String> {

    @Override
    default void customize(final QuerydslBindings bindings, final QMessage root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.subject).first(StringPath::contains);
    }

}
