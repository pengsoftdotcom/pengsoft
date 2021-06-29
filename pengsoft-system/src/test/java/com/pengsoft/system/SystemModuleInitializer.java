package com.pengsoft.system;

import java.util.List;

import javax.inject.Inject;

import com.pengsoft.security.facade.AuthorityFacade;
import com.pengsoft.security.service.RoleService;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.domain.Captcha;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.system.domain.DictionaryType;
import com.pengsoft.system.domain.Region;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system" })
public class SystemModuleInitializer {

    @Inject
    private RoleService service;

    @Inject
    private AuthorityFacade facade;

    @Test
    void initRolesAndAuthorities() {
        List.of(Asset.class, Captcha.class, DictionaryType.class, DictionaryItem.class, Region.class)
                .forEach(entityClass -> {
                    service.saveEntityAdmin(entityClass);
                    facade.saveEntityAdminAuthorities(entityClass);
                });
    }

}
