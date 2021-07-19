package com.pengsoft.basedata.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Post;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.repository.PostRepository;
import com.pengsoft.basedata.repository.StaffRepository;
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

    @Inject
    private StaffRepository staffRepository;

    @Override
    public Post save(final Post post) {
        final var parent = Optional.ofNullable(post.getParent()).orElse(null);
        getRepository().findOneByOrganizationAndParentAndName(post.getOrganization(), parent, post.getName())
                .ifPresent(source -> {
                    if (EntityUtils.ne(source, post)) {
                        throw getExceptions().constraintViolated("name", "exists", post.getName());
                    }
                });
        super.save(post);
        getRepository().flush();
        final var admin = post.getOrganization().getAdmin();
        if (admin != null) {
            final var createdBy = admin.getUser().getId();
            final var updatedBy = SecurityUtils.getUserId();
            final var staff = staffRepository.findOneByPersonAndPrimaryTrue(admin).orElse(null);
            final var controlledBy = Optional.ofNullable(staff).map(Staff::getJob).map(Job::getDepartment)
                    .map(Department::getId).orElse(null);
            final var belongsTo = Optional.ofNullable(staff).map(Staff::getJob).map(Job::getDepartment)
                    .map(Department::getOrganization).map(Organization::getId).orElse(null);
            getRepository().updateBelonging(List.of(post), createdBy, updatedBy, controlledBy, belongsTo);
        }
        return post;
    }

}
