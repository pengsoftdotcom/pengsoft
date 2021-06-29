package com.pengsoft.support.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.pengsoft.support.util.StringUtils;

/**
 * The mobile phone number validator.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return StringUtils.isMobile(value);
    }

}
