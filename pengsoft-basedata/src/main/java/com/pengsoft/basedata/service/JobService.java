package com.pengsoft.basedata.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Job;
import com.pengsoft.security.domain.Role;
import com.pengsoft.support.service.TreeEntityService;

/**
 * The service interface of {@link Job}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface JobService extends TreeEntityService<Job, String> {

    /**
     * Grant roles.
     *
     * @param job   The {@link Job}.
     * @param roles The {@link Role}s to be granted.
     */
    void grantRoles(@NotNull Job job, List<Role> roles);

}
