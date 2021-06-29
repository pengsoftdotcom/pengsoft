package com.pengsoft.support.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JPA auto configure.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Configuration
@ComponentScan({ "com.pengsoft.*.facade" })
@ComponentScan({ "com.pengsoft.*.service" })
@EnableJpaRepositories({ "com.pengsoft.*.repository" })
@EntityScan({ "com.pengsoft.*.domain" })
public class DefaultJpaConfigurer {

}
