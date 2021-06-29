package com.pengsoft.support.exception;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.extern.slf4j.Slf4j;

/**
 * Convert an unexpected exception to a {@link ResponseEntity}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Named
public class UnexpectedExceptionResponseEntityConverter implements ExceptionResponseEntityConverter {

    @Inject
    private MessageSource messageSource;

    @Override
    public boolean support(final Throwable cause) {
        return true;
    }

    @Override
    public ResponseEntity<Object> convert(final Throwable cause) {
        log.error("An unexpected exception occurred.", cause);
        final var message = messageSource.getMessage("unexpected", null, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(Map.of("error", "unexpected", "error_description", message),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
