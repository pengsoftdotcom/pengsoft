package com.pengsoft.basedata.facade;

import javax.inject.Inject;

import com.pengsoft.basedata.service.PersonService;

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
    private PersonService service;

    @Test
    @WithUserDetails("admin")
    void save() {
        facade.findOneByCode("001")
                .ifPresent(organization -> service.findOneByMobile("18508101366").ifPresent(admin -> {
                    organization.setAdmin(admin);
                    facade.save(organization);
                }));
    }

    @Test
    void delete() {
        facade.delete(facade.findAll(null, null));
    }

}
