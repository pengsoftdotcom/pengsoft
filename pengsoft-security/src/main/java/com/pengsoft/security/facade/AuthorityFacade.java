package com.pengsoft.security.facade;

import java.io.Serializable;

import com.pengsoft.security.domain.Authority;
import com.pengsoft.security.service.AuthorityService;
import com.pengsoft.support.domain.Entity;
import com.pengsoft.support.facade.EntityFacade;

/**
 * The facade interface of {@link Authority}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface AuthorityFacade extends EntityFacade<AuthorityService, Authority, String>, AuthorityService {

    /**
     * Save the authorities of entity admin.
     *
     * @param entityClass The entity class
     */
    void saveEntityAdminAuthorities(Class<? extends Entity<? extends Serializable>> entityClass);

}
