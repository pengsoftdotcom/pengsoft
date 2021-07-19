package com.pengsoft.basedata.api;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
@AutoConfigureMockMvc
class OrganizationApiTest {

    @Inject
    MockMvc mockMvc;

    @Test
    @WithUserDetails("18508101366")
    void findAllAvailableSuppliers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/organization/find-all-available-suppliers")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("consumer.id", "0ac76207-8ca9-47d8-ae59-3f5e95b4ac70"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
