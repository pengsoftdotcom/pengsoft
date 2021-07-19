package com.pengsoft.basedata.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.QJob;
import com.pengsoft.support.repository.TreeEntityRepository;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link Job} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface JobRepository extends TreeEntityRepository<QJob, Job, String>, OwnedExtRepository {

    @Override
    default void customize(final QuerydslBindings bindings, final QJob root) {
        TreeEntityRepository.super.customize(bindings, root);
        bindings.bind(root.name).first(StringPath::contains);
    }

    /**
     * Returns an {@link Optional} of a {@link Job} with given department, parent
     * and name.
     *
     * @param department The {@link Job}'s department
     * @param parent     The {@link Job}'s parent
     * @param name       The {@link Job}'s name
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Job> findOneByDepartmentAndParentAndName(@NotNull Department department, Job parent, String name);

    /**
     * Returns all jobs by given organization.
     * 
     * @param organization
     */
    List<Job> findAllByDepartmentOrganization(Organization organization);

    /**
     * Update the job belonging.
     * 
     * @param jobs
     * @param createdBy
     * @param updatedBy
     * @param controlledBy
     * @param belongsTo
     */
    @Modifying
    @Query("update Job o set o.createdBy = ?2, o.updatedBy = ?3, o.controlledBy = ?4, o.belongsTo = ?5, o.updatedAt = now() where o in ?1")
    void updateBelonging(@NotEmpty List<Job> jobs, String createdBy, String updatedBy, String controlledBy,
            String belongsTo);

}
