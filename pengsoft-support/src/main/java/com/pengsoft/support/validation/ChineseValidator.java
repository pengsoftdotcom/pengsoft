package com.pengsoft.support.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.pengsoft.support.util.StringUtils;

/**
 * The chinese character validator.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class ChineseValidator implements ConstraintValidator<Chinese, String> {

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return StringUtils.isChinese(value);
    }

}
