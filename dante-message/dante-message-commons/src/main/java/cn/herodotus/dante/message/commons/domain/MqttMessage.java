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

package cn.herodotus.dante.message.commons.domain;

import cn.herodotus.dante.message.commons.constant.MqttConstants;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NullMarked;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: Mqtt 类型消息参数实体 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/11/2 16:05
 */
public class MqttMessage implements Message<String> {

    private String topic;
    private String payload;
    private Integer qos;
    private String responseTopic;
    private byte[] correlationData;
    private Map<String, Object> headers;

    public MqttMessage() {
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getQos() {
        return qos;
    }

    public void setQos(Integer qos) {
        this.qos = qos;
    }

    public String getResponseTopic() {
        return responseTopic;
    }

    public void setResponseTopic(String responseTopic) {
        this.responseTopic = responseTopic;
    }

    public byte[] getCorrelationData() {
        return correlationData;
    }

    public void setCorrelationData(byte[] correlationData) {
        this.correlationData = correlationData;
    }

    private void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    @Override
    @NullMarked
    public String getPayload() {
        return payload;
    }

    @Override
    @NullMarked
    public MessageHeaders getHeaders() {
        return new MessageHeaders(this.headers);
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public static Builder with(String topic, String payload) {
        return new Builder(topic, payload);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("topic", topic)
                .add("payload", payload)
                .add("qos", qos)
                .add("responseTopic", responseTopic)
                .toString();
    }

    public static class Builder {
        private final String topic;
        private final String payload;
        private Integer qos = 0;
        private String responseTopic;
        private byte[] correlationData;
        private final Map<String, Object> headers;

        protected Builder(String topic, String payload) {
            this.topic = topic;
            this.payload = payload;
            this.headers = new HashMap<>();
        }

        public Builder qos(Integer qos) {
            this.qos = qos;
            return this;
        }

        public Builder responseTopic(String responseTopic) {
            this.responseTopic = responseTopic;
            return this;
        }

        public Builder correlationData(byte[] correlationData) {
            this.correlationData = correlationData;
            return this;
        }

        public Builder header(String key, Object value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder headers(Map<String, Object> headers) {
            this.headers.putAll(headers);
            return this;
        }

        public MqttMessage build() {
            MqttMessage message = new MqttMessage();

            headers.put(MqttConstants.MESSAGE_HEADER__HERODOTUS_EVENT_ROUTER, MqttConstants.MESSAGE_ROUTER__TO_MQTT);

            message.setTopic(this.topic);
            headers.put(MqttConstants.TOPIC, this.topic);

            message.setPayload(payload);

            if (ObjectUtils.isNotEmpty(this.qos)) {
                message.setQos(this.qos);
                headers.put(MqttConstants.QOS, this.qos);
            }

            if (StringUtils.isNotBlank(this.responseTopic)) {
                message.setResponseTopic(responseTopic);
                headers.put(MqttConstants.RESPONSE_TOPIC, this.responseTopic);
            }

            if (ArrayUtils.isNotEmpty(this.correlationData)) {
                message.setCorrelationData(this.correlationData);
                headers.put(MqttConstants.CORRELATION_DATA, this.correlationData);
            }

            message.setHeaders(this.headers);

            return message;
        }
    }
}