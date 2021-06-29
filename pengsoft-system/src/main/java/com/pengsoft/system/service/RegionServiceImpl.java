package com.pengsoft.system.service;

import java.util.List;
import java.util.Optional;

import com.pengsoft.support.service.TreeEntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.system.domain.Region;
import com.pengsoft.system.repository.RegionRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link RegionService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class RegionServiceImpl extends TreeEntityServiceImpl<RegionRepository, Region, String>
        implements RegionService {

    @Override
    public Region save(final Region region) {
        findOneByCode(region.getCode()).ifPresent(source -> {
            if (EntityUtils.ne(source, region)) {
                throw getExceptions().constraintViolated("code", "exists", region.getCode());
            }
        });
        return super.save(region);
    }

    @Override
    public Optional<Region> findOneByCode(final String code) {
        return getRepository().findOneByCode(code);
    }

    @Override
    public List<Region> findAllIndexedCities() {
        return getRepository().findAllByIndexIsNotNullOrderByIndexAscCodeAsc();
    }

}
