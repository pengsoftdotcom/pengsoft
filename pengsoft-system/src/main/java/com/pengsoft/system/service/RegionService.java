package com.pengsoft.system.service;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;

import com.pengsoft.support.service.TreeEntityService;
import com.pengsoft.system.domain.Region;

/**
 * The service interface of {@link Region}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface RegionService extends TreeEntityService<Region, String> {

    /**
     * Returns an {@link Optional} of a {@link Region} with given code.
     *
     * @param code {@link Region}'code
     */
    Optional<Region> findOneByCode(@NotBlank String code);

    /**
     * Returns an {@link Optional} of a {@link Region} with property 'index' is not
     * null.
     */
    List<Region> findAllIndexedCities();

}
