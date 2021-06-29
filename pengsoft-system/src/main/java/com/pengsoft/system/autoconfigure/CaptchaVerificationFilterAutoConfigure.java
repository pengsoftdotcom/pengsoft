package com.pengsoft.system.autoconfigure;

import com.pengsoft.system.autoconfigure.properties.CaptchaVerificationFilterAutoConfigureProperties;
import com.pengsoft.system.filter.CaptchaVerificationFilter;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Configuration
@ComponentScan({ "com.pengsoft.system.filter" })
@EnableConfigurationProperties(CaptchaVerificationFilterAutoConfigureProperties.class)
public class CaptchaVerificationFilterAutoConfigure {

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> captchaVerificationFilterRegistrationBean(
            final CaptchaVerificationFilterAutoConfigureProperties properties, CaptchaVerificationFilter filter) {
        final FilterRegistrationBean<OncePerRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registrationBean.setUrlPatterns(properties.getVerificationRequiredUris());
        return registrationBean;
    }

}
