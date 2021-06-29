package com.pengsoft.system.service;

import java.util.Optional;

import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.system.domain.DictionaryType;
import com.pengsoft.system.repository.DictionaryTypeRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link DictionaryTypeService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class DictionaryTypeServiceImpl extends EntityServiceImpl<DictionaryTypeRepository, DictionaryType, String>
        implements DictionaryTypeService {

    @Override
    public DictionaryType save(final DictionaryType type) {
        findOneByCode(type.getCode()).ifPresent(source -> {
            if (EntityUtils.ne(source, type)) {
                throw getExceptions().constraintViolated("code", type.getCode());
            }
        });
        return super.save(type);
    }

    @Override
    public Optional<DictionaryType> findOneByCode(final String code) {
        return getRepository().findOneByCode(code);
    }

}
