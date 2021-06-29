package com.pengsoft.system.service;

import java.util.List;
import java.util.Optional;

import com.pengsoft.support.service.TreeEntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.system.domain.DictionaryType;
import com.pengsoft.system.repository.DictionaryItemRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link DictionaryItemService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class DictionaryItemServiceImpl extends TreeEntityServiceImpl<DictionaryItemRepository, DictionaryItem, String>
        implements DictionaryItemService {

    @Override
    public DictionaryItem save(final DictionaryItem item) {
        findOneByTypeAndParentAndCode(item.getType(), item.getParent(), item.getCode()).ifPresent(source -> {
            if (EntityUtils.ne(source, item)) {
                throw getExceptions().constraintViolated("code", item.getCode());
            }
        });
        return super.save(item);
    }

    @Override
    public List<DictionaryItem> findAllByTypeCode(final String code) {
        return getRepository().findAllByTypeCode(code);
    }

    @Override
    public Optional<DictionaryItem> findOneByTypeAndParentAndCode(final DictionaryType type,
            final DictionaryItem parent, final String code) {
        return getRepository().findOneByTypeIdAndParentAndCode(type.getId(), parent, code);
    }

}
