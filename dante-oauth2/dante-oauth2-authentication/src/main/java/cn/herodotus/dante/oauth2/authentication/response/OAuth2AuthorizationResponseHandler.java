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

package cn.herodotus.dante.oauth2.authentication.response;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.web.OAuth2AuthorizationEndpointFilter;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * <p>Description: OAuth2 认证 Response 扩展 </p>
 * <p>
 * 自定义 {@link AuthenticationSuccessHandler} 扩展，用以支持授权码模式可以返回 iss 参数。
 * 核心代码来自于 {@link OAuth2AuthorizationEndpointFilter} 中的方法 <code>sendAuthorizationResponse</code>。这也是 Spring Authorization Server 授权码模式响应的默认实现。
 *
 * @author : gengwei_zheng
 * @date : 2026/9/9 18:30
 */
public class OAuth2AuthorizationResponseHandler implements AuthenticationSuccessHandler {

    private final boolean supportAuthorizationResponseIssParameter;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public OAuth2AuthorizationResponseHandler(boolean supportAuthorizationResponseIssParameter) {
        this.supportAuthorizationResponseIssParameter = supportAuthorizationResponseIssParameter;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication = (OAuth2AuthorizationCodeRequestAuthenticationToken) authentication;
        String redirectUriForResponse = authorizationCodeRequestAuthentication.getRedirectUri();
        Assert.notNull(redirectUriForResponse, "redirectUri cannot be null");
        OAuth2AuthorizationCode authorizationCode = authorizationCodeRequestAuthentication.getAuthorizationCode();
        Assert.notNull(authorizationCode, "authorizationCode cannot be null");
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(redirectUriForResponse)
                .queryParam(OAuth2ParameterNames.CODE, authorizationCode.getTokenValue());
        if (StringUtils.hasText(authorizationCodeRequestAuthentication.getState())) {
            uriBuilder.queryParam(OAuth2ParameterNames.STATE,
                    UriUtils.encode(authorizationCodeRequestAuthentication.getState(), StandardCharsets.UTF_8));
        }

        if (supportAuthorizationResponseIssParameter) {
            AuthorizationServerSettings authorizationServerSettings = AuthorizationServerContextHolder.getContext().getAuthorizationServerSettings();
            String issuer = authorizationServerSettings.getIssuer();
            if (StringUtils.hasText(issuer)) {
                // 这里使用 JwtClaimNames.ISS 仅是因为常量值相同。Iss Parameter 与 Token 中的 Iss 有本质区别
                uriBuilder.queryParam(JwtClaimNames.ISS,
                        UriUtils.encode(issuer, StandardCharsets.UTF_8));
            }
        }

        // build(true) -> Components are explicitly encoded
        String redirectUri = uriBuilder.build(true).toUriString();
        this.redirectStrategy.sendRedirect(request, response, redirectUri);

    }
}
