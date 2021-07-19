package com.pengsoft.system.service;

import javax.inject.Inject;

import com.pengsoft.security.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system" })
class CaptchaServiceTest {

    @Inject
    CaptchaService service;

    @Inject
    UserRepository repository;

    @Test
    void generate() {
        repository.findOneByMobile("18508101366").ifPresent(service::generate);
    }

}
