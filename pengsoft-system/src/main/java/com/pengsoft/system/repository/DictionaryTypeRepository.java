package com.pengsoft.system.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.support.repository.EntityRepository;
import com.pengsoft.system.domain.DictionaryType;
import com.pengsoft.system.domain.QDictionaryType;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link DictionaryType} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface DictionaryTypeRepository extends EntityRepository<QDictionaryType, DictionaryType, String> {

    @Override
    default void customize(final QuerydslBindings bindings, final QDictionaryType root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.code).first(StringPath::contains);
        bindings.bind(root.name).first(StringPath::contains);
    }

    /**
     * Returns an {@link Optional} of a {@link DictionaryType} with given code.
     *
     * @param code {@link DictionaryType}'code
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<DictionaryType> findOneByCode(@NotBlank String code);

}
