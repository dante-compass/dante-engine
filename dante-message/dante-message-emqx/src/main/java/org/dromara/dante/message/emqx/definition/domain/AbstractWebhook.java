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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.dromara.dante.core.constant.SystemConstants;
import org.dromara.dante.spring.jackson.TimestampToLocalDateTimeDeserializer;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;

/**
 * <p>Description: Webhook 基础实体定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/10/6 21:40
 */
public abstract class AbstractWebhook extends AbstractEmqxDomain {

    /**
     * 事件触发时间 (单位：毫秒)
     */
    @JsonDeserialize(using = TimestampToLocalDateTimeDeserializer.class)
    @JsonProperty(SystemConstants.KEY__TIMESTAMP)
    private LocalDateTime timestamp;
    /**
     * 事件触发所在节点
     */
    private String node;
    /**
     * 具体事件类型
     */
    private String event;

    /**
     * 上线时间
     * <p>
     * 该类的子类中，只有 {@code org.dromara.dante.message.emqx.domain.WebhookClientConnected} 需要该属性，其它子类不需要。
     * 所以在此将其定义为 @JsonIgnore 避免不需要该属性的实体序列化时，包含该属性。
     *
     * @return 上线时间
     */
    @Override
    @JsonIgnore
    public LocalDateTime getConnectedAt() {
        return null;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .addValue(super.toString())
                .add("timestamp", timestamp)
                .add("node", node)
                .add("event", event)
                .toString();
    }
}
