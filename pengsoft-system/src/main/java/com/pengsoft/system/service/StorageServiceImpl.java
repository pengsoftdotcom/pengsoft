package com.pengsoft.system.service;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.exception.BusinessException;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.domain.Asset;

import org.springframework.web.multipart.MultipartFile;

/**
 * Aliyun object storage service.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class StorageServiceImpl implements StorageService {

    private static final String EC_ASSET_UPLOAD_FAILED = "asset.upload.failed";

    private final String publicAccessPathPrefix;

    private final String lockedAccessPathPrefix;

    private final String publicBucket;

    private final String lockedBucket;

    private final String endpoint;

    private final String accessKeyId;

    private final String accessKeySecret;

    public StorageServiceImpl(final String publicAccessPathPrefix, final String lockedAccessPathPrefix,
            final String publicBucket, final String lockedBucket, final String endpoint, final String accessKeyId,
            final String accessKeySecret) {
        this.publicAccessPathPrefix = publicAccessPathPrefix;
        this.lockedAccessPathPrefix = lockedAccessPathPrefix;
        this.publicBucket = publicBucket;
        this.lockedBucket = lockedBucket;
        this.endpoint = endpoint;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
    }

    public String getPublicAccessPathPrefix() {
        return publicAccessPathPrefix;
    }

    public String getLockedAccessPathPrefix() {
        return lockedAccessPathPrefix;
    }

    private OSSClient getClient() {
        return (OSSClient) new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    @Override
    public Asset upload(@NotNull final MultipartFile file, @NotNull final boolean locked) {
        final var bucket = locked ? lockedBucket : publicBucket;
        final var asset = new Asset();
        asset.setLocked(locked);
        asset.setContentLength(file.getSize());
        asset.setContentType(file.getContentType());
        asset.setOriginalName(file.getOriginalFilename());
        final var extension = StringUtils.substringAfterLast(asset.getOriginalName(), StringUtils.PACKAGE_SEPARATOR)
                .toLowerCase();
        asset.setPresentName(UUID.randomUUID().toString() + StringUtils.PACKAGE_SEPARATOR + extension);

        final var accessPathPrefix = new StringBuilder(
                locked ? getLockedAccessPathPrefix() : getPublicAccessPathPrefix());
        if (!accessPathPrefix.toString().endsWith(StringUtils.FILE_SEPARATOR)) {
            accessPathPrefix.append(StringUtils.FILE_SEPARATOR);
        }

        // 用于OSS的KEY
        final var accessPathSuffix = new StringBuilder();
        accessPathSuffix.append("user");
        accessPathSuffix.append(StringUtils.FILE_SEPARATOR);
        accessPathSuffix.append(SecurityUtils.getUserId());
        accessPathSuffix.append(StringUtils.FILE_SEPARATOR);
        accessPathSuffix.append(asset.getPresentName());
        asset.setStoragePath(bucket + StringUtils.GLOBAL_SEPARATOR + accessPathSuffix.toString());
        asset.setAccessPath(accessPathPrefix.append(accessPathSuffix).toString());
        final var client = getClient();
        try {
            client.putObject(bucket, accessPathSuffix.toString(), file.getInputStream());
        } catch (final Exception e) {
            throw new BusinessException(EC_ASSET_UPLOAD_FAILED, e.getMessage());
        } finally {
            client.shutdown();
        }
        return asset;
    }

    @Override
    public Asset download(@NotNull final Asset asset) {
        final var client = getClient();
        try {
            final var bucket = asset.isLocked() ? lockedBucket : publicBucket;
            final var ossKey = new StringBuilder();
            ossKey.append("user");
            ossKey.append(StringUtils.FILE_SEPARATOR);
            ossKey.append(SecurityUtils.getUserId());
            ossKey.append(StringUtils.FILE_SEPARATOR);
            ossKey.append(asset.getPresentName());
            asset.setInputStream(client.getObject(bucket, ossKey.toString()).getObjectContent());
        } finally {
            client.shutdown();
        }
        return asset;
    }

    @Override
    public void delete(@NotNull final Asset asset) {
        final var client = getClient();
        try {
            final var bucket = asset.isLocked() ? lockedBucket : publicBucket;
            final var ossKey = new StringBuilder();
            ossKey.append("user");
            ossKey.append(StringUtils.FILE_SEPARATOR);
            ossKey.append(SecurityUtils.getUserId());
            ossKey.append(StringUtils.FILE_SEPARATOR);
            ossKey.append(asset.getPresentName());
            client.deleteObject(bucket, ossKey.toString());
        } finally {
            client.shutdown();
        }
    }

}
