package com.pengsoft.system.autoconfigure.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * Aliyun SMS auto configure.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
public class Sms {

    private boolean enabled;

    private String regionId;

    private String accessKeyId;

    private String accessKeySecret;

}
