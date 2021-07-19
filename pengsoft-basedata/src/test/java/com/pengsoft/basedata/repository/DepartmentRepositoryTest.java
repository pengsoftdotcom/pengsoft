package com.pengsoft.basedata.repository;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
class DepartmentRepositoryTest {

    @Inject
    DepartmentRepository repository;

    @Inject
    OrganizationRepository organizationRepository;

    @Test
    void findOneByOrganizationAndParentAndName() {
        organizationRepository.findOneByCode("001").ifPresent(
                organization -> repository.findOneByOrganizationAndParentAndName(organization, null, "总经理办公室"));
    }

}
