package com.pengsoft.system.api;

import com.pengsoft.support.api.EntityApi;
import com.pengsoft.system.domain.MessageTemplate;
import com.pengsoft.system.service.MessageTemplateService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link MessageTemplate}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping("api/message-template")
public class MessageTemplateApi extends EntityApi<MessageTemplateService, MessageTemplate, String> {

}
