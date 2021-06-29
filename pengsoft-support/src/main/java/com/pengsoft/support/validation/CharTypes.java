package com.pengsoft.support.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * What {@link CharType}s are allowed, and how many {@link CharType}s are
 * contained at least.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Documented
@Constraint(validatedBy = { CharTypesValidator.class })
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
        ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CharTypes {

    /**
     * The {@link CharType}s allowed.
     */
    CharType[] allowed();

    /**
     * The minimum count of {@link CharType}s contains.
     */
    int count() default 1;

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
