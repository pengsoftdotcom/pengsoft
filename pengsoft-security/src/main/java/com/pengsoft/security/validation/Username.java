package com.pengsoft.security.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.pengsoft.support.validation.CharType;
import com.pengsoft.support.validation.CharTypes;

/**
 * The username constraint.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@CharTypes(allowed = { CharType.DIGIT, CharType.LOWERCASE_LETTER, CharType.UPPERCASE_LETTER,
        CharType.SEPARATOR }, message = "{CharTypes.username}")
@NotBlank
@Size(min = 4, max = 20)
@Documented
@Constraint(validatedBy = {})
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
        ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Username {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
