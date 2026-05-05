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

package org.dromara.dante.message.autoconfigure.message;

import org.apache.commons.lang3.ObjectUtils;
import org.dromara.dante.core.jackson.JacksonUtils;
import org.dromara.dante.core.jackson.JsonNodeUtils;
import org.dromara.dante.message.commons.definition.Message;
import org.dromara.dante.message.commons.definition.enums.MessageCategory;
import org.dromara.dante.message.commons.domain.*;
import org.dromara.dante.message.commons.event.MqttMessageSendingEvent;
import org.dromara.dante.message.commons.event.StreamMessageSendingEvent;
import org.dromara.dante.message.commons.event.WebSocketBroadcastMessageSendingEvent;
import org.dromara.dante.message.commons.event.WebSocketUserMessageSendingEvent;
import org.dromara.dante.spring.context.AbstractApplicationContextAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;

import java.util.function.Supplier;

/**
 * <p>Description: 统一消息发送调度器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/10/25 21:06
 */
public class MessageSendingDispatcher extends AbstractApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(MessageSendingDispatcher.class);

    private static final TypeReference<MqttMessage> MQTT_MESSAGE_TYPE_REFERENCE = new TypeReference<>() {
    };
    private static final TypeReference<BroadcastMessage> BROADCAST_MESSAGE_TYPE_REFERENCE = new TypeReference<>() {
    };
    private static final TypeReference<UserMessage> USER_MESSAGE_TYPE_REFERENCE = new TypeReference<>() {
    };
    private static final TypeReference<StreamMessage> STREAM_MESSAGE_TYPE_REFERENCE = new TypeReference<>() {
    };

    public void process(Message<?> message) {
        MessageCategory category = message.getCategory();

        log.debug("[Herodotus] |- [M3] Dispatch received local message to event!");

        switch (category) {
            case MessageCategory.MQTT:
                sendMqtt(() -> (MqttMessage) message.getPayload());
                break;
            case MessageCategory.WEBSOCKET_BROADCAST:
                sendWebSocketBroadcast(() -> (BroadcastMessage) message.getPayload());
                break;
            case MessageCategory.WEBSOCKET_USER:
                sendWebSocketUser(() -> (UserMessage) message.getPayload());
                break;
            case MessageCategory.STREAM:
                sendStream(() -> (StreamMessage) message.getPayload());
                break;
            default:
                break;
        }
    }

    public void process(String data) {

        log.debug("[Herodotus] |- [M3] Dispatch received remote message to event!");

        JsonNode jsonNode = JacksonUtils.toNode(data);

        MessageCategory category = MessageCategory.valueOf(JsonNodeUtils.findStringValue(jsonNode, "category"));

        if (ObjectUtils.isNotEmpty(category)) {
            switch (category) {
                case MessageCategory.MQTT:
                    sendMqtt(() -> toObject(jsonNode, MQTT_MESSAGE_TYPE_REFERENCE));
                    break;
                case MessageCategory.WEBSOCKET_BROADCAST:
                    sendWebSocketBroadcast(() -> toObject(jsonNode, BROADCAST_MESSAGE_TYPE_REFERENCE));
                    break;
                case MessageCategory.WEBSOCKET_USER:
                    sendWebSocketUser(() -> toObject(jsonNode, USER_MESSAGE_TYPE_REFERENCE));
                    break;
                case MessageCategory.STREAM:
                    sendStream(() -> toObject(jsonNode, STREAM_MESSAGE_TYPE_REFERENCE));
                    break;
                default:
                    break;
            }
        }
    }

    private <T> T toObject(JsonNode jsonNode, TypeReference<T> valueTypeReference) {
        return JsonNodeUtils.findValue(jsonNode, "payload", valueTypeReference, JacksonUtils.getObjectMapper()._deserializationContext());
    }

    private void sendMqtt(Supplier<MqttMessage> supplier) {
        publishEvent(new MqttMessageSendingEvent(supplier.get()));
    }

    private void sendWebSocketBroadcast(Supplier<BroadcastMessage> supplier) {
        publishEvent(new WebSocketBroadcastMessageSendingEvent(supplier.get()));
    }

    private void sendWebSocketUser(Supplier<UserMessage> supplier) {
        publishEvent(new WebSocketUserMessageSendingEvent(supplier.get()));
    }

    private void sendStream(Supplier<StreamMessage> supplier) {
        publishEvent(new StreamMessageSendingEvent(supplier.get()));
    }
}
