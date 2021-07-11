package com.pengsoft.support.exception;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.StaleObjectStateException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Convert a {@link StaleObjectStateException} to a {@link ResponseEntity}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
public class StaleObjectStateExceptionResponseEntityConverter implements ExceptionResponseEntityConverter {
    @Inject
    private MessageSource messageSource;

    @Override
    public boolean support(Throwable cause) {
        return cause instanceof StaleObjectStateException;
    }

    @Override
    public ResponseEntity<Object> convert(Throwable cause) {
        final var message = messageSource.getMessage("staleo_bject", null, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(Map.of("error", "staleo_bject", "error_description", message),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
