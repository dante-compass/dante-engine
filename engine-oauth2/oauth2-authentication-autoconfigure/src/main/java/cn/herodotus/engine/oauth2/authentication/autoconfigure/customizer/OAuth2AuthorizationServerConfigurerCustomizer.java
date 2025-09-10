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

package cn.herodotus.engine.oauth2.authentication.autoconfigure.customizer;

import cn.herodotus.engine.core.definition.constant.SystemConstants;
import cn.herodotus.engine.core.identity.service.ClientDetailsService;
import cn.herodotus.engine.oauth2.authentication.configurer.OAuth2AuthenticationConfigurerManager;
import cn.herodotus.engine.oauth2.authentication.consumer.OAuth2TokenEndpointAuthenticationProviderConsumer;
import cn.herodotus.engine.oauth2.authentication.customizer.HerodotusOidcUserInfoMapper;
import cn.herodotus.engine.oauth2.authentication.provider.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import cn.herodotus.engine.oauth2.authentication.provider.OAuth2SocialCredentialsAuthenticationConverter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2DeviceCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.DelegatingAuthenticationConverter;

import java.util.Arrays;

/**
 * <p>Description: AuthorizationServerConfigurer 自定义配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/2/4 12:40
 */
public class OAuth2AuthorizationServerConfigurerCustomizer implements Customizer<OAuth2AuthorizationServerConfigurer> {

    private final HttpSecurity httpSecurity;
    private final SessionRegistry sessionRegistry;
    private final ClientDetailsService clientDetailsService;
    private final OAuth2AuthenticationConfigurerManager authenticationConfigurerManager;

    public OAuth2AuthorizationServerConfigurerCustomizer(HttpSecurity httpSecurity, SessionRegistry sessionRegistry, ClientDetailsService clientDetailsService, OAuth2AuthenticationConfigurerManager authenticationConfigurerManager) {
        this.httpSecurity = httpSecurity;
        this.sessionRegistry = sessionRegistry;
        this.clientDetailsService = clientDetailsService;
        this.authenticationConfigurerManager = authenticationConfigurerManager;
    }


    @Override
    public void customize(OAuth2AuthorizationServerConfigurer oauth2AuthorizationServerConfigurer) {

        oauth2AuthorizationServerConfigurer
                .clientAuthentication(endpoint -> endpoint.errorResponseHandler(authenticationConfigurerManager.getOAuth2AuthenticationFailureHandler()))
                .authorizationEndpoint(endpoint -> {
                    endpoint.errorResponseHandler(authenticationConfigurerManager.getOAuth2AuthenticationFailureHandler());
                    endpoint.consentPage(SystemConstants.OAUTH2_AUTHORIZATION_CONSENT_URI);
                })
                .deviceAuthorizationEndpoint(endpoint -> {
                    endpoint.errorResponseHandler(authenticationConfigurerManager.getOAuth2AuthenticationFailureHandler());
                    endpoint.verificationUri(SystemConstants.OAUTH2_DEVICE_ACTIVATION_URI);
                })
                .deviceVerificationEndpoint(endpoint -> {
                    endpoint.errorResponseHandler(authenticationConfigurerManager.getOAuth2AuthenticationFailureHandler());
                    endpoint.consentPage(SystemConstants.OAUTH2_AUTHORIZATION_CONSENT_URI);
                })
                .tokenEndpoint(endpoint -> {
                    AuthenticationConverter authenticationConverter = new DelegatingAuthenticationConverter(
                            Arrays.asList(
                                    new OAuth2AuthorizationCodeAuthenticationConverter(),
                                    new OAuth2RefreshTokenAuthenticationConverter(),
                                    new OAuth2ClientCredentialsAuthenticationConverter(),
                                    new OAuth2DeviceCodeAuthenticationConverter(),
                                    new OAuth2ResourceOwnerPasswordAuthenticationConverter(authenticationConfigurerManager.getHttpCryptoProcessor()),
                                    new OAuth2SocialCredentialsAuthenticationConverter(authenticationConfigurerManager.getHttpCryptoProcessor())
                            ));
                    endpoint.accessTokenRequestConverter(authenticationConverter);
                    endpoint.errorResponseHandler(authenticationConfigurerManager.getOAuth2AuthenticationFailureHandler());
                    endpoint.accessTokenResponseHandler(authenticationConfigurerManager.getOAuth2AccessTokenResponseHandler());
                    endpoint.authenticationProviders(new OAuth2TokenEndpointAuthenticationProviderConsumer(httpSecurity, sessionRegistry, clientDetailsService));
                })
                .tokenIntrospectionEndpoint(endpoint -> endpoint.errorResponseHandler(authenticationConfigurerManager.getOAuth2AuthenticationFailureHandler()))
                .tokenRevocationEndpoint(endpoint -> endpoint.errorResponseHandler(authenticationConfigurerManager.getOAuth2AuthenticationFailureHandler()))
                .oidc(oidc -> oidc.clientRegistrationEndpoint(endpoint -> {
                            endpoint.errorResponseHandler(authenticationConfigurerManager.getOAuth2AuthenticationFailureHandler());
                        })
                        .userInfoEndpoint(userInfo -> userInfo
                                .userInfoMapper(new HerodotusOidcUserInfoMapper())));
    }
}
