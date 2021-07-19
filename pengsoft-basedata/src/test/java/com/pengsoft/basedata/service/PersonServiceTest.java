package com.pengsoft.basedata.service;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Person;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
class PersonServiceTest {

    @Inject
    PersonService service;

    @Test
    void save() {
        final var person = new Person();
        person.setName("党鹏");
        person.setMobile("18508101366");
        service.save(person);
    }

}
