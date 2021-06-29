package com.pengsoft.system.service;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import com.pengsoft.support.service.EntityService;
import com.pengsoft.system.domain.DictionaryType;

/**
 * The service interface of {@link DictionaryType}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface DictionaryTypeService extends EntityService<DictionaryType, String> {

    /**
     * Returns an {@link Optional} of a {@link DictionaryType} with given code.
     *
     * @param code {@link DictionaryType}'s code
     */
    Optional<DictionaryType> findOneByCode(@NotBlank String code);

}
