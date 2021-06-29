package com.pengsoft.support.exception;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * The default exception handler.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    private final List<ExceptionResponseEntityConverter> converters;

    public DefaultExceptionHandler(final List<ExceptionResponseEntityConverter> converters) {
        Collections.reverse(converters);
        this.converters = converters;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnexpectedException(final Exception e) {
        final var cause = ExceptionUtils.getRootCause(e);
        return converters.stream().filter(converter -> converter.support(cause)).findFirst()
                .map(converter -> converter.convert(cause))
                .orElseThrow(() -> new MissingConfigurationException("Not possible!"));
    }

}
