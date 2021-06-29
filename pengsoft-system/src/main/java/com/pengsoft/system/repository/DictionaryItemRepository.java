package com.pengsoft.system.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.pengsoft.support.repository.TreeEntityRepository;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.system.domain.DictionaryType;
import com.pengsoft.system.domain.QDictionaryItem;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link DictionaryItem} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface DictionaryItemRepository extends TreeEntityRepository<QDictionaryItem, DictionaryItem, String> {

    @Override
    default void customize(final QuerydslBindings bindings, final QDictionaryItem root) {
        TreeEntityRepository.super.customize(bindings, root);
        bindings.bind(root.code).first(StringPath::contains);
        bindings.bind(root.name).first(StringPath::contains);
    }

    /**
     * Returns a collection of a {@link DictionaryItem} with given code.
     *
     * @param code {@link DictionaryType}'s code
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<DictionaryItem> findAllByTypeCode(@NotBlank String code);

    /**
     * Returns an {@link Optional} of a {@link DictionaryItem} with given
     * {@linkplain DictionaryType type}, {@linkplain DictionaryItem parent} and
     * code.
     *
     * @param typeId {@link DictionaryType} The type id.
     * @param parent The parent {@link DictionaryItem} The parent type item.
     * @param code   {@link DictionaryType}'s code
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<DictionaryItem> findOneByTypeIdAndParentAndCode(@NotNull String typeId, DictionaryItem parent,
            @NotBlank String code);

}
