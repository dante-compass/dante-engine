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

package org.dromara.dante.message.autoconfigure.event;

import jakarta.annotation.PostConstruct;
import org.dromara.dante.message.commons.constant.Channels;
import org.dromara.dante.message.commons.constant.MqttConstants;
import org.dromara.dante.message.commons.event.MqttMessageSendingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.event.inbound.ApplicationEventListeningMessageProducer;
import org.springframework.integration.event.outbound.ApplicationEventPublishingMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

/**
 * <p>Description: Event 发送自动配置 </p>
 * <p>
 * 接收指定类型的 {@link ApplicationEvent} 消息，将其转发至 {@link Channels#EVENT_ROUTING_CHANNEL} 通道中。
 * <p>
 * 接收到 {@link Channels#EVENT_ROUTING_CHANNEL} 通道中的消息后。根据 {@link Message} 中的 {@link MqttConstants#MESSAGE_HEADER__HERODOTUS_EVENT_ROUTER} Header 的值,将{@link Message} 转发至指定的通道中。
 *
 * @author : gengwei.zheng
 * @date : 2024/6/14 23:36
 */
@AutoConfiguration
public class IntegrationEventAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(IntegrationEventAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [Integration Event] Configure.");
    }

    /**
     * 统一定义的错误通道
     *
     * @return 直接通道 {@link org.springframework.integration.channel.DirectChannel}
     */
    @Bean(Channels.ERROR_CHANNEL)
    public MessageChannel errorChannel() {
        return MessageChannels.publishSubscribe().getObject();
    }

    /**
     * 统一订的事件路由通道，将事件路由到不同的通道
     *
     * @return 发布订阅通道 {@link PublishSubscribeChannel}
     */
    @Bean(Channels.EVENT_ROUTING_CHANNEL)
    public MessageChannel eventRoutingChannel() {
        return MessageChannels.publishSubscribe().getObject();
    }

    /**
     * 分发事件默认的输出通道
     *
     * @return 发布订阅通道 {@link PublishSubscribeChannel}
     */
    @Bean(name = Channels.EVENT__DEFAULT_OUTBOUND_CHANNEL)
    public MessageChannel eventDefaultOutboundChannel() {
        return MessageChannels.publishSubscribe().getObject();
    }

    /**
     * 统一的 Event 消息发送器。
     * <p>
     * 消息转换成 ApplicationEvent 配置都一样，所以定义一个统一处理器。
     *
     * @return {@link ApplicationEventPublishingMessageHandler}
     */
    @Bean
    public ApplicationEventPublishingMessageHandler applicationEventPublishingMessageHandler() {
        ApplicationEventPublishingMessageHandler handler = new ApplicationEventPublishingMessageHandler();
        // 设置该值的作用是将具体的 ApplicationEvent 作为 Payload，而不会将其包装成 MessageEvent
        handler.setPublishPayload(true);
        return handler;
    }

    /**
     * 接收指定类型的 {@link ApplicationEvent} 消息，将其转发至 {@link Channels#EVENT_ROUTING_CHANNEL} 通道中
     *
     * @param eventRoutingChannel 事件路由通道
     * @return {@link ApplicationEventListeningMessageProducer}
     */
    @Bean
    public ApplicationEventListeningMessageProducer applicationEventListeningMessageProducer(@Qualifier(Channels.EVENT_ROUTING_CHANNEL) MessageChannel eventRoutingChannel) {
        ApplicationEventListeningMessageProducer producer = new ApplicationEventListeningMessageProducer();
        producer.setEventTypes(MqttMessageSendingEvent.class);
        producer.setOutputChannel(eventRoutingChannel);
        return producer;
    }

    /**
     * 根据 {@link Message} 中的 {@link MqttConstants#MESSAGE_HEADER__HERODOTUS_EVENT_ROUTER} Header 的值,将{@link Message} 转发至指定的通道中。
     *
     * @return {@link IntegrationFlow}
     */
    @Bean
    public IntegrationFlow eventRoutingFlow() {
        log.debug("[Herodotus] |- [M4] Mail or Mqtt start to handle received message!");
        return IntegrationFlow.from(Channels.EVENT_ROUTING_CHANNEL)
                .route(Message.class,
                        message -> message.getHeaders().get(MqttConstants.MESSAGE_HEADER__HERODOTUS_EVENT_ROUTER, String.class),
                        e -> e
                                .channelMapping(MqttConstants.MESSAGE_ROUTER_TO_MAIL, Channels.MAIL__EVENT_INBOUND_CHANNEL)
                                .channelMapping(MqttConstants.MESSAGE_ROUTER_TO_MQTT, Channels.MQTT__DEFAULT_OUTBOUND_CHANNEL)
                )
                .get();
    }
}
