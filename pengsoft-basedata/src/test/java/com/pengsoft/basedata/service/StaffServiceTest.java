package com.pengsoft.basedata.service;

import java.util.Locale;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
public class StaffServiceTest {

    @Inject
    MessageSource messages;

    @Test
    void test() {
        System.out.println(
                messages.getMessage("staff.save.exceeded", new String[] { "test" }, Locale.SIMPLIFIED_CHINESE));
    }

}
