package com.pengsoft.system.service;

import java.util.Optional;

import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.system.domain.MessageTemplate;
import com.pengsoft.system.repository.MessageTemplateRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link MessageTemplateService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class MessageTemplateServiceImpl extends EntityServiceImpl<MessageTemplateRepository, MessageTemplate, String>
        implements MessageTemplateService {

    @Override
    public Optional<MessageTemplate> findOneByCode(final String code) {
        return getRepository().findOneByCode(code);
    }

}
