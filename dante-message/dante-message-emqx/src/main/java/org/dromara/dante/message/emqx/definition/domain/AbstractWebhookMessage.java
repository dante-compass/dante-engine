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

package org.dromara.dante.message.emqx.definition.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.dromara.dante.spring.jackson.TimestampToLocalDateTimeDeserializer;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>Description: Message 事件通用属性 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/10/6 21:55
 */
public abstract class AbstractWebhookMessage extends AbstractWebhookEvent {

    /**
     * MQTT 消息 ID
     */
    private String id;
    /**
     * MQTT 消息体
     */
    private String payload;

    /**
     * MQTT 消息的 Flags
     */
    private Map<String, Object> flags;
    /**
     * PUBLISH Properties (仅适用于 MQTT 5.0)
     */
    @JsonProperty("pub_props")
    private Map<String, Object> publishes;
    /**
     * PUBLISH 消息到达 Broker 的时间 (单位：毫秒)
     */
    @JsonDeserialize(using = TimestampToLocalDateTimeDeserializer.class)
    @JsonProperty("publish_received_at")
    private LocalDateTime publishReceivedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Map<String, Object> getFlags() {
        return flags;
    }

    public void setFlags(Map<String, Object> flags) {
        this.flags = flags;
    }

    public Map<String, Object> getPublishes() {
        return publishes;
    }

    public void setPublishes(Map<String, Object> publishes) {
        this.publishes = publishes;
    }

    public LocalDateTime getPublishReceivedAt() {
        return publishReceivedAt;
    }

    public void setPublishReceivedAt(LocalDateTime publishReceivedAt) {
        this.publishReceivedAt = publishReceivedAt;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("payload", payload)
                .add("publishReceivedAt", publishReceivedAt)
                .toString();
    }
}
