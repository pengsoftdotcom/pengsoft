package com.pengsoft.security.repository;

import java.util.List;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotEmpty;

import com.pengsoft.security.domain.QUserRole;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.domain.UserRole;
import com.pengsoft.support.repository.EntityRepository;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link UserRole} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface UserRoleRepository extends EntityRepository<QUserRole, UserRole, String> {

    /**
     * Returns all {@link UserRole} with given role.
     * 
     * @param roles
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<UserRole> findAllByRoleIn(@NotEmpty Role... roles);

}
