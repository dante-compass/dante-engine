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

package cn.herodotus.dante.oauth2.authentication.configurer;

import cn.herodotus.dante.oauth2.authentication.provider.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import cn.herodotus.dante.oauth2.authentication.provider.OAuth2SocialCredentialsAuthenticationProvider;
import cn.herodotus.dante.oauth2.authentication.utils.OAuth2ConfigurerUtils;
import cn.herodotus.dante.oauth2.commons.properties.OAuth2AuthenticationProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

/**
 * <p>Description: 自定义 AuthenticationProvider 配置器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/9/1 15:46
 */
public class OAuth2AuthenticationProviderConfigurer extends AbstractHttpConfigurer<OAuth2AuthenticationProviderConfigurer, HttpSecurity> {

    private final SessionRegistry sessionRegistry;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final OAuth2AuthenticationProperties authenticationProperties;

    public OAuth2AuthenticationProviderConfigurer(SessionRegistry sessionRegistry, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, OAuth2AuthenticationProperties authenticationProperties) {
        this.sessionRegistry = sessionRegistry;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.authenticationProperties = authenticationProperties;
    }

    @Override
    public void configure(HttpSecurity httpSecurity) {
        OAuth2AuthorizationService authorizationService = OAuth2ConfigurerUtils.getAuthorizationService(httpSecurity);
        OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator = OAuth2ConfigurerUtils.getTokenGenerator(httpSecurity);

        OAuth2ResourceOwnerPasswordAuthenticationProvider resourceOwnerPasswordAuthenticationProvider =
                new OAuth2ResourceOwnerPasswordAuthenticationProvider(authorizationService, tokenGenerator, userDetailsService, authenticationProperties);
        resourceOwnerPasswordAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        resourceOwnerPasswordAuthenticationProvider.setSessionRegistry(sessionRegistry);
        httpSecurity.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);

        OAuth2SocialCredentialsAuthenticationProvider socialCredentialsAuthenticationProvider =
                new OAuth2SocialCredentialsAuthenticationProvider(authorizationService, tokenGenerator, userDetailsService, authenticationProperties);
        socialCredentialsAuthenticationProvider.setSessionRegistry(sessionRegistry);
        httpSecurity.authenticationProvider(socialCredentialsAuthenticationProvider);
    }
}
