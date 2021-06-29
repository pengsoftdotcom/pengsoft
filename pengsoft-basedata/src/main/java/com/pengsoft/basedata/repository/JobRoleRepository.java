package com.pengsoft.basedata.repository;

import com.pengsoft.basedata.domain.JobRole;
import com.pengsoft.basedata.domain.QJobRole;
import com.pengsoft.support.repository.EntityRepository;

import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link JobRole} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface JobRoleRepository extends EntityRepository<QJobRole, JobRole, String> {

}
