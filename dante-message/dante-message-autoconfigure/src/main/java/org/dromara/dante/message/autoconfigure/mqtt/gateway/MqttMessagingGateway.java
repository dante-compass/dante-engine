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

package org.dromara.dante.message.autoconfigure.mqtt.gateway;

import org.dromara.dante.message.commons.constant.Channels;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * <p>Description: Mqtt 消息发送网关定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/9/11 16:10
 */
@MessagingGateway(defaultRequestChannel = Channels.MQTT__DEFAULT_OUTBOUND_CHANNEL)
public interface MqttMessagingGateway {

    /**
     * 使用 默认 Topic 和 默认 Qos 发送数据
     *
     * @param payload string 类型内容
     */
    void publish(String payload);

    /**
     * 使用 默认 Topic 和 自定义 Qos 发送数据
     *
     * @param qos     Qos 等级
     * @param payload string 类型内容
     */
    void publish(@Header(MqttHeaders.QOS) Integer qos, String payload);

    /**
     * 使用 自定义 Topic 和 Qos 发送数据
     *
     * @param topic   主题
     * @param payload string 类型内容
     */
    void publish(@Header(MqttHeaders.TOPIC) String topic, String payload);

    /**
     * 使用 自定义 Topic 和 自定义 Qos 发送数据
     *
     * @param topic   主题
     * @param qos     Qos 等级
     * @param payload string 类型内容
     */
    void publish(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) Integer qos, String payload);

    /**
     * 使用 自定义 Topic 和 自定义 Qos 发送数据
     *
     * @param topic   主题
     * @param qos     Qos 等级
     * @param payload byte[] 类型内容
     */
    void publish(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) Integer qos, byte[] payload);

    /**
     * 发送请求响应的消息（MQTT v5新特性）
     * <p>
     * 所以为 PUBLISH 报文新增了一个 对比数据（Correlation Data） 属性。
     * <p>
     * 由于发布订阅模式本身的一些局限性，使用大于 0 的 QoS 也只能保证消息到达了对端而不是订阅端，如果发布消息时订阅端还未完成订阅，那么消息就会丢失，但发布方却无法得知。因此，对于一些投递要求比较严格的消息，可以通过请求响应来确认消息是否到达订阅端。
     *
     * @param topic           请求主题
     * @param responseTopic   响应主题
     * @param correlationData 对比数据
     * @param qos             Qos 等级
     * @param payload         内容
     */
    void publish(
            @Header(MqttHeaders.TOPIC) String topic,
            @Header(MqttHeaders.RESPONSE_TOPIC) String responseTopic,
            @Header(MqttHeaders.CORRELATION_DATA) String correlationData,
            @Header(MqttHeaders.QOS) Integer qos,
            String payload);
}
