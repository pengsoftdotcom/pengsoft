package com.pengsoft.support.config.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * Transaction auto configure properties
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties("pengsoft.transaction")
public class TransactionConfigurationProperties {

    private List<String> readonly;

    private List<String> required = List.of("*");

}
