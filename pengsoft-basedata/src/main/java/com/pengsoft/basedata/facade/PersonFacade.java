package com.pengsoft.basedata.facade;

import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.service.PersonService;
import com.pengsoft.support.facade.EntityFacade;

/**
 * The facade interface of {@link Person}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface PersonFacade extends EntityFacade<PersonService, Person, String>, PersonService {

}
