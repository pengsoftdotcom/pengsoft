package com.pengsoft.system.api;

import com.pengsoft.support.api.EntityApi;
import com.pengsoft.system.domain.DictionaryType;
import com.pengsoft.system.service.DictionaryTypeService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link DictionaryType}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping("api/dictionary-type")
public class DictionaryTypeApi extends EntityApi<DictionaryTypeService, DictionaryType, String> {

}
