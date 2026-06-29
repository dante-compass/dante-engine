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

package cn.herodotus.dante.message.autoconfigure.emqx;

import cn.herodotus.dante.message.commons.constant.Channels;
import cn.herodotus.dante.message.emqx.condition.ConditionalOnEventSource;
import cn.herodotus.dante.message.emqx.condition.EventSource;
import cn.herodotus.dante.message.emqx.event.SystemClientConnectedEvent;
import cn.herodotus.dante.message.emqx.event.SystemClientDisconnectedEvent;
import cn.herodotus.dante.message.emqx.event.SystemClientSubscribedEvent;
import cn.herodotus.dante.message.emqx.event.SystemClientUnsubscribedEvent;
import jakarta.annotation.PostConstruct;
import org.eclipse.paho.mqttv5.client.IMqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.context.IntegrationContextUtils;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.event.outbound.ApplicationEventPublishingMessageHandler;
import org.springframework.integration.mqtt.core.ClientManager;
import org.springframework.integration.mqtt.inbound.Mqttv5PahoMessageDrivenChannelAdapter;
import org.springframework.messaging.MessageChannel;

/**
 * <p>Description: Emqx 系统主题中 Client 相关监控内容转 ApplicationEvent 配置 </p>
 * <p>
 * EMQX 周期性发布自身运行状态、消息统计、客户端上下线事件到以 $SYS/ 开头系统主题。
 * <p>
 * SystemClient 主要对应 Emqx 客户端相关事件的系统主题。
 * · $SYS/brokers/${node}/clients/connected - 上线事件。当任意客户端上线时，EMQX 就会发布该主题的消息 {@link SystemClientConnectedEvent}
 * · $SYS/brokers/${node}/clients/disconnected - 上下线事件。当任意客户端下线时，EMQX 就会发布该主题的消息 {@link SystemClientDisconnectedEvent}
 * · $SYS/brokers/${node}/clients/subscribed - 订阅事件。当任意客户端订阅主题时，EMQX 就会发布该主题的消息 {@link SystemClientSubscribedEvent}
 * · $SYS/brokers/${node}/clients/unsubscribed - 取消订阅事件。当任意客户端取消订阅主题时，EMQX 就会发布该主题的消息 {@link SystemClientUnsubscribedEvent}
 * <p>
 * Emqx 6 主要对应 Emqx 客户端相关事件的系统主题变更为
 * · $SYS/brokers/${node}/clients/${clientid}/connected
 * · $SYS/brokers/${node}/clients/${clientid}/disconnected
 * · $SYS/brokers/${node}/clients/${clientid}/subscribed
 * · $SYS/brokers/${node}/clients/${clientid}/unsubscribed
 *
 * @author : gengwei.zheng
 * @date : 2023/12/5 16:02
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnEventSource(EventSource.SYS_TOPIC)
class EmqxSystemTopicToEventFlowConfiguration {

    private static final Logger log = LoggerFactory.getLogger(EmqxSystemTopicToEventFlowConfiguration.class);

    private static final String[] EMQX_MONITOR_TOPICS = new String[]{
            "$SYS/brokers/+/clients/+/connected",
            "$SYS/brokers/+/clients/+/disconnected"};

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- Module [Emqx $sys/client To Event Flow] Configure.");
    }

    /**
     * Emqx 默认系统主题入站通道。通过该种方式保证通道的唯一性。
     *
     * @return Emqx 默认系统主题入站通道 {@link MessageChannel}
     */
    @Bean(name = Channels.EMQX__DEFAULT_SYSTEM_TOPIC_INBOUND_CHANNEL)
    public MessageChannel emqxDefaultSystemTopicInboundChannel() {
        return MessageChannels.direct().getObject();
    }

    /**
     * Emqx 默认入站消息转 Event通道。通过该种方式保证通道的唯一性。
     *
     * @return Emqx 默认入站消息转 Event通道 {@link MessageChannel}
     */
    @Bean(name = Channels.EMQX__DEFAULT_INBOUND_TO_EVENT_CHANNEL)
    public MessageChannel emqxDefaultInboundToEventChannel() {
        return MessageChannels.direct().getObject();
    }

    @Bean
    public IntegrationFlow emqxSystemTopicToEventFlow(
            ClientManager<IMqttAsyncClient, MqttConnectionOptions> clientManager,
            ApplicationEventPublishingMessageHandler applicationEventPublishingMessageHandler,
            @Qualifier(Channels.EMQX__DEFAULT_SYSTEM_TOPIC_INBOUND_CHANNEL) MessageChannel emqxDefaultSystemTopicInboundChannel,
            @Qualifier(Channels.EMQX__DEFAULT_INBOUND_TO_EVENT_CHANNEL) MessageChannel emqxDefaultInboundToEventChannel) {
        Mqttv5PahoMessageDrivenChannelAdapter adapter = new Mqttv5PahoMessageDrivenChannelAdapter(clientManager, EMQX_MONITOR_TOPICS);
        adapter.setPayloadType(String.class);
        adapter.setManualAcks(false);
        adapter.setQos(0);
        adapter.setOutputChannel(emqxDefaultSystemTopicInboundChannel);
        adapter.setErrorChannelName(IntegrationContextUtils.ERROR_CHANNEL_BEAN_NAME);

        return IntegrationFlow.from(adapter)
                .transform(new SystemClientByteArrayToEventTransformer())
                .channel(emqxDefaultInboundToEventChannel)
                .handle(applicationEventPublishingMessageHandler)
                .get();
    }
}
