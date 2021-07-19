package com.pengsoft.basedata.repository;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
class PostRepositoryTest {

    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    PostRepository postRepository;

    @Test
    void findallby() {
        organizationRepository.findOneByCode("001").ifPresent(
                organization -> postRepository.findOneByOrganizationAndParentAndName(organization, null, null));
    }

}
