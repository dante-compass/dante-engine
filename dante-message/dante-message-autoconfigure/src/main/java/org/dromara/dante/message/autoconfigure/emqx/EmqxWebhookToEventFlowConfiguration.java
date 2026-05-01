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

package org.dromara.dante.message.autoconfigure.emqx;

import jakarta.annotation.PostConstruct;
import org.dromara.dante.core.constant.SystemConstants;
import org.dromara.dante.message.commons.constant.Channels;
import org.dromara.dante.message.emqx.condition.ConditionalOnEventSource;
import org.dromara.dante.message.emqx.condition.EventSource;
import org.dromara.dante.message.emqx.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.event.outbound.ApplicationEventPublishingMessageHandler;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.MessageChannel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: Emqx Webhook Client相关事件消息转 ApplicationEvent Flow 配置 </p>
 * <p>
 * 通过 Emqx Webhook 方式获取到的 Emqx 客户端订阅事件消息。
 * $events/message_delivered - 消息投递事件 {@link WebhookMessageDeliveredEvent}
 * $events/message_acked - 消息确认事件 {@link WebhookMessageAckedEvent}
 * $events/message_dropped - 消息在转发的过程中被丢弃事件 {@link WebhookMessageDroppedEvent}
 * $events/delivery_dropped	- 消息在投递的过程中被丢弃事件 {@link WebhookDeliveryDroppedEvent}
 * $events/client_connected	- 客户端连接成功事件 {@link WebhookClientConnectedEvent}
 * $events/client_disconnected- 客户端连接断开事件 {@link WebhookClientDisconnectedEvent}
 * $events/client_connack - 连接确认事件 {@link WebhookClientConnectAckEvent}
 * $events/client_check_authz_complete - 鉴权完成事件 {@link WebhookClientCheckAuthenticationCompleteEvent}
 * $events/session_subscribed - 客户端订阅成功事件 {@link WebhookSessionSubscribedEvent}
 * $events/session_unsubscribed- 客户端取消订阅成功事件 {@link WebhookSessionUnsubscribedEvent}
 * <p>
 * 这些消息内容为 JSON 类型，将其转成对应的实体，然后将这些实体放入到对应的 ApplicationEvent 发送出去。
 * 相关应用代码，进需要监听具体的 ApplicationEvent 即可以完成相关的实现。进一步简化操作。
 *
 * @author : gengwei.zheng
 * @date : 2023/11/28 18:05
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnEventSource(EventSource.WEBHOOK)
public class EmqxWebhookToEventFlowConfiguration {

    private static final Logger log = LoggerFactory.getLogger(EmqxWebhookToEventFlowConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- Module [Emqx Webhook To Event Flow] Configure.");
    }

    /**
     * Emqx Webhook Http 入站通道
     *
     * @return {@link QueueChannel}
     */
    @Bean(Channels.EMQX_DEFAULT_WEBHOOK_INBOUND_CHANNEL)
    public MessageChannel emqxWebhookInboundChannel() {
        return MessageChannels.direct().getObject();
    }

    /**
     * Emqx Webhook 事件转成系统 Event Flow
     *
     * @param applicationEventPublishingMessageHandler {@link ApplicationEventPublishingMessageHandler}
     * @return {@link IntegrationFlow}
     */
    @Bean
    public IntegrationFlow emqxWebhookHttpToEventFlow(
            ApplicationEventPublishingMessageHandler applicationEventPublishingMessageHandler,
            @Qualifier(Channels.EMQX_DEFAULT_WEBHOOK_INBOUND_CHANNEL) MessageChannel emqxWebhookInboundChannel) {
        return IntegrationFlow.from(Http.inboundChannelAdapter(SystemConstants.WEBHOOK_EMQX_URI)
                        .requestMapping(m -> m.methods(HttpMethod.POST))
                        .requestPayloadType(ResolvableType.forClass(Map.class, LinkedHashMap.class))
                        .statusCodeFunction(s -> HttpStatus.OK))
                .channel(emqxWebhookInboundChannel)
                .transform(new WebhookMapToEventTransformer())
                .channel(MessageChannels.direct(Channels.EMQX_DEFAULT_EVENT_OUTBOUND_CHANNEL))
                .handle(applicationEventPublishingMessageHandler)
                .get();
    }
}
