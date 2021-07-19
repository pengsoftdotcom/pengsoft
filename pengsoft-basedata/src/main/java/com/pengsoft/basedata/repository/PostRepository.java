package com.pengsoft.basedata.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Post;
import com.pengsoft.basedata.domain.QPost;
import com.pengsoft.support.repository.TreeEntityRepository;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link Post} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface PostRepository extends TreeEntityRepository<QPost, Post, String>, OwnedExtRepository {

    @Override
    default void customize(final QuerydslBindings bindings, final QPost root) {
        TreeEntityRepository.super.customize(bindings, root);
        bindings.bind(root.name).first(StringPath::contains);
    }

    /**
     * Returns an {@link Optional} of a {@link Post} with given organization id,
     * parent id and name.
     *
     * @param organization The {@link Post}'s organization
     * @param parent       The {@link Post}'s parent
     * @param name         The {@link Post}'s name
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Post> findOneByOrganizationAndParentAndName(@NotNull Organization organization, Post parent, String name);

    /**
     * Returns all posts by given organization.
     * 
     * @param organization
     */
    List<Post> findAllByOrganization(Organization organization);

    /**
     * Update the post belonging.
     * 
     * @param posts
     * @param createdBy
     * @param updatedBy
     * @param controlledBy
     * @param belongsTo
     */
    @Modifying
    @Query("update Post o set o.createdBy = ?2, o.updatedBy = ?3, o.controlledBy = ?4, o.belongsTo = ?5, o.updatedAt = now() where o in ?1")
    void updateBelonging(@NotEmpty List<Post> posts, String createdBy, String updatedBy, String controlledBy,
            String belongsTo);

}
