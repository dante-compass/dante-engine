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

package org.dromara.dante.message.autoconfigure.mqtt;

import cn.hutool.v7.core.util.ByteUtil;
import jakarta.annotation.PostConstruct;
import org.dromara.dante.core.utils.ListUtils;
import org.dromara.dante.core.utils.NumberUtils;
import org.dromara.dante.message.autoconfigure.event.IntegrationEventAutoConfiguration;
import org.dromara.dante.message.autoconfigure.mqtt.gateway.MqttMessagingGateway;
import org.dromara.dante.message.commons.constant.Channels;
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

    @Bean(name = Channels.MQTT__DEFAULT_INBOUND_CHANNEL)
    public MessageChannel mqttDefaultInboundChannel() {
        return MessageChannels.publishSubscribe().getObject();
    }

    @Bean(name = Channels.MQTT__DEFAULT_OUTBOUND_CHANNEL)
    public MessageChannel mqttDefaultOutboundChannel() {
        return MessageChannels.direct().getObject();
    }

    @Bean
    public ClientManager<IMqttAsyncClient, MqttConnectionOptions> clientManager(MqttProperties mqttProperties) {
        MqttConnectionOptions options = new MqttConnectionOptions();
        options.setUserName(mqttProperties.getUsername());
        options.setPassword(ByteUtil.toBytes(mqttProperties.getPassword(), StandardCharsets.UTF_8));
        options.setCleanStart(mqttProperties.getCleanStart());
        options.setKeepAliveInterval(NumberUtils.longToInt(mqttProperties.getKeepAliveInterval().getSeconds()));
        options.setServerURIs(ListUtils.toStringArray(mqttProperties.getServerUrls()));
        options.setAutomaticReconnect(mqttProperties.getAutomaticReconnect());
        options.setSessionExpiryInterval(mqttProperties.getSessionExpiryInterval().getSeconds());
        options.setAutomaticReconnectDelay(
                NumberUtils.longToInt(mqttProperties.getAutomaticReconnectMinDelay().getSeconds()),
                NumberUtils.longToInt(mqttProperties.getAutomaticReconnectMaxDelay().getSeconds()));
        Mqttv5ClientManager clientManager = new Mqttv5ClientManager(options, mqttProperties.getClientId());
        clientManager.setPersistence(new MqttDefaultFilePersistence());

        log.trace("[Herodotus] |- Bean [Mqtt Connection Options] Configure.");
        return clientManager;
    }

    @Bean(name = "mqttDefaultInbound")
    public Mqttv5PahoMessageDrivenChannelAdapter mqttDefaultInbound(
            ClientManager<IMqttAsyncClient, MqttConnectionOptions> clientManager,
            MqttProperties mqttProperties,
            @Qualifier(Channels.MQTT__DEFAULT_INBOUND_CHANNEL) MessageChannel mqttDefaultInboundChannel) {
        Mqttv5PahoMessageDrivenChannelAdapter adapter = new Mqttv5PahoMessageDrivenChannelAdapter(clientManager, ListUtils.toStringArray(mqttProperties.getDefaultSubscribes()));
        adapter.setManualAcks(false);
        adapter.setOutputChannel(mqttDefaultInboundChannel);
        adapter.setErrorChannelName(IntegrationContextUtils.ERROR_CHANNEL_BEAN_NAME);
        log.trace("[Herodotus] |- Bean [Mqtt v5 Paho Message Driven Channel Adapter] Configure.");
        return adapter;
    }

    @Bean
    public IntegrationFlow mqttDefaultInboundFlow(
            @Qualifier("mqttDefaultInbound") Mqttv5PahoMessageDrivenChannelAdapter mqttDefaultInbound,
            ApplicationEventPublishingMessageHandler applicationEventPublishingMessageHandler,
            @Qualifier(Channels.EVENT__DEFAULT_OUTBOUND_CHANNEL) MessageChannel eventDefaultOutboundChannel) {
        return IntegrationFlow.from(mqttDefaultInbound)
                .transform(new DefaultMessageToEventTransformer())
                .channel(eventDefaultOutboundChannel)
                .handle(applicationEventPublishingMessageHandler)
                .get();
    }

    @Bean(name = "mqttDefaultOutbound")
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
