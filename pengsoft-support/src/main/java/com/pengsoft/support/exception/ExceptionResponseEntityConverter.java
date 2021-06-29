package com.pengsoft.support.exception;

import org.springframework.http.ResponseEntity;

/**
 * Convert an {@link Exception} to a {@link ResponseEntity}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface ExceptionResponseEntityConverter {

    boolean support(final Throwable cause);

    ResponseEntity<Object> convert(Throwable cause);

}
