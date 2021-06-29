package com.pengsoft.basedata.facade;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.system.service.DictionaryItemService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
public class OrganizationFacadeTest {

    @Inject
    private OrganizationFacade facade;

    @Inject
    private DictionaryItemService service;

    @Test
    @WithUserDetails("admin")
    void save() {
        var organization = new Organization();
        organization.setCode("001");
        organization.setName("重庆鹏软科技有限公司");
        service.findOne("98fdc3e6-567c-48b6-9c82-ffe7f20b6717").ifPresent(organization::setCategory);
        var admin = new Person();
        admin.setName("党鹏");
        admin.setMobile("18508101366");
        organization.setAdmin(admin);
        facade.save(organization);
    }

    @Test
    void delete() {
        facade.delete(facade.findAll(null, null));
    }

}
