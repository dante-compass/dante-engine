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
import org.dromara.dante.message.emqx.definition.domain.AbstractWebhookClientConnect;
import org.dromara.dante.spring.jackson.TimestampToLocalDateTimeDeserializer;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;

/**
 * <p>Description: 客户端连接成功事件 </p>
 * <p>
 * 客户端连接成功事件 ("$events/client_connected")。
 * 当客户端连接成功时触发规则。
 *
 * @author : gengwei.zheng
 * @date : 2023/10/6 22:06
 */
public class WebhookClientConnected extends AbstractWebhookClientConnect {

    /**
     * 主题挂载点(主题前缀)
     */
    @JsonProperty("mountpoint")
    private String mountPoint;
    /**
     * 是否为 MQTT bridge 连接
     */
    @JsonProperty("is_bridge")
    private Boolean bridge;
    /**
     * 客户端连接完成时间 (单位：毫秒)
     */
    @JsonDeserialize(using = TimestampToLocalDateTimeDeserializer.class)
    @JsonProperty("connected_at")
    private LocalDateTime connectedAt;

    @JsonProperty("receive_maximum")
    private Integer receiveMaximum;

    public String getMountPoint() {
        return mountPoint;
    }

    public void setMountPoint(String mountPoint) {
        this.mountPoint = mountPoint;
    }

    public Boolean getBridge() {
        return bridge;
    }

    public void setBridge(Boolean bridge) {
        this.bridge = bridge;
    }

    @Override
    public LocalDateTime getConnectedAt() {
        return connectedAt;
    }

    public void setConnectedAt(LocalDateTime connectedAt) {
        this.connectedAt = connectedAt;
    }

    public Integer getReceiveMaximum() {
        return receiveMaximum;
    }

    public void setReceiveMaximum(Integer receiveMaximum) {
        this.receiveMaximum = receiveMaximum;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("mountPoint", mountPoint)
                .add("bridge", bridge)
                .add("connectedAt", connectedAt)
                .add("receiveMaximum", receiveMaximum)
                .toString();
    }
}
