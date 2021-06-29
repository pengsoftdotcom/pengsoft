package com.pengsoft.system.service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import com.pengsoft.security.domain.User;
import com.pengsoft.support.exception.BusinessException;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.system.domain.Captcha;
import com.pengsoft.system.repository.CaptchaRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link CaptchaService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class CaptchaServiceImpl extends EntityServiceImpl<CaptchaRepository, Captcha, String>
        implements CaptchaService {

    private static final String EC_CAPTCHA_GENERATE_EXCEEDED = "captcha.generate.exceeded";

    private static final String EC_CAPTCHA_GENERATE_FORBIDDEN = "captcha.generate.forbidden";

    private static final int CAPTCHA_GENERATION_INTERVAL = 60;

    private static final int CAPTCHA_GENERATION_MAX_COUNT = 5;

    private static final int CAPTCHA_EXPIRATION = 300;

    @Override
    public Captcha generate(final User user) {
        final var captchas = getRepository().findAllByUserIdAndCreatedAtAfterOrderByCreatedAtDesc(user.getId(),
                DateUtils.currentDate().atStartOfDay());
        if (captchas.size() >= CAPTCHA_GENERATION_MAX_COUNT) {
            throw new BusinessException(EC_CAPTCHA_GENERATE_EXCEEDED, CAPTCHA_GENERATION_MAX_COUNT);
        }

        final var currentDateTime = DateUtils.currentDateTime();
        if (CollectionUtils.isNotEmpty(captchas)) {
            final var captcha = captchas.get(0);
            final var allowAt = captcha.getCreatedAt().plus(CAPTCHA_GENERATION_INTERVAL, ChronoUnit.SECONDS);
            if (allowAt.isAfter(currentDateTime)) {
                throw new BusinessException(EC_CAPTCHA_GENERATE_FORBIDDEN,
                        Duration.between(currentDateTime, allowAt).toSeconds());
            }
            if (captcha.getExpiredAt().isAfter(currentDateTime)) {
                captcha.setExpiredAt(DateUtils.currentDateTime().plus(CAPTCHA_EXPIRATION, ChronoUnit.SECONDS));
                return save(captcha);
            }
        }
        final var captcha = new Captcha();
        captcha.setUser(user);
        captcha.setCode(RandomStringUtils.randomNumeric(6));
        captcha.setExpiredAt(DateUtils.currentDateTime().plus(CAPTCHA_EXPIRATION, ChronoUnit.SECONDS));
        return save(captcha);
    }

    @Override
    public boolean isValid(final User user, final String code) {
        final var captchas = getRepository().findAllByUserIdAndCreatedAtAfterOrderByCreatedAtDesc(user.getId(),
                DateUtils.currentDate().atStartOfDay());
        if (CollectionUtils.isNotEmpty(captchas)) {
            final var captcha = captchas.get(0);
            if (captcha.getExpiredAt().isAfter(DateUtils.currentDateTime())
                    && StringUtils.equals(captcha.getCode(), code)) {
                delete(captchas);
                return true;
            }
        }
        return false;
    }

    @Override
    protected Sort getDefaultSort() {
        return Sort.by(Sort.Direction.DESC, "createdAt");
    }

}
