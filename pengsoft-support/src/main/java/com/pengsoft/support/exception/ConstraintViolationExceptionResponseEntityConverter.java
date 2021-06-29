package com.pengsoft.support.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Named;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.extern.slf4j.Slf4j;

/**
 * Convert a {@link ConstraintViolationException} to a {@link ResponseEntity}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Slf4j
@Named
public class ConstraintViolationExceptionResponseEntityConverter implements ExceptionResponseEntityConverter {

    private static final String PACKAGE_DELIMITER = ".";

    @Override
    public boolean support(final Throwable cause) {
        return cause instanceof ConstraintViolationException;
    }

    @Override
    public ResponseEntity<Object> convert(final Throwable cause) {
        final var e = (ConstraintViolationException) cause;
        final var body = e.getConstraintViolations().stream().collect(Collectors.toMap(cv -> {
            var propertyPath = cv.getPropertyPath().toString();
            if (propertyPath.contains(PACKAGE_DELIMITER)) {
                propertyPath = StringUtils.substringAfter(propertyPath, PACKAGE_DELIMITER);
                if (propertyPath.contains(PACKAGE_DELIMITER)) {
                    propertyPath = StringUtils.substringAfter(propertyPath, PACKAGE_DELIMITER);
                }
            }
            return propertyPath;
        }, cv -> List.of(cv.getMessage()), (final List<String> oldList, final List<String> newList) -> {
            final var result = new ArrayList<String>();
            result.addAll(oldList);
            result.addAll(newList);
            return result;
        }));
        log.debug("Validation failed.", e);
        return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
