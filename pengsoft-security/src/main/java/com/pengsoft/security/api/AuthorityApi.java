package com.pengsoft.security.api;

import com.pengsoft.security.annotation.AuthorityChanged;
import com.pengsoft.security.domain.Authority;
import com.pengsoft.security.service.AuthorityService;
import com.pengsoft.support.api.EntityApi;
import com.querydsl.core.types.Predicate;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link Authority}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping("api/authority")
public class AuthorityApi extends EntityApi<AuthorityService, Authority, String> {

    @AuthorityChanged
    @Override
    public void save(@RequestBody Authority entity) {
        super.save(entity);
    }

    @AuthorityChanged
    @Override
    public void delete(Predicate predicate) {
        super.delete(predicate);
    }

}
