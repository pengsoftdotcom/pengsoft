package com.pengsoft.basedata.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.QDepartment;
import com.pengsoft.support.repository.TreeEntityRepository;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
     * Returns an {@link Optional} of a {@link Department} with given organization,
     * parent and name.
     *
     * @param organization The {@link Department}'s organization
     * @param parent       The {@link Department}'s parent
     * @param name         {@link Department}'s name
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Department> findOneByOrganizationAndParentAndName(@NotNull Organization organization, Department parent,
            @NotBlank String name);

    /**
     * Returns all departments by given organization.
     * 
     * @param organization
     */
    List<Department> findAllByOrganization(Organization organization);

    /**
     * Update the department belonging.
     * 
     * @param departments
     * @param createdBy
     * @param updatedBy
     * @param controlledBy
     * @param belongsTo
     */
    @Modifying
    @Query("update Department o set o.createdBy = ?2, o.updatedBy = ?3, o.controlledBy = ?4, o.belongsTo = ?5, o.updatedAt = now() where o in ?1")
    void updateBelonging(@NotEmpty List<Department> departments, String createdBy, String updatedBy,
            String controlledBy, String belongsTo);

}
