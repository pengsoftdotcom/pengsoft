package com.pengsoft.system.api;

import com.pengsoft.support.api.EntityApi;
import com.pengsoft.system.domain.Message;
import com.pengsoft.system.service.MessageService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link Message}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping("api/message")
public class MessageApi extends EntityApi<MessageService, Message, String> {

}
