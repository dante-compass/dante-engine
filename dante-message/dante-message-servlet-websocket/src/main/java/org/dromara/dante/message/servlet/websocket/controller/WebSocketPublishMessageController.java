/*
 * Copyright 2020-2030 码匠君<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 是 Dante Cloud 系统核心组件库，采用 APACHE LICENSE 2.0 开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1. 请不要删除和修改根目录下的LICENSE文件。
 * 2. 请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3. 请保留源码和相关描述文件的项目出处，作者声明等。
 * 4. 分发源码时候，请注明软件出处 <https://gitee.com/dromara/dante-cloud>
 * 5. 在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/dromara/dante-cloud>
 * 6. 若您的项目无法满足以上几点，可申请商业授权
 */

package org.dromara.dante.message.servlet.websocket.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.dante.message.core.constants.MessageConstants;
import org.dromara.dante.message.core.domain.DialogueMessage;
import org.dromara.dante.message.core.domain.WebSocketMessage;
import org.dromara.dante.message.core.event.SendDialogueMessageEvent;
import org.dromara.dante.message.servlet.websocket.definition.WebSocketMessageSender;
import org.dromara.dante.message.servlet.websocket.domain.WebSocketPrincipal;
import org.dromara.dante.spring.context.AbstractApplicationContextAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Description: 前端使用的 publish 响应接口 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/12/5 17:49
 */
@RestController
public class WebSocketPublishMessageController extends AbstractApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(WebSocketPublishMessageController.class);

    private final WebSocketMessageSender webSocketMessageSender;

    public WebSocketPublishMessageController(WebSocketMessageSender webSocketMessageSender) {
        this.webSocketMessageSender = webSocketMessageSender;
    }

    @MessageMapping("/public/notice")
    @SendTo(MessageConstants.WEBSOCKET_DESTINATION_BROADCAST_NOTICE)
    public String notice(String message, StompHeaderAccessor headerAccessor) {
        System.out.println("---message---" + message);
        if (ObjectUtils.isNotEmpty(headerAccessor)) {
            System.out.println("---id---" + headerAccessor.getUser().getName());
        }

        return message;
    }

    /**
     * 发送私信消息。
     *
     * @param detail         前端数据 {@link DialogueMessage}
     * @param headerAccessor 在WebSocketChannelInterceptor拦截器中绑定上的对象
     */
    @MessageMapping("/private/message")
    public void sendPrivateMessage(@Payload DialogueMessage detail, StompHeaderAccessor headerAccessor) {

        WebSocketMessage response = new WebSocketMessage();
        response.setUser(detail.getReceiverId());
        response.setDestination(MessageConstants.WEBSOCKET_DESTINATION_PERSONAL_MESSAGE);

        if (StringUtils.isNotBlank(detail.getReceiverId()) && StringUtils.isNotBlank(detail.getReceiverName())) {
            if (StringUtils.isBlank(detail.getSenderId()) && StringUtils.isBlank(detail.getSenderName())) {
                WebSocketPrincipal sender = (WebSocketPrincipal) headerAccessor.getUser();
                detail.setSenderId(sender.getUserId());
                detail.setSenderName(sender.getUsername());
                detail.setSenderAvatar(sender.getAvatar());
            }

            this.publishEvent(new SendDialogueMessageEvent(detail));

            response.setPayload("私信发送成功");
        } else {
            response.setPayload("私信发送失败，参数错误");
        }

        webSocketMessageSender.toUser(response);
    }
}
