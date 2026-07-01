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

package cn.herodotus.dante.message.autoconfigure.event;

import cn.herodotus.dante.message.commons.constant.Channels;
import cn.herodotus.dante.message.commons.constant.MqttConstants;
import cn.herodotus.dante.message.commons.event.MqttMessageSendingEvent;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.context.IntegrationContextUtils;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.event.inbound.ApplicationEventListeningMessageProducer;
import org.springframework.integration.event.outbound.ApplicationEventPublishingMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

/**
 * <p>Description: Event 发送自动配置 </p>
 * <p>
 * 接收指定类型的 {@link ApplicationEvent} 消息，将其转发至 {@link Channels#EVENT__ROUTING_CHANNEL} 通道中。
 * <p>
 * 接收到 {@link Channels#EVENT__ROUTING_CHANNEL} 通道中的消息后。根据 {@link Message} 中的 {@link MqttConstants#MESSAGE_HEADER__HERODOTUS_EVENT_ROUTER} Header 的值,将{@link Message} 转发至指定的通道中。
 *
 * @author : gengwei.zheng
 * @date : 2024/6/14 23:36
 */
@AutoConfiguration
@EnableIntegration
public class IntegrationEventAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(IntegrationEventAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [Integration Event] Configure.");
    }

    /**
     * 整个系统中统一定义的错误通道
     *
     * @return 错误通道 {@link MessageChannel}
     */
    @Bean(IntegrationContextUtils.ERROR_CHANNEL_BEAN_NAME)
    public MessageChannel errorChannel() {
        return MessageChannels.publishSubscribe().getObject();
    }

    /**
     * 以 Bean 的形式定义 Event 路由通道。通过该种方式保证通道的唯一性。
     *
     * @return Event 路由通道 {@link MessageChannel}
     */
    @Bean(Channels.EVENT__ROUTING_CHANNEL)
    public MessageChannel eventRoutingChannel() {
        return MessageChannels.direct().getObject();
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
     * 接收指定类型的 {@link ApplicationEvent} 消息，将其转发至 {@link Channels#EVENT__ROUTING_CHANNEL} 通道中
     * <p>
     * 该配置主要用于系统的统一消息发送。因为不同的类型的消息，发送逻辑不同，所以目前只处理 {@link MqttMessageSendingEvent} 两种事件。
     *
     * @return {@link ApplicationEventListeningMessageProducer}
     */
    @Bean
    public ApplicationEventListeningMessageProducer applicationEventListeningMessageProducer(@Qualifier(Channels.EVENT__ROUTING_CHANNEL) MessageChannel eventRoutingChannel) {
        ApplicationEventListeningMessageProducer producer = new ApplicationEventListeningMessageProducer();
        producer.setEventTypes(MqttMessageSendingEvent.class);
        producer.setOutputChannel(eventRoutingChannel);
        producer.setErrorChannelName(IntegrationContextUtils.ERROR_CHANNEL_BEAN_NAME);
        return producer;
    }

    /**
     * 接收 {@link Channels#EVENT__ROUTING_CHANNEL} 通道中的 {@link Message}，
     * 根据 {@link Message} 中的 {@link MqttConstants#MESSAGE_HEADER__HERODOTUS_EVENT_ROUTER} Header 的值,将 {@link Message} 转发至指定的通道中,由不同的终端发送对应通道中的消息
     *
     * @return {@link IntegrationFlow}
     */
    @Bean
    public IntegrationFlow eventRoutingFlow(@Qualifier(Channels.EVENT__ROUTING_CHANNEL) MessageChannel eventRoutingChannel) {
        log.debug("[Herodotus] |- [UM4] Mail or Mqtt start to handle received message!");
        return IntegrationFlow.from(eventRoutingChannel)
                .route(Message.class,
                        message -> message.getHeaders().get(MqttConstants.MESSAGE_HEADER__HERODOTUS_EVENT_ROUTER, String.class),
                        spec -> spec
                                // 这里接收到的 mail 消息，是用于本系统自身内部，所以使用 mail inbound 通道
                                .channelMapping(MqttConstants.MESSAGE_ROUTER__TO_MAIL, Channels.MAIL__DEFAULT_INBOUND_CHANNEL)
                                // 这里接收到的 mqtt 消息，发送到 mqtt broker 的消息，是像系统以外发送的，所以使用 mqtt outbound 通道
                                .channelMapping(MqttConstants.MESSAGE_ROUTER__TO_MQTT, Channels.MQTT__DEFAULT_OUTBOUND_CHANNEL)
                )
                .get();
    }
}
