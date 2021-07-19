package com.pengsoft.basedata.repository;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
class StaffRepositoryTest {

    @Inject
    StaffRepository staffRepository;

    @Inject
    OrganizationRepository organizationRepository;

    @Test
    void findAllByJobDepartmentOrganization() {
        organizationRepository.findOneByCode("001").ifPresent(organization -> {
            staffRepository.findAllByJobDepartmentOrganization(organization);
        });
    }

}
