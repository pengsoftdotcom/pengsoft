package com.pengsoft.basedata.repository;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
class OrganizationRepositoryTest {

    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    PersonRepository personRepository;

    @Test
    void findPageExcludeConsumerBySupplier() {
        organizationRepository.findOneByCode("001")
                .ifPresent(organization -> organizationRepository.findPageOfAvailableConsumers(organization, null));
    }

    @Test
    void findPageExcludeSupplierByConsumer() {
        organizationRepository.findOneByCode("001").ifPresent(organization -> organizationRepository
                .findPageOfAvailableSuppliers(organization, PageRequest.of(0, 20)));
    }

    @Test
    void findAllByAdmin() {
        personRepository.findOneByMobile("18508101366").ifPresent(organizationRepository::findAllByAdmin);
    }

    @Test
    void countByAdmin() {
        personRepository.findOneByMobile("18508101366").ifPresent(organizationRepository::countByAdmin);
    }

}
