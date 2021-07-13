package com.pengsoft.security.repository;

import java.util.List;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotEmpty;

import com.pengsoft.security.domain.Authority;
import com.pengsoft.security.domain.QRoleAuthority;
import com.pengsoft.security.domain.RoleAuthority;
import com.pengsoft.security.domain.UserRole;
import com.pengsoft.support.repository.EntityRepository;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link RoleAuthority} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface RoleAuthorityRepository extends EntityRepository<QRoleAuthority, RoleAuthority, String> {

    /**
     * Returns all {@link UserRole} with given authority.
     * 
     * @param authorities
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<RoleAuthority> findAllByAuthorityIn(@NotEmpty Authority... authorities);

}
