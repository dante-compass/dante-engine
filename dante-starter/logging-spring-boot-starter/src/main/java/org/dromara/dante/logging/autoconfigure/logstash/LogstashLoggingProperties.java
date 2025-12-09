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

package org.dromara.dante.logging.autoconfigure.logstash;

import ch.qos.logback.core.util.Duration;
import org.dromara.dante.core.constant.BaseConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>Description: 日志中心配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/2/5 17:48
 */
@ConfigurationProperties(prefix = BaseConstants.PROPERTY_LOG_LOGSTASH)
public class LogstashLoggingProperties {

    /**
     * 日志中心的 logstash 服务器主机。
     */
    private String host = "127.0.0.1";
    /**
     * 日志中心的 logstash 服务器端口。
     */
    private Integer port = 5044;
    /**
     * 保持活动持续时间，默认5分钟，单位：分钟
     */
    private Duration keepAliveDuration = Duration.buildByMinutes(5);

    /**
     * 尝试连接到目标间隔时间，默认30秒， 单位：秒
     */
    private Duration reconnectionDelay = Duration.buildBySeconds(30);
    /**
     * 日志写入超时时间，默认1分钟，单位：分钟
     */
    private Duration writeTimeout = Duration.buildByMinutes(1);
    /**
     * 是否开启 Logstash 日志
     */
    private Boolean enabled = Boolean.FALSE;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Duration getKeepAliveDuration() {
        return keepAliveDuration;
    }

    public void setKeepAliveDuration(Duration keepAliveDuration) {
        this.keepAliveDuration = keepAliveDuration;
    }

    public Duration getReconnectionDelay() {
        return reconnectionDelay;
    }

    public void setReconnectionDelay(Duration reconnectionDelay) {
        this.reconnectionDelay = reconnectionDelay;
    }

    public Duration getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(Duration writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
