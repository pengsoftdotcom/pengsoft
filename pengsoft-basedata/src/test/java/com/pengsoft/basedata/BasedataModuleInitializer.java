package com.pengsoft.basedata;

import java.util.List;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.Post;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.security.facade.AuthorityFacade;
import com.pengsoft.security.service.RoleService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "support", "security", "system", "basedata" })
public class BasedataModuleInitializer {

    @Inject
    private RoleService service;

    @Inject
    private AuthorityFacade facade;

    @Test
    void initRolesAndAuthorities() {
        List.of(Department.class, Job.class, Organization.class, Person.class, Post.class, Staff.class)
                .forEach(entityClass -> {
                    service.saveEntityAdmin(entityClass);
                    facade.saveEntityAdminAuthorities(entityClass);
                });
    }

}
