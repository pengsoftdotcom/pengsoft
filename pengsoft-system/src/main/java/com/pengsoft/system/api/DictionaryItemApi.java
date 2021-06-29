package com.pengsoft.system.api;

import java.util.List;

import com.pengsoft.support.api.TreeEntityApi;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.system.service.DictionaryItemService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link DictionaryItem}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping("api/dictionary-item")
public class DictionaryItemApi extends TreeEntityApi<DictionaryItemService, DictionaryItem, String> {

    @GetMapping("find-all-by-type-code")
    public List<DictionaryItem> findAllByTypeCode(final String code) {
        return getService().findAllByTypeCode(code);
    }

}
