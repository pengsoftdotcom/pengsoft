package com.pengsoft.basedata.api;

import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.service.PersonService;
import com.pengsoft.support.api.EntityApi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link Person}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping("api/person")
public class PersonApi extends EntityApi<PersonService, Person, String> {

}
