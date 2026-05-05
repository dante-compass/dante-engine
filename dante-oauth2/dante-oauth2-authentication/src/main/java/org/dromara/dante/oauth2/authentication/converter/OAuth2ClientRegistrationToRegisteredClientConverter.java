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

package org.dromara.dante.oauth2.authentication.converter;

import org.springframework.security.oauth2.server.authorization.OAuth2ClientRegistration;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.converter.OAuth2ClientRegistrationRegisteredClientConverter;

import java.util.List;

/**
 * <p>Description: 自定义扩展的  {@link OAuth2ClientRegistration} 转 {@link RegisteredClient} 转换器抽象定义 </p>
 * <p>
 * 主要为了解决在客户端自动注册时，增加自定属性。例如：物联网模式下增加 ProductKey
 *
 * @author : gengwei_zheng
 * @date : 2026/4/27 18:00
 */
public class OAuth2ClientRegistrationToRegisteredClientConverter extends AbstractToRegisteredClientConverter<OAuth2ClientRegistration> {

    private final OAuth2ClientRegistrationRegisteredClientConverter delegate;

    public OAuth2ClientRegistrationToRegisteredClientConverter(List<String> clientMetadata, boolean isRemoteValidate) {
        super(clientMetadata, isRemoteValidate);
        this.delegate = new OAuth2ClientRegistrationRegisteredClientConverter();
    }

    @Override
    protected RegisteredClient convertToRegisteredClient(OAuth2ClientRegistration source) {
        // 先使用 Spring Authorization Server 默认的 OAuth2ClientRegistrationRegisteredClientConverter 将 OAuth2ClientRegistration 转换为 RegisteredClient
        // 使用默认的 OAuth2ClientRegistrationRegisteredClientConverter 减少转换错误
        return this.delegate.convert(source);
    }
}
