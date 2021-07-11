package com.pengsoft.basedata.service;

import java.util.List;
import java.util.Optional;

import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.repository.OrganizationRepository;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.support.util.StringUtils;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link OrganizationService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class OrganizationServiceImpl extends EntityServiceImpl<OrganizationRepository, Organization, String>
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
        return super.save(organization);
    }

    @Override
    public Optional<Organization> findOneByCode(String code) {
        return getRepository().findOneByCode(code);
    }

    @Override
    public Optional<Organization> findOneByName(String name) {
        return getRepository().findOneByName(name);
    }

    @Override
    public List<Organization> findAllByAdmin(final Person admin) {
        return getRepository().findAllByAdminId(admin.getId());
    }

    @Override
    public long countByAdmin(Person admin) {
        return getRepository().countByAdmin(admin);
    }

    @Override
    public Page<Organization> findPageOfAvailableConsumers(Organization supplier, Pageable pageable) {
        return getRepository().findPageOfAvailableConsumers(supplier, pageable);
    }

    @Override
    public List<Organization> findAllAvailableConsumers(Organization supplier) {
        return getRepository().findAllAvailableConsumers(supplier);
    }

    @Override
    public Page<Organization> findPageOfAvailableSuppliers(Organization consumer, Pageable pageable) {
        return getRepository().findPageOfAvailableSuppliers(consumer, pageable);
    }

    @Override
    public List<Organization> findAllAvailableSuppliers(Organization consumer) {
        return getRepository().findAllAvailableSuppliers(consumer);
    }

}
