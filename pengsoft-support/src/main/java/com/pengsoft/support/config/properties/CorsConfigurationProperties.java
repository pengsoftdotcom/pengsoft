package com.pengsoft.support.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * The CORS auto configure properties.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties("pengsoft.web.cors")
public class CorsConfigurationProperties {

    private boolean allowCredentials = false;

    private String allowedOrigin = "*";

    private String allowedHeader = "*";

    private String allowedMethod = "*";

}
