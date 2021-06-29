package com.pengsoft.system.service;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.pengsoft.support.service.TreeEntityService;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.system.domain.DictionaryType;

/**
 * The service interface of {@link DictionaryItem}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface DictionaryItemService extends TreeEntityService<DictionaryItem, String> {

    /**
     * Returns a collection of {@link DictionaryItem} with given code.
     *
     * @param code {@link DictionaryType}'s code
     */
    List<DictionaryItem> findAllByTypeCode(@NotBlank String code);

    /**
     * Returns an {@link Optional} of a {@link DictionaryItem} with given
     * {@linkplain DictionaryType type}, {@linkplain DictionaryItem parent} and
     * code.
     *
     * @param type   {@link DictionaryType}
     * @param parent The parent {@link DictionaryItem}
     * @param code   {@link DictionaryType}'s code
     */
    Optional<DictionaryItem> findOneByTypeAndParentAndCode(@NotNull DictionaryType type, DictionaryItem parent,
            @NotBlank String code);

}
