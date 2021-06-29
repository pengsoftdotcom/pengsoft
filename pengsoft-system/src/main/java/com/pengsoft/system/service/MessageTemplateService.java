package com.pengsoft.system.service;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import com.pengsoft.support.service.EntityService;
import com.pengsoft.system.domain.MessageTemplate;

/**
 * The service interface of {@link MessageTemplate}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface MessageTemplateService extends EntityService<MessageTemplate, String> {

    /**
     * Returns an {@link Optional} of a {@link MessageTemplate} with given code.
     *
     * @param code {@link MessageTemplate}'s code
     */
    Optional<MessageTemplate> findOneByCode(@NotBlank String code);

}
