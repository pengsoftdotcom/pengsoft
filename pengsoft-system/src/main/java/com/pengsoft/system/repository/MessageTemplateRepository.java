package com.pengsoft.system.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.support.repository.EntityRepository;
import com.pengsoft.system.domain.MessageTemplate;
import com.pengsoft.system.domain.QMessageTemplate;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link MessageTemplate} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface MessageTemplateRepository extends EntityRepository<QMessageTemplate, MessageTemplate, String> {

    @Override
    default void customize(final QuerydslBindings bindings, final QMessageTemplate root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.code).first(StringPath::contains);
        bindings.bind(root.subject).first(StringPath::contains);
    }

    /**
     * Returns an {@link Optional} of a {@link MessageTemplate} with given code.
     *
     * @param code {@link MessageTemplate}'code
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<MessageTemplate> findOneByCode(@NotBlank String code);

}
