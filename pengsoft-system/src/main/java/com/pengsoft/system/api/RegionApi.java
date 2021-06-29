package com.pengsoft.system.api;

import java.util.List;
import java.util.stream.Collectors;

import com.pengsoft.security.annotation.Authorized;
import com.pengsoft.support.api.TreeEntityApi;
import com.pengsoft.system.domain.Region;
import com.pengsoft.system.json.RegionWrapper;
import com.pengsoft.system.service.RegionService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link Region}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping("api/region")
public class RegionApi extends TreeEntityApi<RegionService, Region, String> {

    @Authorized
    @GetMapping("find-all-indexed-cities")
    public List<RegionWrapper> findAllIndexedCities() {
        return getService().findAllIndexedCities().stream().map(RegionWrapper::new).collect(Collectors.toList());
    }

}