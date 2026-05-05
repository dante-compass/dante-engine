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
 * <p>Description: 消息通道定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/11/22 18:49
 */
public interface Channels {

    /**
     * 统一的错误通道定义
     */
    String ERROR_CHANNEL = "errorChannel";
    /**
     * Application Event 路由通道
     */
    String EVENT_ROUTING_CHANNEL = "eventRoutingChannel";

    /**
     * EVENT 类型出站通道
     */
    String EVENT__DEFAULT_OUTBOUND_CHANNEL = "eventDefaultOutboundChannel";

    /**
     * Mail 系统接收 Event 通道
     */
    String MAIL__EVENT_INBOUND_CHANNEL = "mailEventInboundChannel";

    /**
     * 默认的发送 Mail 通道
     */
    String MAIL__DEFAULT_SENDING_CHANNEL = "mailDefaultSendingChannel";

    /**
     * Mqtt 默认入站通道
     */
    String MQTT__DEFAULT_INBOUND_CHANNEL = "mqttDefaultInboundChannel";
    /**
     * Mqtt 默认出站通道
     */
    String MQTT__DEFAULT_OUTBOUND_CHANNEL = "mqttDefaultOutboundChannel";
    /**
     * Mqtt 默认入站通道
     */
    String MQTT__THINGS_BRAIN_INBOUND_CHANNEL = "mqttThingBrainInboundChannel";

    /**
     * Emqx 默认的监控指标数据数据 Mqtt 类型入站通道
     */
    String EMQX_DEFAULT_SYSTEM_TOPIC_INBOUND_CHANNEL = "emqxDefaultSystemTopicInboundChannel";
    /**
     * Emqx 默认的 Webhook 数据 HTTP 类型入站通道
     */
    String EMQX_DEFAULT_WEBHOOK_INBOUND_CHANNEL = "emqxDefaultWebhookInboundChannel";
    /**
     * Emqx 默认的系统时间数据 EVENT 类型出站通道
     */
    String EMQX_DEFAULT_EVENT_OUTBOUND_CHANNEL = "emqxDefaultEventOutboundChannel";
}
