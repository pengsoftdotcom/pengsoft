package com.pengsoft.support.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * The mobile phone number constraint.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Documented
@Constraint(validatedBy = { MobileValidator.class })
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
        ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Mobile {

    String message() default "{Mobile}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
