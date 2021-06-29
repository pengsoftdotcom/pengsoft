package com.pengsoft.security.repository;

import com.pengsoft.security.domain.QUserRole;
import com.pengsoft.security.domain.UserRole;
import com.pengsoft.support.repository.EntityRepository;

import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link UserRole} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface UserRoleRepository extends EntityRepository<QUserRole, UserRole, String> {

}
