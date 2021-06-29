package com.pengsoft.system.autoconfigure.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * Messaging auto configure properties
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties("pengsoft.captcha")
public class CaptchaVerificationFilterAutoConfigureProperties {

    private List<String> verificationRequiredUris;

}
