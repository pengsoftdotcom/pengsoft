package com.pengsoft.support.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * Web mvc auto configure properties
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties("pengsoft.web")
public class WebMvcConfigurationProperties {

    private CorsConfigurationProperties cors = new CorsConfigurationProperties();

}
