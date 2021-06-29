package com.pengsoft.support.util;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * String utility methods.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static final String GLOBAL_SEPARATOR = "::";

    public static final String PACKAGE_SEPARATOR = ".";

    public static final String FILE_SEPARATOR = "/";

    public static final String HYPHEN = "-";

    public static final String UNDERLINE = "_";

    public static final String COMMA = ",";

    public static final String ESCAPES = "\\";

    /**
     * @see #equals(CharSequence, CharSequence)
     */
    public static boolean notEquals(final CharSequence cs1, final CharSequence cs2) {
        return !equals(cs1, cs2);
    }

    /**
     * Camel case to the other case.
     *
     * @param name        The camel case name.
     * @param separator   The separator.
     * @param isUpperCase Change to the other upper case name.
     * @return The other case name.
     */
    private static String camelCaseTo(final String name, final String separator, final boolean isUpperCase) {
        if (isBlank(name)) {
            return name;
        } else {
            final var result = new StringBuilder();
            final var charArray = name.toCharArray();
            result.append(charArray[0]);
            if (charArray.length > 1) {
                for (int i = 1; i < charArray.length; i++) {
                    final var c = charArray[i];
                    if (Character.getType(c) < 3 && c == Character.toUpperCase(c)
                            && !separator.equals(String.valueOf(c))) {
                        result.append(separator);
                    }
                    result.append(c);
                }
            }
            if (isUpperCase) {
                return result.toString().toUpperCase();
            } else {
                return result.toString().toLowerCase();
            }
        }
    }

    /**
     * Camel case to snake case.
     *
     * @param name        The camel case name.
     * @param isUpperCase Change to upper snake case name.
     * @return The snake case name.
     */
    public static String camelCaseToSnakeCase(final String name, final boolean isUpperCase) {
        return camelCaseTo(name, UNDERLINE, isUpperCase);
    }

    /**
     * Camel case to kebab case.
     *
     * @param name        The camel case name.
     * @param isUpperCase Change to upper kebab case name.
     * @return The kebab case name.
     */
    public static String camelCaseToKebabCase(final String name, final boolean isUpperCase) {
        return camelCaseTo(name, HYPHEN, isUpperCase);
    }

    /**
     * Check the given characters is full of chinese characters.
     *
     * @param text Characters to be checked.
     * @return if true, text is full of chinese characters.
     */
    public static boolean isChinese(final String text) {
        if (isBlank(text)) {
            return true;
        } else {
            final var regex = "[\u4e00-\u9fa5]+";
            final var pattern = Pattern.compile(regex);
            return pattern.matcher(text).matches();
        }
    }

    /**
     * Check the given characters is a valid mobile phone number.
     *
     * @param text Characters to be checked.
     * @return if true, text is a valid mobile phone number.
     */
    public static boolean isMobile(final String text) {
        if (isBlank(text)) {
            return true;
        } else {
            final var chars = new ArrayList<Character>();
            final var array = text.toCharArray();
            for (final var c : array) {
                chars.add(c);
            }
            return chars.stream().allMatch(Character::isDigit) && text.length() == 11;
        }
    }

}
