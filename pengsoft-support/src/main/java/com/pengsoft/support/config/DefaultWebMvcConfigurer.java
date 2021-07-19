package com.pengsoft.support.config;

import java.util.List;

import javax.inject.Inject;

import com.pengsoft.support.api.DefaultQuerydslPredicateArgumentResolver;
import com.pengsoft.support.config.properties.CorsConfigurationProperties;
import com.pengsoft.support.config.properties.WebMvcConfigurationProperties;
import com.pengsoft.support.json.ObjectMapper;
import com.pengsoft.support.util.DateUtils;

import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Configuration
@ComponentScan({ "com.pengsoft.*.exception", "com.pengsoft.*.api" })
@EnableSpringDataWebSupport
@EnableConfigurationProperties({ WebMvcConfigurationProperties.class, CorsConfigurationProperties.class })
public class DefaultWebMvcConfigurer implements WebMvcConfigurer {

    @Inject
    private LocalValidatorFactoryBean validator;

    @Inject
    private MessageSource messageSource;

    @Inject
    private DefaultQuerydslPredicateArgumentResolver predicateArgumentResolver;

    @Bean
    private static ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/static/");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(0, predicateArgumentResolver);
    }

    @Override
    public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper()));
    }

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        final var registrar = new DateTimeFormatterRegistrar();
        registrar.setTimeFormatter(DateUtils.timeFormatter);
        registrar.setDateFormatter(DateUtils.dateFormatter);
        registrar.setDateTimeFormatter(DateUtils.dateTimeFormatter);
        registrar.registerFormatters(registry);
    }

    @Override
    public Validator getValidator() {
        validator.setValidationMessageSource(messageSource);
        return validator;
    }

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> corsFilterRegistrationBean(
            final WebMvcConfigurationProperties properties) {
        final FilterRegistrationBean<OncePerRequestFilter> registrationBean = new FilterRegistrationBean<>();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(properties.getCors().isAllowCredentials());
        config.addAllowedOrigin(properties.getCors().getAllowedOrigin());
        config.addAllowedHeader(properties.getCors().getAllowedHeader());
        config.addAllowedMethod(properties.getCors().getAllowedMethod());
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        registrationBean.setFilter(new CorsFilter(source));
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

    @Bean
    public HttpTraceRepository httpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }

}
