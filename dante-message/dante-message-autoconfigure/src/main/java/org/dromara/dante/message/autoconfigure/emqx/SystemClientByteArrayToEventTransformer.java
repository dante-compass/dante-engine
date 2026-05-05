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

import org.apache.commons.lang3.StringUtils;
import org.dromara.dante.core.constant.SymbolConstants;
import org.dromara.dante.core.jackson.JacksonUtils;
import org.dromara.dante.message.emqx.domain.SystemClientConnected;
import org.dromara.dante.message.emqx.domain.SystemClientDisconnected;
import org.dromara.dante.message.emqx.domain.SystemClientSubscribed;
import org.dromara.dante.message.emqx.domain.SystemClientUnsubscribed;
import org.dromara.dante.message.emqx.event.SystemClientConnectedEvent;
import org.dromara.dante.message.emqx.event.SystemClientDisconnectedEvent;
import org.dromara.dante.message.emqx.event.SystemClientSubscribedEvent;
import org.dromara.dante.message.emqx.event.SystemClientUnsubscribedEvent;
import org.dromara.dante.message.emqx.utils.EmqxMessageUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.messaging.Message;

import java.nio.charset.StandardCharsets;

/**
 * <p>Description: Emqx 系统主题消息转换成 Event 转换器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/12/9 14:35
 */
public class SystemClientByteArrayToEventTransformer extends AbstractTransformer {

    @Override
    protected Object doTransform(Message<?> message) {
        String topic = parseTopic(message);
        byte[] payload = EmqxMessageUtils.getPayload(message);

        return convert(topic, StringUtils.toEncodedString(payload, StandardCharsets.UTF_8));
    }

    /**
     * 将 String 类型数据转换为指定类型的实体
     *
     * @param data      String 类型数据
     * @param beanClass 实体类
     * @param <T>       实体类型
     * @return 指定的实体
     */
    private <T> T getBean(String data, Class<T> beanClass) {
        return JacksonUtils.toObject(data, beanClass);
    }

    /**
     * 解析 Emqx Message 中的 Topic 头信息
     *
     * @param message {@link Message}
     * @return emqx 系统主题 topic 名称
     */
    private String parseTopic(Message<?> message) {
        String topic = EmqxMessageUtils.getTopic(message);
        String[] elements = StringUtils.split(topic, SymbolConstants.FORWARD_SLASH);
        return elements[elements.length - 1];
    }

    private ApplicationEvent convert(String type, String data) {
        return switch (type) {
            case "connected" -> new SystemClientConnectedEvent(getBean(data, SystemClientConnected.class));
            case "disconnected" -> new SystemClientDisconnectedEvent(getBean(data, SystemClientDisconnected.class));
            case "subscribed" -> new SystemClientSubscribedEvent(getBean(data, SystemClientSubscribed.class));
            default -> new SystemClientUnsubscribedEvent(getBean(data, SystemClientUnsubscribed.class));
        };
    }

    @Override
    public String getComponentType() {
        return this.getClass().getName();
    }
}
