package com.pengsoft.system.api;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;

import com.pengsoft.security.service.UserService;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.validation.Mobile;
import com.pengsoft.system.domain.Captcha;
import com.pengsoft.system.json.CaptchaWrapper;
import com.pengsoft.system.service.CaptchaService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link Captcha}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping("api/captcha")
public class CaptchaApi extends EntityApi<CaptchaService, Captcha, String> {

    @Inject
    private UserService userService;

    // @Messaging("authenticationCaptchaMessageBuilder")
    @PostMapping("generate")
    public CaptchaWrapper generate(@NotBlank @Mobile final String username) {
        final var user = userService.findOneByMobile(username)
                .orElseThrow(() -> getExceptions().constraintViolated("username", "not_exists"));
        return new CaptchaWrapper(getService().generate(user));
    }

}
