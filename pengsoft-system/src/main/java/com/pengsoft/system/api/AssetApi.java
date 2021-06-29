package com.pengsoft.system.api;

import java.util.List;

import javax.inject.Inject;

import com.pengsoft.support.api.EntityApi;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.service.AssetService;
import com.pengsoft.system.service.StorageService;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * The web api of {@link Asset}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping("api/asset")
public class AssetApi extends EntityApi<AssetService, Asset, String> {

    @Inject
    private StorageService storageService;

    @PostMapping("upload")
    public List<Asset> upload(@RequestParam("file") final List<MultipartFile> files,
            @RequestParam(name = "locked", defaultValue = "false") final boolean locked) {
        return getService().save(storageService.upload(files, locked));
    }

    @Override
    public void delete(final Predicate predicate) {
        final var assets = getService().findAll(predicate, Sort.unsorted());
        getService().delete(assets);
        storageService.delete(assets);
    }

}
