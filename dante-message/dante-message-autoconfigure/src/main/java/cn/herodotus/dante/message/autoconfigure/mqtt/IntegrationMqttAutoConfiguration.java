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

package cn.herodotus.dante.message.autoconfigure.mqtt;

import cn.herodotus.dante.core.utils.ListUtils;
import cn.herodotus.dante.core.utils.NumberUtils;
import cn.herodotus.dante.message.autoconfigure.event.IntegrationEventAutoConfiguration;
import cn.herodotus.dante.message.autoconfigure.mqtt.gateway.MqttMessagingGateway;
import cn.herodotus.dante.message.commons.constant.Channels;
import cn.hutool.v7.core.util.ByteUtil;
import jakarta.annotation.PostConstruct;
import org.eclipse.paho.mqttv5.client.IMqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MqttDefaultFilePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.context.IntegrationContextUtils;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.event.outbound.ApplicationEventPublishingMessageHandler;
import org.springframework.integration.mqtt.core.ClientManager;
import org.springframework.integration.mqtt.core.Mqttv5ClientManager;
import org.springframework.integration.mqtt.inbound.Mqttv5PahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.Mqttv5PahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.nio.charset.StandardCharsets;

/**
 * <p>Description: Mqtt Integration 自动配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/6/17 22:11
 */
@AutoConfiguration(after = IntegrationEventAutoConfiguration.class)
@ConditionalOnClass(ClientManager.class)
@EnableConfigurationProperties({MqttProperties.class})
@IntegrationComponentScan(basePackageClasses = MqttMessagingGateway.class)
public class IntegrationMqttAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(IntegrationMqttAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [Integration Mqtt] Configure.");
    }

    /**
     * Mqtt 默认消息出站通道。通过该种方式保证通道的唯一性。
     *
     * @return Mqtt 默认消息出站通道 {@link MessageChannel}
     */
    @Bean(name = Channels.MQTT__DEFAULT_OUTBOUND_CHANNEL)
    public MessageChannel mqttDefaultOutboundChannel() {
        return MessageChannels.direct().getObject();
    }

    /**
     * Mqtt 默认消息入站通道。通过该种方式保证通道的唯一性。
     *
     * @return Mqtt 默认消息入站通道 {@link MessageChannel}
     */
    @Bean(name = Channels.MQTT__DEFAULT_INBOUND_CHANNEL)
    public MessageChannel mqttDefaultInboundChannel() {
        return MessageChannels.direct().getObject();
    }

    /**
     * Mqtt 默认消息入站消息转 Event 通道。通过该种方式保证通道的唯一性。
     *
     * @return Mqtt 默认消息入站消息转 Event 通道 {@link MessageChannel}
     */
    @Bean(name = Channels.MQTT__DEFAULT_INBOUND_TO_EVENT_CHANNEL)
    public MessageChannel mqttDefaultInboundToEventChannel() {
        return MessageChannels.direct().getObject();
    }

    /**
     * Mqtt 客户端配置
     *
     * @param mqttProperties 配置属性 {@link MqttProperties}
     * @return Mqtt 客户端管理器 {@link ClientManager}
     */
    @Bean
    public ClientManager<IMqttAsyncClient, MqttConnectionOptions> clientManager(MqttProperties mqttProperties) {
        MqttConnectionOptions options = new MqttConnectionOptions();
        options.setCleanStart(mqttProperties.getCleanStart());
        options.setSessionExpiryInterval(mqttProperties.getSessionExpiryInterval().getSeconds());
        options.setKeepAliveInterval(NumberUtils.longToInt(mqttProperties.getKeepAliveInterval().getSeconds()));
        options.setAutomaticReconnect(mqttProperties.getAutomaticReconnect());
        options.setAutomaticReconnectDelay(
                NumberUtils.longToInt(mqttProperties.getAutomaticReconnectMinDelay().getSeconds()),
                NumberUtils.longToInt(mqttProperties.getAutomaticReconnectMaxDelay().getSeconds()));
        options.setServerURIs(ListUtils.toStringArray(mqttProperties.getServerUrls()));
        options.setUserName(mqttProperties.getUsername());
        options.setPassword(ByteUtil.toBytes(mqttProperties.getPassword(), StandardCharsets.UTF_8));
        options.setMaximumPacketSize(mqttProperties.getMaximumPacketSize());

        Mqttv5ClientManager clientManager = new Mqttv5ClientManager(options, mqttProperties.getClientId());
        clientManager.setPersistence(new MqttDefaultFilePersistence());

        log.trace("[Herodotus] |- Bean [Mqtt Connection Options] Configure.");
        return clientManager;
    }

    /**
     * Mqtt 默认的入站消息处理
     *
     * @param clientManager                            Mqtt 客户端管理器 {@link ClientManager}
     * @param mqttProperties                           配置属性 {@link MqttProperties}
     * @param applicationEventPublishingMessageHandler 发送 Event 消息处理器,见系统中统一定义的 {@link ApplicationEventPublishingMessageHandler}
     * @return 集成 Flow {@link IntegrationFlow}
     */
    @Bean
    public IntegrationFlow mqttDefaultInboundFlow(
            ClientManager<IMqttAsyncClient, MqttConnectionOptions> clientManager,
            MqttProperties mqttProperties,
            ApplicationEventPublishingMessageHandler applicationEventPublishingMessageHandler,
            @Qualifier(Channels.MQTT__DEFAULT_INBOUND_CHANNEL) MessageChannel mqttDefaultInboundChannel,
            @Qualifier(Channels.MQTT__DEFAULT_INBOUND_TO_EVENT_CHANNEL) MessageChannel mqttDefaultInboundToEventChannel) {

        // 接收 mqtt 指定 topic 中的消息
        Mqttv5PahoMessageDrivenChannelAdapter messageProducer = new Mqttv5PahoMessageDrivenChannelAdapter(clientManager, ListUtils.toStringArray(mqttProperties.getDefaultSubscribes()));
        messageProducer.setManualAcks(false);
        messageProducer.setOutputChannel(mqttDefaultInboundChannel);
        messageProducer.setErrorChannelName(IntegrationContextUtils.ERROR_CHANNEL_BEAN_NAME);
        log.trace("[Herodotus] |- Bean [Mqtt v5 Paho Message Driven Channel Adapter] Configure.");

        // 将 mqtt 指定 topic 中的消息，转换成 Event，通过 ApplicationEventPublishingMessageHandler 发送出去。
        // 这种实现主要用于解决 mqtt 消息接收的解耦
        return IntegrationFlow.from(messageProducer)
                .transform(new DefaultMessageToEventTransformer())
                .channel(mqttDefaultInboundToEventChannel)
                .handle(applicationEventPublishingMessageHandler)
                .get();
    }

    @Bean
    @ServiceActivator(inputChannel = Channels.MQTT__DEFAULT_OUTBOUND_CHANNEL)
    public MessageHandler mqttDefaultOutbound(ClientManager<IMqttAsyncClient, MqttConnectionOptions> clientManager, MqttProperties mqttProperties) {
        Mqttv5PahoMessageHandler handler = new Mqttv5PahoMessageHandler(clientManager);
        handler.setDefaultTopic(mqttProperties.getTopicHeader());
        handler.setDefaultQos(mqttProperties.getDefaultQos());
        handler.setTopicExpressionString(mqttProperties.getTopicExpression());
        handler.setAsync(true);
        handler.setAsyncEvents(true);

        log.trace("[Herodotus] |- Bean [Mqtt v5 Paho Message Handler] Configure.");
        return handler;
    }
}
