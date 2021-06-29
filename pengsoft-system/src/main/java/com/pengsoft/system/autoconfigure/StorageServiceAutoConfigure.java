package com.pengsoft.system.autoconfigure;

import com.pengsoft.system.autoconfigure.properties.StorageServiceAutoConfigureProperties;
import com.pengsoft.system.service.StorageService;
import com.pengsoft.system.service.StorageServiceImpl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Object storage service auto configure.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(StorageServiceAutoConfigureProperties.class)
public class StorageServiceAutoConfigure {

    @ConditionalOnProperty(value = "pengsoft.storage.enabled", havingValue = "true")
    @Bean
    public StorageService storageService(final StorageServiceAutoConfigureProperties properties) {
        return new StorageServiceImpl(properties.getPublicAccessPathPrefix(), properties.getLockedAccessPathPrefix(),
                properties.getPublicBucket(), properties.getLockedBucket(), properties.getEndpoint(),
                properties.getAccessKeyId(), properties.getAccessKeySecret());
    }

}
