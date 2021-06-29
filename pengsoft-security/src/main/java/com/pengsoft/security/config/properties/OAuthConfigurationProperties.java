package com.pengsoft.security.config.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties("pengsoft.security.oauth")
public class OAuthConfigurationProperties {

    private boolean authorizationServerEnabled;

    private boolean resourceServerEnabled;

    private List<OAuthClientConfigurationProperties> clients = new ArrayList<>();

}
