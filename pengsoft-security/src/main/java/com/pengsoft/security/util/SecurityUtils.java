package com.pengsoft.security.util;

import static com.pengsoft.support.util.StringUtils.ESCAPES;
import static com.pengsoft.support.util.StringUtils.PACKAGE_SEPARATOR;

import java.io.Serializable;
import java.util.Arrays;

import com.pengsoft.security.domain.DefaultUserDetails;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.domain.User;
import com.pengsoft.support.domain.Entity;
import com.pengsoft.support.util.StringUtils;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Security utility methods.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class SecurityUtils {

    private static final String ADMIN = "admin";

    private static final SpelExpressionParser parser = new SpelExpressionParser();

    private SecurityUtils() {
    }

    /**
     * Returns {@link UserDetails}
     */
    public static DefaultUserDetails getUserDetails() {
        final var context = SecurityContextHolder.getContext();
        if (context != null) {
            final var authentication = context.getAuthentication();
            if (authentication != null) {
                final var principal = authentication.getPrincipal();
                if (principal instanceof DefaultUserDetails) {
                    return (DefaultUserDetails) principal;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public static boolean hasAnyRole(final String... roleCodes) {
        final var userDetails = getUserDetails();
        if (userDetails == null) {
            return false;
        } else {
            return userDetails.getRoles().stream()
                    .anyMatch(role -> Arrays.stream(roleCodes).anyMatch(roleCode -> roleCode.equals(role.getCode())));
        }
    }

    /**
     * Returns the current user.
     */
    public static User getUser() {
        return get("user", User.class);
    }

    /**
     * Returns the current user id.
     */
    public static String getUserId() {
        return getId("user");
    }

    /**
     * Returns the current role.
     */
    public static Role getCurrentRole() {
        return get("currentRole", Role.class);
    }

    /**
     * Returns the current role id.
     */
    public static String getCurrentRoleId() {
        return getId("currentRole");
    }

    /**
     * Returns the primary role.
     */
    public static Role getPrimaryRole() {
        return get("primaryRole", Role.class);
    }

    /**
     * Returns the primary role id.
     */
    public static String getPrimaryRoleId() {
        return getId("primaryRole");
    }

    /**
     * Returns the property of {@link UserDetails}
     *
     * @param property The raw expression string to parse.
     * @param clazz    The class the caller would like the result to be
     */
    public static <T> T get(final String property, final Class<T> clazz) {
        final var userDetails = getUserDetails();
        if (userDetails == null) {
            return null;
        } else {
            final var expression = StringUtils.substringBefore(StringUtils.substringBefore(property, PACKAGE_SEPARATOR),
                    "?");
            if (FieldUtils.getField(userDetails.getClass(), expression, true) != null) {
                return parser.parseExpression(property).getValue(userDetails, clazz);
            } else {
                return null;
            }
        }
    }

    /**
     * Returns the property's id of {@link UserDetails}
     *
     * @param property The raw expression string to parse.
     */
    public static String getId(final String property) {
        if (isPropertyExists(property)) {
            return SecurityUtils.get(property + "?.id", String.class);
        } else {
            return null;
        }
    }

    private static boolean isPropertyExists(final String property) {
        final var userDetails = getUserDetails();
        if (userDetails == null) {
            return false;
        } else {
            return FieldUtils.getField(userDetails.getClass(), property, true) != null;
        }
    }

    /**
     * Returns the module admin role code.
     *
     * @param entityClass The entity class.
     * @return The module admin role code.
     */
    public static String getModuleAdminCode(final Class<? extends Entity<? extends Serializable>> entityClass) {
        final var moduleCode = getModuleCodeFromEntityClass(entityClass).replaceAll(ESCAPES + PACKAGE_SEPARATOR,
                StringUtils.UNDERLINE);
        return StringUtils.join(new String[] { moduleCode, ADMIN }, StringUtils.UNDERLINE);
    }

    /**
     * Returns the entity admin role code.
     *
     * @param entityClass The entity class.
     * @return The entity admin role code
     */
    public static String getEntityAdminCode(final Class<? extends Entity<? extends Serializable>> entityClass) {
        final var moduleCode = getModuleCodeFromEntityClass(entityClass);
        final var entityCode = getEntityCodeFromEntityClass(entityClass);
        return StringUtils.join(new String[] { moduleCode, entityCode, ADMIN }, StringUtils.UNDERLINE);
    }

    /**
     * Returns the entity class.
     *
     * @param entityClass The entity class.
     * @return The entity code.
     */
    public static String getModuleCodeFromEntityClass(
            final Class<? extends Entity<? extends Serializable>> entityClass) {
        var packageName = getModuleCodeFromPackageName(entityClass.getPackageName());
        return packageName.replaceAll(ESCAPES + PACKAGE_SEPARATOR, StringUtils.UNDERLINE);
    }

    /**
     * Returns the module code with the given package name and sub-package name.
     *
     * @param packageName The package name.
     * @return The module code.
     */
    public static String getModuleCodeFromPackageName(final String packageName) {
        return StringUtils.substringAfter(
                StringUtils.substringBetween(packageName, "com" + PACKAGE_SEPARATOR, PACKAGE_SEPARATOR + "domain"),
                PACKAGE_SEPARATOR);
    }

    /**
     * Returns the entity code.
     *
     * @param entityClass The entity class.
     * @return The entity code.
     */
    public static String getEntityCodeFromEntityClass(
            final Class<? extends Entity<? extends Serializable>> entityClass) {
        return StringUtils.camelCaseToSnakeCase(entityClass.getSimpleName(), false);
    }

    /**
     * Returns the entity admin authority code prefix by given entity class.
     *
     * @param entityClass The entity class.
     * @return The entity admin authority code prefix.
     */
    public static String getEntityAdminAuthorityCodePrefixFromEntityClass(
            final Class<? extends Entity<? extends Serializable>> entityClass) {
        final var moduleCode = getModuleCodeFromEntityClass(entityClass);
        final var entityCode = getEntityCodeFromEntityClass(entityClass);
        return StringUtils.join(new String[] { moduleCode, entityCode }, StringUtils.GLOBAL_SEPARATOR);
    }

}
