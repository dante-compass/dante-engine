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

package org.dromara.dante.message.emqx.utils;

import org.apache.commons.lang3.Strings;
import org.dromara.dante.message.emqx.domain.SystemTopic;
import org.springframework.messaging.Message;

/**
 * <p>Description: Emqx 消息处理工具 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/11/22 12:21
 */
public class EmqxMessageUtils {

    /**
     * 获取 Emqx 消息主题
     *
     * @param message Spring Integration 消息对象 {@link Message}
     * @return mqtt_receivedTopic 消息头的值
     */
    public static String getTopic(Message<?> message) {
        Object mqttReceivedTopic = message.getHeaders().get("mqtt_receivedTopic");
        return String.valueOf(mqttReceivedTopic);
    }

    /**
     * 获取 Emqx 消息负载
     *
     * @param message Spring Integration 消息对象 {@link Message}
     * @return byte[] 类型消息内容
     */
    public static byte[] getPayload(Message<?> message) {
        return (byte[]) message.getPayload();
    }

    /**
     * 获取 Emqx 消息时间戳
     *
     * @param message Spring Integration 消息对象 {@link Message}
     * @return 发送消息的时间戳
     */
    public static Long getTimestamp(Message<?> message) {
        return message.getHeaders().getTimestamp();
    }

    /**
     * 当前主题是否是作为标记的主题
     *
     * @param topic Emqx 系统主题
     * @return true 是标记主题，false 不是标记主题
     */
    public static boolean isStampTopic(String topic) {
        return Strings.CS.equals(topic, "$SYS/brokers");
    }

    /**
     * 当前主题是否是作为标记的主题
     *
     * @param message Spring Integration 消息对象 {@link Message}
     * @return true 是标记主题，false 不是标记主题
     */
    public static boolean isStampTopic(Message<?> message) {
        String topic = getTopic(message);
        return isStampTopic(topic);
    }

    /**
     * 当前主题名称是否是版本信息
     *
     * @param systemTopic Emqx 系统主题 {@link SystemTopic}
     * @return true 是标记主题，false 不是标记主题
     */
    public static boolean isVersion(SystemTopic systemTopic) {
        return Strings.CS.equals(systemTopic.topicName(), "version");
    }
}
