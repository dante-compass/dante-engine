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

package cn.herodotus.dante.oauth2.authentication.consumer;

import cn.herodotus.dante.core.constant.SystemConstants;
import cn.herodotus.dante.oauth2.authentication.utils.OAuth2SettingUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.authentication.*;

import java.util.List;
import java.util.function.Consumer;

/**
 * <p>Description: 授权码模式认证 Provider 扩展 </p>
 *
 * @author : gengwei_zheng
 * @date : 2026/9/4 17:45
 */
public class OAuth2AuthorizationAuthenticationProviderConsumer implements Consumer<List<AuthenticationProvider>> {

    private final boolean supportResourceIndicators;

    public OAuth2AuthorizationAuthenticationProviderConsumer(boolean supportResourceIndicators) {
        this.supportResourceIndicators = supportResourceIndicators;
    }

    @Override
    public void accept(List<AuthenticationProvider> authenticationProviders) {
        authenticationProviders.forEach((authenticationProvider) -> {
            if (authenticationProvider instanceof OAuth2AuthorizationCodeRequestAuthenticationProvider provider) {
                if (!supportResourceIndicators) {
                    provider.setAuthenticationValidator(
                            OAuth2AuthorizationCodeRequestAuthenticationValidator.DEFAULT_REDIRECT_URI_VALIDATOR
                                    .andThen(OAuth2AuthorizationCodeRequestAuthenticationValidator.DEFAULT_SCOPE_VALIDATOR));
                } else {
                    provider.setAuthenticationValidator(
                            OAuth2AuthorizationCodeRequestAuthenticationValidator.DEFAULT_REDIRECT_URI_VALIDATOR
                                    .andThen(OAuth2AuthorizationCodeRequestAuthenticationValidator.DEFAULT_SCOPE_VALIDATOR)
                                    .andThen(new ResourceParameterValidator()));
                }
            }
        });
    }

    private static class ResourceParameterValidator implements Consumer<OAuth2AuthorizationCodeRequestAuthenticationContext> {

        @Override
        public void accept(OAuth2AuthorizationCodeRequestAuthenticationContext authenticationContext) {
            OAuth2AuthorizationCodeRequestAuthenticationToken authenticationToken =
                    authenticationContext.getAuthentication();

            if (authenticationToken.getScopes().contains(OidcScopes.OPENID)) {
                // resource parameter is not required for OpenID Connect flow
                return;
            }

            String resource = (String) authenticationToken.getAdditionalParameters().get(SystemConstants.PARAMETER__RESOURCE);

            if (OAuth2SettingUtils.unavailable(authenticationContext.getRegisteredClient().getClientSettings(), resource)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST);
                throw new OAuth2AuthorizationCodeRequestAuthenticationException(error, null);
            }
        }
    }
}
