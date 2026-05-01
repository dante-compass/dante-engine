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

import org.dromara.dante.core.jackson.JacksonUtils;
import org.dromara.dante.message.emqx.domain.*;
import org.dromara.dante.message.emqx.event.*;
import org.springframework.context.ApplicationEvent;
import org.springframework.integration.transformer.AbstractPayloadTransformer;

import java.util.Map;

/**
 * <p>Description: Emqx Webhook Map 数据转换成 ApplicationEvent 转换器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/11/30 16:00
 */
public class WebhookMapToEventTransformer extends AbstractPayloadTransformer<Map<String, Object>, ApplicationEvent> {

    @Override
    protected ApplicationEvent transformPayload(Map<String, Object> payload) {
        Object event = payload.get("event");
        String type = String.valueOf(event);
        return process(type, payload);
    }

    /**
     * 将 Map 类型数据转换为指定类型的实体
     *
     * @param data      Map 类型数据
     * @param beanClass 实体类
     * @param <T>       实体类型
     * @return 指定的实体
     */
    private <T> T getBean(Map<String, Object> data, Class<T> beanClass) {
        return JacksonUtils.toObject(data, beanClass);
    }

    private ApplicationEvent process(String type, Map<String, Object> data) {
        return switch (type) {
            case "client.connack" -> new WebhookClientConnectAckEvent(getBean(data, WebhookClientConnectAck.class));
            case "client.connected" -> new WebhookClientConnectedEvent(getBean(data, WebhookClientConnected.class));
            case "client.disconnected" ->
                    new WebhookClientDisconnectedEvent(getBean(data, WebhookClientDisconnected.class));
            case "client.check_authn_complete" ->
                    new WebhookClientCheckAuthenticationCompleteEvent(getBean(data, WebhookClientCheckAuthenticationComplete.class));
            case "message.acked" -> new WebhookMessageAckedEvent(getBean(data, WebhookMessageAcked.class));
            case "message.delivered" -> new WebhookMessageDeliveredEvent(getBean(data, WebhookMessageDelivered.class));
            case "message.dropped" -> new WebhookMessageDroppedEvent(getBean(data, WebhookMessageDropped.class));
            case "session.subscribed" ->
                    new WebhookSessionSubscribedEvent(getBean(data, WebhookSessionSubscribed.class));
            case "session.unsubscribed" ->
                    new WebhookSessionUnsubscribedEvent(getBean(data, WebhookSessionUnsubscribed.class));
            default ->
                    new WebhookClientCheckAuthorizationCompleteEvent(getBean(data, WebhookClientCheckAuthorizationComplete.class));
        };
    }

    @Override
    public String getComponentType() {
        return this.getClass().getName();
    }
}
