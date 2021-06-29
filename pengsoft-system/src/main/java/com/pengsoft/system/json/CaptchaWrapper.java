package com.pengsoft.system.json;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pengsoft.system.domain.Captcha;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This wrapper just for JSON serialization.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@JsonSerialize(using = CaptchaWrapperJsonSerializer.class)
@Getter
@RequiredArgsConstructor
public class CaptchaWrapper implements Serializable {

    private static final long serialVersionUID = 5958167478830762828L;

    private final Captcha captcha;

}
