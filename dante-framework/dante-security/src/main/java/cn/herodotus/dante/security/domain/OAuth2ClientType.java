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

package cn.herodotus.dante.security.domain;

import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * <p>Description: OAuth2 客户端类型 </p>
 * <p>
 * 主要用于客户端动态注册时，区分具体注册的来源，以便实现不同的处理逻辑。
 * <p>
 * 这里没有使用枚举定义，参考 Spring Authorization Server 中 OAuth2TokenFormat 的方式，以避免产生不可以预期的序列化问题。
 *
 * @author : gengwei_zheng
 * @date : 2026/9/3 16:15
 */
public class OAuth2ClientType implements Serializable {

    public static final OAuth2ClientType WEB = new OAuth2ClientType("web");
    public static final OAuth2ClientType NATIVE = new OAuth2ClientType("native");
    public static final OAuth2ClientType IOT = new OAuth2ClientType("iot");

    private final String value;

    public OAuth2ClientType(String value) {
        Assert.hasText(value, "value cannot be empty");
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        OAuth2ClientType that = (OAuth2ClientType) obj;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }
}
