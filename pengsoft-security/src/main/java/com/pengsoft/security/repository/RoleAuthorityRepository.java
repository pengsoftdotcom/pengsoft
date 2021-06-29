package com.pengsoft.security.repository;

import com.pengsoft.security.domain.QRoleAuthority;
import com.pengsoft.security.domain.RoleAuthority;
import com.pengsoft.support.repository.EntityRepository;

import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link RoleAuthority} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface RoleAuthorityRepository extends EntityRepository<QRoleAuthority, RoleAuthority, String> {

}
