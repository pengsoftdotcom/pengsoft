package com.pengsoft.system.exception;

import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;

/**
 * Invalid captcha exception
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class InvalidCaptchaException extends ClientAuthenticationException {

	private static final long serialVersionUID = -3854602592228855797L;

	public InvalidCaptchaException(String msg, Throwable t) {
        super(msg, t);
    }

    public InvalidCaptchaException(String msg) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "invalid_captcha";
    }

}
