package com.pengsoft.system.service;

import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.system.domain.Message;
import com.pengsoft.system.repository.MessageRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link MessageService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class MessageServiceImpl extends EntityServiceImpl<MessageRepository, Message, String>
        implements MessageService {

}
