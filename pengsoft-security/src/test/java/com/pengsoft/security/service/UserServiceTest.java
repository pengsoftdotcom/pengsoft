package com.pengsoft.security.service;

import javax.inject.Inject;

import com.pengsoft.security.domain.User;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security" })
class UserServiceTest {

    @Inject
    UserService service;

    @Test
    void saveWithoutValidation() {
        service.delete(service.saveWithoutValidation(new User("test", "test")));
    }

}
