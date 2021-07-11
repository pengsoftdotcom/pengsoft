package com.pengsoft.basedata.repository;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
public class OrganizationRepositoryTest {

    @Inject
    private OrganizationRepository organizationRepository;

    @Inject
    private PersonRepository personRepository;

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
    void countByAdmin() {
        personRepository.findOneByMobile("18508101366").ifPresent(organizationRepository::countByAdmin);
    }

}
