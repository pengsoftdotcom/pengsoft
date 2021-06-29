package com.pengsoft.support.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Aspect auto configure.
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan({ "com.pengsoft.*.aspect" })
public class DefaultAspectJConfigurer {

}
