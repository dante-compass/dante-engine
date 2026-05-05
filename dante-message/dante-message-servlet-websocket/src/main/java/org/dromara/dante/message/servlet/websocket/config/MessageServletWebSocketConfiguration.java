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

package org.dromara.dante.message.servlet.websocket.config;

import jakarta.annotation.PostConstruct;
import org.dromara.dante.cache.redis.utils.OnlineUserStat;
import org.dromara.dante.message.commons.condition.ConditionalOnInstanceMode;
import org.dromara.dante.message.commons.condition.InstanceMode;
import org.dromara.dante.message.servlet.websocket.definition.WebSocketMessageSender;
import org.dromara.dante.message.servlet.websocket.domain.StompWebSocketMessage;
import org.dromara.dante.message.servlet.websocket.interceptor.WebSocketAuthenticationHandshakeInterceptor;
import org.dromara.dante.message.servlet.websocket.listener.WebSocketBroadcastMessageListener;
import org.dromara.dante.message.servlet.websocket.listener.WebSocketConnectedListener;
import org.dromara.dante.message.servlet.websocket.listener.WebSocketDisconnectListener;
import org.dromara.dante.message.servlet.websocket.listener.WebSocketUserMessageListener;
import org.dromara.dante.message.servlet.websocket.sender.MultipleInstanceMessageSender;
import org.dromara.dante.message.servlet.websocket.sender.MultipleInstanceMessageSyncConsumer;
import org.dromara.dante.message.servlet.websocket.sender.SingleInstanceMessageSender;
import org.dromara.dante.message.servlet.websocket.sender.WebSocketMessagingTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;

import java.util.function.Consumer;

/**
 * <p>Description: WebSocket 处理器相关配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/12/29 15:52
 */
@Configuration(proxyBeanMethods = false)
public class MessageServletWebSocketConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MessageServletWebSocketConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- Module [Message Servlet WebSocket] Configure.");
    }

    @Bean
    public WebSocketAuthenticationHandshakeInterceptor webSocketPrincipalHandshakeHandler(ApplicationContext applicationContext) {
        WebSocketAuthenticationHandshakeInterceptor interceptor = new WebSocketAuthenticationHandshakeInterceptor(applicationContext);
        log.trace("[Herodotus] |- Bean [WebSocket Authentication Handshake Interceptor] Configure.");
        return interceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    public WebSocketMessagingTemplate webSocketMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate, SimpUserRegistry simpUserRegistry) {
        WebSocketMessagingTemplate template = new WebSocketMessagingTemplate(simpMessagingTemplate, simpUserRegistry);
        log.trace("[Herodotus] |- Bean [WebSocket Messaging Template] Configure.");
        return template;
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnInstanceMode(InstanceMode.SINGLE)
    static class SingleInstanceConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public WebSocketMessageSender singleInstanceMessageSender(WebSocketMessagingTemplate webSocketMessagingTemplate) {
            SingleInstanceMessageSender sender = new SingleInstanceMessageSender(webSocketMessagingTemplate);
            log.debug("[Herodotus] |- Strategy [Single Instance Web Socket Message Sender] Configure.");
            return sender;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnInstanceMode(InstanceMode.MULTIPLE)
    static class MultipleInstanceConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public WebSocketMessageSender multipleInstanceMessageSender(WebSocketMessagingTemplate webSocketMessagingTemplate) {
            MultipleInstanceMessageSender sender = new MultipleInstanceMessageSender(webSocketMessagingTemplate);
            log.debug("[Herodotus] |- Strategy [Multiple Instance Web Socket Message Sender] Configure.");
            return sender;
        }

        @Bean
        public Consumer<StompWebSocketMessage> webSocketConsumer(WebSocketMessagingTemplate webSocketMessagingTemplate) {
            MultipleInstanceMessageSyncConsumer consumer = new MultipleInstanceMessageSyncConsumer(webSocketMessagingTemplate);
            log.trace("[Herodotus] |- Bean [Multiple Instance Message Receiver] Configure.");
            return consumer;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @Import({
            WebSocketMessageBrokerConfiguration.class,
    })
    @ComponentScan(basePackages = {
            "org.dromara.dante.message.servlet.websocket.controller",
    })
    static class WebSocketConfiguration {

        @Bean
        public WebSocketConnectedListener webSocketConnectedListener(WebSocketMessageSender webSocketMessageSender) {
            WebSocketConnectedListener listener = new WebSocketConnectedListener(OnlineUserStat::connected, webSocketMessageSender::online);
            log.trace("[Herodotus] |- Bean [Principal Connected Listener] Configure.");
            return listener;
        }

        @Bean
        public WebSocketDisconnectListener webSocketDisconnectListener(WebSocketMessageSender webSocketMessageSender) {
            WebSocketDisconnectListener listener = new WebSocketDisconnectListener(OnlineUserStat::disconnected, webSocketMessageSender::online);
            log.trace("[Herodotus] |- Bean [Principal Disconnected Listener] Configure.");
            return listener;
        }

        @Bean
        public WebSocketBroadcastMessageListener webSocketBroadcastMessageListener(WebSocketMessageSender webSocketMessageSender) {
            WebSocketBroadcastMessageListener listener = new WebSocketBroadcastMessageListener(webSocketMessageSender);
            log.trace("[Herodotus] |- Bean [WebSocket Broadcast Message Listener] Configure.");
            return listener;
        }

        @Bean
        public WebSocketUserMessageListener webSocketUserMessageListener(WebSocketMessageSender webSocketMessageSender) {
            WebSocketUserMessageListener listener = new WebSocketUserMessageListener(webSocketMessageSender);
            log.trace("[Herodotus] |- Bean [WebSocket User Message Listener] Configure.");
            return listener;
        }
    }
}
