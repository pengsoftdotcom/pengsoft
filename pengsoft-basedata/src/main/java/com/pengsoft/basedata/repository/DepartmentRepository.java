package com.pengsoft.basedata.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.QDepartment;
import com.pengsoft.support.repository.TreeEntityRepository;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link Department} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface DepartmentRepository
        extends TreeEntityRepository<QDepartment, Department, String>, OwnedExtRepository {

    @Override
    default void customize(final QuerydslBindings bindings, final QDepartment root) {
        TreeEntityRepository.super.customize(bindings, root);
        bindings.bind(root.name).first(StringPath::contains);
    }

    /**
     * Returns an {@link Optional} of a {@link Department} with given organization
     * id, parent id and name.
     *
     * @param organizationId The id of {@link Department}'s organization
     * @param parentId       The id of {@link Department}'s parent
     * @param name           {@link Department}'s name
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Organization> findOneByOrganizationIdAndParentIdAndName(@NotBlank String organizationId, String parentId,
            @NotBlank String name);

}
