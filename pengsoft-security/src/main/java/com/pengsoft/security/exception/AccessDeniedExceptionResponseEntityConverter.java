package com.pengsoft.security.exception;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import com.pengsoft.support.exception.ExceptionResponseEntityConverter;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

/**
 * Convert a {@link AccessDeniedException} to a {@link ResponseEntity}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
public class AccessDeniedExceptionResponseEntityConverter implements ExceptionResponseEntityConverter {

    @Inject
    private MessageSource messageSource;

    @Override
    public boolean support(final Throwable cause) {
        return cause instanceof AccessDeniedException;
    }

    @Override
    public ResponseEntity<Object> convert(final Throwable cause) {
        final var message = messageSource.getMessage("access_denied", new String[] { cause.getMessage() },
                LocaleContextHolder.getLocale());
        return new ResponseEntity<>(Map.of("error", "access_denied", "error_description", message),
                HttpStatus.FORBIDDEN);
    }

}
