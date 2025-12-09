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

package org.dromara.dante.assistant.oss.properties;

import org.dromara.dante.assistant.oss.constant.OssConstants;
import org.dromara.dante.core.domain.Pool;
import com.google.common.base.MoreObjects;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * <p>Description: 对象存储配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/21 9:55
 */
@ConfigurationProperties(prefix = OssConstants.PROPERTY_ASSISTANT_OSS)
public class OssProperties {

    /**
     * Oss Server endpoint
     */
    private String endpoint;

    /**
     * Oss Server accessKey
     */
    private String accessKey;

    /**
     * Oss Server secretKey
     */
    private String secretKey;

    /**
     * Oss Server region
     */
    private String region;

    /**
     * 对象存储预签名URL有效期，默认：5分钟
     */
    private Duration preSignedExpires = Duration.ofMinutes(5);

    /**
     * 自定义 OSS 对象池参数配置
     */
    private Pool pool = new Pool();

    /**
     * 预签名地址内部代理配置
     */
    private Proxy proxy = new Proxy();

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Duration getPreSignedExpires() {
        return preSignedExpires;
    }

    public void setPreSignedExpires(Duration preSignedExpires) {
        this.preSignedExpires = preSignedExpires;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public static class Proxy {
        /**
         * 是否开启。默认开水器
         */
        private Boolean enabled = true;

        /**
         * 代理请求转发源地址。例如：前端 <a href="http://localhost:3000">...</a>。注意如果有前端有配置代理需要加上
         */
        private String source;

        /**
         * 代理请求转发目的地址
         */
        private String destination;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("destination", destination)
                    .add("enabled", enabled)
                    .add("source", source)
                    .toString();
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("endpoint", endpoint)
                .add("accessKey", accessKey)
                .add("secretKey", secretKey)
                .add("region", region)
                .add("preSignedExpires", preSignedExpires)
                .add("pool", pool)
                .add("proxy", proxy)
                .toString();
    }
}
