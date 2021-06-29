package com.pengsoft.support.exception;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Convert a {@link BusinessException} to a {@link ResponseEntity}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
public class BusinessExceptionResponseEntityConverter implements ExceptionResponseEntityConverter {

    @Inject
    private MessageSource messageSource;

    @Override
    public boolean support(final Throwable cause) {
        return cause instanceof BusinessException;
    }

    @Override
    public ResponseEntity<Object> convert(final Throwable cause) {
        final var e = (BusinessException) cause;
        final var code = e.getCode();
        final var args = e.getArgs();
        final var text = messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(Map.of("error", code, "error_description", text), HttpStatus.BAD_REQUEST);
    }

}
