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

package org.dromara.dante.message.commons.constant;

/**
 * <p>Description: 核心 Event 常量 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/6/15 22:30
 */
public interface MqttConstants {

    /**
     * 以下常量，从 spring-integration-mqtt 中的 MqttHeaders 中拷贝过来。主要为了解决减少依赖，以防引起不必要的注入或干扰
     */
    String PREFIX = "mqtt_";
    String QOS = "mqtt_qos";
    String ID = "mqtt_id";
    String RECEIVED_QOS = "mqtt_receivedQos";
    String DUPLICATE = "mqtt_duplicate";
    String RETAINED = "mqtt_retained";
    String RECEIVED_RETAINED = "mqtt_receivedRetained";
    String TOPIC = "mqtt_topic";
    String RECEIVED_TOPIC = "mqtt_receivedTopic";
    String MESSAGE_EXPIRY_INTERVAL = "mqtt_messageExpiryInterval";
    String TOPIC_ALIAS = "mqtt_topicAlias";
    String RESPONSE_TOPIC = "mqtt_responseTopic";
    String CORRELATION_DATA = "mqtt_correlationData";


    String MESSAGE_HEADER__HERODOTUS_EVENT_ROUTER = "herodotus-event-router";

    String MESSAGE_ROUTER_TO_MAIL = "mail-outbound";
    String MESSAGE_ROUTER_TO_MQTT = "mqtt-outbound";
}
