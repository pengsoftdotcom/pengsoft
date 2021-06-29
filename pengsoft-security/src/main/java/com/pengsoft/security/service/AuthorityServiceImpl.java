package com.pengsoft.security.service;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import com.pengsoft.security.domain.Authority;
import com.pengsoft.security.repository.AuthorityRepository;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link AuthorityService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class AuthorityServiceImpl extends EntityServiceImpl<AuthorityRepository, Authority, String>
        implements AuthorityService {

    @Override
    public Authority save(final Authority authority) {
        findOneByCode(authority.getCode()).ifPresent(source -> {
            if (EntityUtils.ne(source, authority)) {
                throw getExceptions().constraintViolated("code", "exists", authority.getCode());
            }
        });
        return super.save(authority);
    }

    @Override
    public Optional<Authority> findOneByCode(@NotBlank final String code) {
        return getRepository().findOneByCode(code);
    }

}
