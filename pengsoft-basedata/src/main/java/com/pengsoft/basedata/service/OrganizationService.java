package com.pengsoft.basedata.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.support.service.TreeEntityService;

/**
 * The service interface of {@link Organization}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface OrganizationService extends TreeEntityService<Organization, String> {

    /**
     * Returns all organizations with given admin.
     *
     * @param admin {@link Organization}'s admin
     */
    List<Organization> findAllByAdmin(@NotNull Person admin);

}
