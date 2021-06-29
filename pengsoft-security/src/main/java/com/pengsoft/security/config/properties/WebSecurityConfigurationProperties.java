package com.pengsoft.security.config.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties("pengsoft.security")
public class WebSecurityConfigurationProperties {

    /**
     * The maximum count of sign in failure
     */
    private int allowSignInFailure = 5;

    /**
     * The URIs permitted.
     */
    private List<String> urisPermitted = List.of();

}
