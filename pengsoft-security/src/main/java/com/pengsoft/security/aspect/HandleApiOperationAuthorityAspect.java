package com.pengsoft.security.aspect;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.inject.Named;

import com.pengsoft.security.annotation.Authorized;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.aspect.JoinPoints;
import com.pengsoft.support.domain.Entity;
import com.pengsoft.support.util.ClassUtils;
import com.pengsoft.support.util.StringUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;

/**
 * Handle API operation authorities from outside callers.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
@Aspect
public class HandleApiOperationAuthorityAspect {

    @SuppressWarnings("unchecked")
    @Around(JoinPoints.ALL_API)
    public Object handle(final ProceedingJoinPoint jp) throws Throwable {
        final var apiClass = jp.getTarget().getClass();
        final var entityClass = (Class<? extends Entity<? extends Serializable>>) ClassUtils
                .getSuperclassGenericType(apiClass, 1);
        var method = ((MethodSignature) jp.getSignature()).getMethod();
        method = ClassUtils.getPublicMethod(apiClass, jp.getSignature().getName(), method.getParameterTypes());
        final var args = jp.getArgs();
        if (apiClass.getAnnotation(Authorized.class) == null && method.getAnnotation(Authorized.class) == null
                && SecurityUtils.getUserDetails() != null) {
            final var modulePart = SecurityUtils.getModuleCodeFromEntityClass(entityClass);
            final var entityPart = SecurityUtils.getEntityCodeFromEntityClass(entityClass);
            final var methodPart = StringUtils.camelCaseToSnakeCase(jp.getSignature().getName(), false);
            final var requiredAuthority = StringUtils.join(new String[] { modulePart, entityPart, methodPart },
                    StringUtils.GLOBAL_SEPARATOR);
            final var grantedAuthorities = Optional.ofNullable(SecurityUtils.get("authorities", Collection.class))
                    .orElse(List.of());
            if (grantedAuthorities.stream().noneMatch(grantedAuthority -> StringUtils.equals(requiredAuthority,
                    ((GrantedAuthority) grantedAuthority).getAuthority()))) {
                throw new AccessDeniedException(requiredAuthority);
            }
        }
        return jp.proceed(args);
    }

}
