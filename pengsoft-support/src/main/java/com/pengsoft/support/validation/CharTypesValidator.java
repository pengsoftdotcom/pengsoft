package com.pengsoft.support.validation;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

/**
 * Password validator.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class CharTypesValidator implements ConstraintValidator<CharTypes, String> {

    private static final String separator = "-_@.";

    private CharTypes charTypes;

    @Override
    public void initialize(final CharTypes charTypes) {
        this.charTypes = charTypes;
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (StringUtils.isNotBlank(value)) {
            final var types = value.chars().mapToObj(this::convert).collect(Collectors.toSet());
            final var allowedTypes = Arrays.stream(charTypes.allowed()).collect(Collectors.toSet());
            // check if 'value' contains characters not allowed
            if (types.stream().anyMatch(type -> allowedTypes.stream().noneMatch(type::equals))) {
                return false;
            }
            // check if the number of char types that 'value' contains greater or equals
            // than configured.
            return types.stream().filter(type -> allowedTypes.stream().anyMatch(type::equals)).count() >= charTypes
                    .count();
        }
        return false;
    }

    private CharType convert(final int ch) {

        final var characterType = Character.getType(ch);
        var charType = switch (characterType) {
            case Character.DECIMAL_DIGIT_NUMBER -> CharType.DIGIT;
            case Character.UPPERCASE_LETTER -> CharType.UPPERCASE_LETTER;
            case Character.LOWERCASE_LETTER -> CharType.LOWERCASE_LETTER;
            default -> CharType.PUNCTUATION;
        };
        if (charType == CharType.PUNCTUATION && separator.chars().anyMatch(s -> s == ch)) {
            charType = CharType.SEPARATOR;
        }
        return charType;
    }

}
