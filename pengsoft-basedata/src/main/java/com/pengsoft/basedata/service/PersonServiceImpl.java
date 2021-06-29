package com.pengsoft.basedata.service;

import java.util.Optional;

import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.repository.PersonRepository;
import com.pengsoft.security.domain.User;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.support.util.StringUtils;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link PersonService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class PersonServiceImpl extends EntityServiceImpl<PersonRepository, Person, String> implements PersonService {

    @Override
    public Person save(final Person person) {
        getRepository().findOneByMobile(person.getMobile()).ifPresent(source -> {
            if (EntityUtils.ne(source, person)) {
                throw getExceptions().constraintViolated("mobile", "Exists", person.getMobile());
            }
        });
        if (StringUtils.isBlank(person.getNickname())) {
            person.setNickname("*" + person.getName().substring(1));
        }
        return super.save(person);
    }

    @Override
    public Optional<Person> findOneByMobile(final String mobile) {
        return getRepository().findOneByMobile(mobile);
    }

    @Override
    public Optional<Person> findOneByUser(final User user) {
        return getRepository().findOneByUserId(user.getId());
    }

}
