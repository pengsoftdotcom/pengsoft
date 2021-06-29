package com.pengsoft.basedata.service;

import java.util.List;

import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.repository.OrganizationRepository;
import com.pengsoft.support.service.TreeEntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.support.util.StringUtils;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link OrganizationService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class OrganizationServiceImpl extends TreeEntityServiceImpl<OrganizationRepository, Organization, String>
        implements OrganizationService {

    @Override
    public Organization save(final Organization organization) {
        getRepository().findOneByCode(organization.getCode()).ifPresent(source -> {
            if (EntityUtils.ne(source, organization)) {
                throw getExceptions().constraintViolated("code", "exists", organization.getCode());
            }
        });
        getRepository().findOneByName(organization.getName()).ifPresent(source -> {
            if (EntityUtils.ne(source, organization)) {
                throw getExceptions().constraintViolated("name", "exists", organization.getCode());
            }
        });
        if (StringUtils.isBlank(organization.getShortName())) {
            organization.setShortName(organization.getName());
        }
        super.save(organization);
        if (StringUtils.notEquals(organization.getId(), organization.getBelongsTo())) {
            getRepository().updateBelongsTo(organization.getId());
        }
        return organization;
    }

    @Override
    public List<Organization> findAllByAdmin(final Person admin) {
        return getRepository().findAllByAdminId(admin.getId());
    }

}
