package com.pengsoft.support.exception;

/**
 * If this exception occurs, it means that some necessary data or configuration
 * is missing.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class MissingConfigurationException extends RuntimeException {

    private static final long serialVersionUID = 7049692874095706163L;

    public MissingConfigurationException(final String message) {
        super(message);
    }

}
