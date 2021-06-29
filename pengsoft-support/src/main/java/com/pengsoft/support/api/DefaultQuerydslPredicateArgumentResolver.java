package com.pengsoft.support.api;

import java.util.Arrays;
import java.util.Optional;

import javax.inject.Named;

import com.pengsoft.support.util.ClassUtils;
import com.querydsl.core.BooleanBuilder;

import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.querydsl.binding.QuerydslPredicateBuilder;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.web.querydsl.QuerydslPredicateArgumentResolver;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Override the logic of resolve argument to avoid using
 * {@link QuerydslPredicate} in every web api.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
public class DefaultQuerydslPredicateArgumentResolver extends QuerydslPredicateArgumentResolver {

    private final QuerydslBindingsFactory bindingsFactory;

    private final QuerydslPredicateBuilder predicateBuilder;

    public DefaultQuerydslPredicateArgumentResolver(final QuerydslBindingsFactory factory,
            final Optional<ConversionService> conversionService) {
        super(factory, conversionService);
        bindingsFactory = factory;
        predicateBuilder = new QuerydslPredicateBuilder(conversionService.orElseGet(DefaultConversionService::new),
                factory.getEntityPathResolver());
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        final var parameters = new LinkedMultiValueMap<String, String>();

        for (final var entry : webRequest.getParameterMap().entrySet()) {
            parameters.put(entry.getKey(), Arrays.asList(entry.getValue()));
        }

        final var domainType = ClassTypeInformation
                .from(ClassUtils.getSuperclassGenericType(parameter.getContainingClass(), 1));
        final var bindings = bindingsFactory.createBindingsFor(domainType);
        return Optional.ofNullable(predicateBuilder.getPredicate(domainType, parameters, bindings))
                .orElse(new BooleanBuilder());
    }

}