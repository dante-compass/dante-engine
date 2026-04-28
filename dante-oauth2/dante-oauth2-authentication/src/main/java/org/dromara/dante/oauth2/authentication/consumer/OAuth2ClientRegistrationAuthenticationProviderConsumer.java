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

package org.dromara.dante.oauth2.authentication.consumer;

import org.dromara.dante.core.constant.SystemConstants;
import org.dromara.dante.oauth2.authentication.converter.OAuth2ClientRegistrationToRegisteredClientConverter;
import org.dromara.dante.oauth2.authentication.converter.RegisteredClientToOAuth2ClientRegistrationConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.OAuth2ClientRegistration;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientRegistrationAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.List;
import java.util.function.Consumer;

/**
 * <p>Description: 客户端动态注册自定义属性 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/5/16 16:37
 */
public class OAuth2ClientRegistrationAuthenticationProviderConsumer implements Consumer<List<AuthenticationProvider>> {

    private static final List<String> clientMetadata = List.of(SystemConstants.PARAMETER__PRODUCT_KEY);
    private final boolean isRemoteValidate;

    public OAuth2ClientRegistrationAuthenticationProviderConsumer(boolean isRemoteValidate) {
        this.isRemoteValidate = isRemoteValidate;
    }

    @Override
    public void accept(List<AuthenticationProvider> authenticationProviders) {

        Converter<OAuth2ClientRegistration, RegisteredClient> toRegisteredClientConverter =
                new OAuth2ClientRegistrationToRegisteredClientConverter(clientMetadata, isRemoteValidate);
        Converter<RegisteredClient, OAuth2ClientRegistration> toOidcClientRegistrationConverter =
                new RegisteredClientToOAuth2ClientRegistrationConverter(clientMetadata);

        authenticationProviders.forEach((authenticationProvider) -> {
            if (authenticationProvider instanceof OAuth2ClientRegistrationAuthenticationProvider provider) {
                provider.setRegisteredClientConverter(toRegisteredClientConverter);
                provider.setClientRegistrationConverter(toOidcClientRegistrationConverter);
            }
        });

    }
}
