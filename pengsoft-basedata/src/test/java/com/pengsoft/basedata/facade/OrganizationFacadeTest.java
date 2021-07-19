package com.pengsoft.basedata.facade;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.system.service.DictionaryItemService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
class OrganizationFacadeTest {

    @Inject
    OrganizationFacade facade;

    @Inject
    DictionaryItemService dictionaryItemService;

    @Test
    @WithUserDetails("admin")
    void save() {
        final var organization = new Organization();
        organization.setCode("001");
        organization.setName("重庆鹏软科技有限公司");
        dictionaryItemService.findOne("98fdc3e6-567c-48b6-9c82-ffe7f20b6717").ifPresent(organization::setCategory);
        facade.save(organization);
    }

    @Test
    @WithUserDetails("admin")
    void setAdmin() {
        facade.findOneByCode("001").ifPresent(organization -> facade.setAdmin(organization, null));
    }

    @Test
    void delete() {
        facade.delete(facade.findAll(null, null));
    }
}
