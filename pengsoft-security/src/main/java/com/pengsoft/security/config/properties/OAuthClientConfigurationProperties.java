package com.pengsoft.security.config.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties("pengsoft.security.oauth.client")
public class OAuthClientConfigurationProperties {

    private String id;

    private String secret;

    private String scope = "all";

    private int accessTokenValiditySeconds = 60 * 60 * 8;

    private List<String> grantTypes = List.of();

}
