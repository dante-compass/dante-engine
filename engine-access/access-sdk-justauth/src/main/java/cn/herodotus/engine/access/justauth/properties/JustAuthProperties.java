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

package cn.herodotus.engine.access.justauth.properties;

import cn.herodotus.engine.access.core.constants.AccessConstants;
import com.google.common.base.MoreObjects;
import me.zhyd.oauth.config.AuthConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Map;

/**
 * <p>Description: 用于支持JustAuth第三方登录的配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/5/16 10:24
 */
@ConfigurationProperties(prefix = AccessConstants.PROPERTY_ACCESS_JUSTAUTH)
public class JustAuthProperties {

    /**
     * 是否开启
     */
    private Boolean enabled;
    /**
     * State 缓存时长，默认5分钟
     */
    private Duration timeout = Duration.ofMinutes(5);
    /**
     * 第三方系统登录配置信息
     */
    private Map<String, AuthConfig> configs;
    /**
     * 支付宝登录 PublicKey
     */
    private String alipayPublicKey;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    public Map<String, AuthConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<String, AuthConfig> configs) {
        this.configs = configs;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("enabled", enabled)
                .add("timeout", timeout)
                .add("alipayPublicKey", alipayPublicKey)
                .toString();
    }
}
