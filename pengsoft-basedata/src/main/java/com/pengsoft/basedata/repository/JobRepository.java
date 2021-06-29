package com.pengsoft.basedata.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.QJob;
import com.pengsoft.support.repository.TreeEntityRepository;
import com.querydsl.core.types.dsl.StringPath;

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
     * Returns an {@link Optional} of a {@link Job} with given department id, parent
     * id and name.
     *
     * @param departmentId The id of {@link Job}'s department
     * @param parentId     The id of {@link Job}'s parent
     * @param name         {@link Job}'s name
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Job> findOneByDepartmentIdAndParentIdAndName(@NotBlank String departmentId, String parentId,
            @NotBlank String name);

}
