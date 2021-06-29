package com.pengsoft.basedata.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.basedata.domain.Post;
import com.pengsoft.basedata.domain.QPost;
import com.pengsoft.support.repository.TreeEntityRepository;
import com.querydsl.core.types.dsl.StringPath;

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
     * @param organizationId The id of {@link Post}'s organization
     * @param parentId       The id of {@link Post}'s parent
     * @param name           {@link Post}'s name
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Post> findOneByOrganizationIdAndParentIdAndName(@NotBlank String organizationId, String parentId,
            @NotBlank String name);

}
