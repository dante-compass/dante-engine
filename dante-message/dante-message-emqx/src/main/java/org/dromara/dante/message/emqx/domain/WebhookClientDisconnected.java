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

package org.dromara.dante.message.emqx.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.dromara.dante.message.emqx.definition.domain.AbstractWebhookClient;
import org.dromara.dante.spring.jackson.TimestampToLocalDateTimeDeserializer;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>Description: 客户端连接断开事件 </p>
 * <p>
 * 客户端连接断开事件 ("$events/client_disconnected")。
 * 当客户端连接断开时触发规则。
 *
 * @author : gengwei.zheng
 * @date : 2023/10/6 22:16
 */
public class WebhookClientDisconnected extends AbstractWebhookClient {

    /**
     * 客户端连接断开原因：
     * normal：客户端主动断开
     * kicked：服务端踢出，通过 REST API
     * keepalive_timeout：keepalive 超时
     * not_authorized：认证失败，或者 acl_nomatch = disconnect 时没有权限的 Pub/Sub 会主动断开客户端
     * tcp_closed：对端关闭了网络连接
     * discarded: another client connected with the same ClientID and set clean_start = true
     * takenover: another client connected with the same ClientID and set clean_start = false
     * internal_error：畸形报文或其他未知错误
     */
    private String reason;

    /**
     * 客户端连接完成时间 (单位：毫秒)
     */
    @JsonDeserialize(using = TimestampToLocalDateTimeDeserializer.class)
    @JsonProperty("disconnected_at")
    private LocalDateTime disconnectedAt;

    /**
     * DISCONNECT Properties (仅适用于 MQTT 5.0)
     */
    @JsonProperty("disconn_props")
    private Map<String, Object> disconnects;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getDisconnectedAt() {
        return disconnectedAt;
    }

    public void setDisconnectedAt(LocalDateTime disconnectedAt) {
        this.disconnectedAt = disconnectedAt;
    }

    public Map<String, Object> getDisconnects() {
        return disconnects;
    }

    public void setDisconnects(Map<String, Object> disconnects) {
        this.disconnects = disconnects;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("reason", reason)
                .add("disconnectedAt", disconnectedAt)
                .toString();
    }
}
