package com.pengsoft.security;

import java.util.List;

import javax.inject.Inject;

import com.pengsoft.security.domain.Authority;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.domain.User;
import com.pengsoft.security.facade.AuthorityFacade;
import com.pengsoft.security.service.RoleService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security" })
class SecurityModuleInitializer {

    @Inject
    RoleService service;
    @Inject
    AuthorityFacade facade;

    @Test
    void initRolesAndAuthorities() {
        List.of(User.class, Role.class, Authority.class).forEach(entityClass -> {
            service.saveEntityAdmin(entityClass);
            facade.saveEntityAdminAuthorities(entityClass);
        });
    }

}
