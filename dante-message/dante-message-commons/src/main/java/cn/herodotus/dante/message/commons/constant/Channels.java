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

package cn.herodotus.dante.message.commons.constant;

/**
 * <p>Description: 消息通道定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/11/22 18:49
 */
public interface Channels {

    /**
     * Application Event 路由通道
     */
    String EVENT__ROUTING_CHANNEL = "eventRoutingChannel";

    /**
     * Mail 系统接收消息默认通道，目前通过 Event 进行接收
     */
    String MAIL__DEFAULT_INBOUND_CHANNEL = "mailDefaultInboundChannel";

    /**
     * Mail 系统发送消息默认通道
     */
    String MAIL__DEFAULT_OUTBOUND_CHANNEL = "mailDefaultOutboundChannel";

    /**
     * Mqtt 默认入站通道
     */
    String MQTT__DEFAULT_INBOUND_CHANNEL = "mqttDefaultInboundChannel";

    /**
     * Mqtt 默认入站通道
     */
    String MQTT__DEFAULT_INBOUND_TO_EVENT_CHANNEL = "mqttDefaultInboundToEventChannel";

    /**
     * Mqtt 默认出站通道
     */
    String MQTT__DEFAULT_OUTBOUND_CHANNEL = "mqttDefaultOutboundChannel";

    /**
     * Mqtt 默认入站通道
     */
    String MQTT__THINGSMESH_INBOUND_CHANNEL = "mqttThingsMeshInboundChannel";

    /**
     * Emqx 默认的监控指标数据数据 Mqtt 类型入站通道
     */
    String EMQX__DEFAULT_SYSTEM_TOPIC_INBOUND_CHANNEL = "emqxDefaultSystemTopicInboundChannel";
    /**
     * Emqx 默认的 Webhook 数据 HTTP 类型入站通道
     */
    String EMQX__DEFAULT_WEBHOOK_INBOUND_CHANNEL = "emqxDefaultWebhookInboundChannel";
    /**
     * Emqx 默认的系统主题入站消息转 EVENT 通道
     */
    String EMQX__DEFAULT_INBOUND_TO_EVENT_CHANNEL = "emqxDefaultInboundToEventChannel";

    /**
     * RSocket 消息默认 InBound Channel。当前定位主要用于基于 RSocket 的实现消息系统使用。
     */
    String RSOCKET__DEFAULT_INBOUND_CHANNEL = "rsocketDefaultInboundChannel";
    /**
     * RSocket OutBound Gateway。处理自定义 Message Gateway 消息通道。当前定位主要用于基于 RSocket 的实现消息发送使用。
     */
    String RSOCKET__GATEWAY_REQUEST_CHANNEL = "rsocketGatewayRequestChannel";
    /**
     * RSocket OutBound Gateway。处理自定义 Message Gateway 消息通道。当前定位主要用于基于 RSocket 的实现消息发送使用。
     */
    String RSOCKET__GATEWAY_RESPONSE_CHANNEL = "rsocketGatewayResponseChannel";
}
