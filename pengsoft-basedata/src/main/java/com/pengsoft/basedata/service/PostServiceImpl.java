package com.pengsoft.basedata.service;

import java.util.Optional;

import com.pengsoft.basedata.domain.Post;
import com.pengsoft.basedata.facade.OrganizationFacadeImpl;
import com.pengsoft.basedata.repository.PostRepository;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.service.TreeEntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link PostService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class PostServiceImpl extends TreeEntityServiceImpl<PostRepository, Post, String> implements PostService {

    @Override
    public Post save(final Post post) {
        final var parentId = Optional.ofNullable(post.getParent()).map(Post::getId).orElse(null);
        getRepository()
                .findOneByOrganizationIdAndParentIdAndName(post.getOrganization().getId(), parentId, post.getName())
                .ifPresent(source -> {
                    if (EntityUtils.ne(source, post)) {
                        throw getExceptions().constraintViolated("name", "exists", post.getName());
                    }
                });
        if (!SecurityUtils.hasAnyRole(OrganizationFacadeImpl.ORGANIZATION_ADMIN)
                && SecurityUtils.hasAnyRole(Role.ADMIN, OrganizationFacadeImpl.BASEDATA_ORGANIZATION_ADMIN)) {
            post.setCreatedBy(post.getOrganization().getCreatedBy());
            post.setBelongsTo(post.getOrganization().getBelongsTo());
        }
        return super.save(post);
    }

}
