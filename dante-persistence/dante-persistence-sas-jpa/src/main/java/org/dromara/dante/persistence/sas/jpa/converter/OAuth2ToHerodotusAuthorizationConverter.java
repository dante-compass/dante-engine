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

package org.dromara.dante.persistence.sas.jpa.converter;

import org.dromara.dante.core.constant.SymbolConstants;
import org.dromara.dante.persistence.commons.converter.AbstractOAuth2EntityConverter;
import org.dromara.dante.persistence.commons.jackson.OAuth2JacksonProcessor;
import org.dromara.dante.persistence.sas.jpa.entity.HerodotusAuthorization;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.util.StringUtils;

/**
 * <p>Description: OAuth2Authorization 转 HerodotusAuthorization 转换器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/5/21 20:57
 */
public class OAuth2ToHerodotusAuthorizationConverter extends AbstractOAuth2EntityConverter<OAuth2Authorization, HerodotusAuthorization> {

    public OAuth2ToHerodotusAuthorizationConverter(OAuth2JacksonProcessor jacksonProcessor) {
        super(jacksonProcessor);
    }

    @Override
    public HerodotusAuthorization convert(OAuth2Authorization source) {
        HerodotusAuthorization target = new HerodotusAuthorization();

        target.setId(source.getId());
        target.setRegisteredClientId(source.getRegisteredClientId());
        target.setPrincipalName(source.getPrincipalName());
        target.setAuthorizationGrantType(source.getAuthorizationGrantType().getValue());
        target.setAuthorizedScopes(StringUtils.collectionToDelimitedString(source.getAuthorizedScopes(), SymbolConstants.COMMA));
        target.setAttributes(writeMap(source.getAttributes()));
        target.setState(source.getAttribute(OAuth2ParameterNames.STATE));

        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = source.getToken(OAuth2AuthorizationCode.class);
        setTokenValues(
                authorizationCode,
                target::setAuthorizationCodeValue,
                target::setAuthorizationCodeIssuedAt,
                target::setAuthorizationCodeExpiresAt,
                target::setAuthorizationCodeMetadata
        );

        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = source.getToken(OAuth2AccessToken.class);
        setTokenValues(
                accessToken,
                target::setAccessTokenValue,
                target::setAccessTokenIssuedAt,
                target::setAccessTokenExpiresAt,
                target::setAccessTokenMetadata
        );
        if (accessToken != null) {
            if (accessToken.getToken().getScopes() != null) {
                target.setAccessTokenScopes(StringUtils.collectionToCommaDelimitedString(accessToken.getToken().getScopes()));
            }
            if (accessToken.getToken().getTokenType() != null) {
                target.setAccessTokenType(accessToken.getToken().getTokenType().getValue());
            }
        }

        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = source.getToken(OAuth2RefreshToken.class);
        setTokenValues(
                refreshToken,
                target::setRefreshTokenValue,
                target::setRefreshTokenIssuedAt,
                target::setRefreshTokenExpiresAt,
                target::setRefreshTokenMetadata
        );

        OAuth2Authorization.Token<OidcIdToken> oidcIdToken = source.getToken(OidcIdToken.class);
        setTokenValues(
                oidcIdToken,
                target::setOidcIdTokenValue,
                target::setOidcIdTokenIssuedAt,
                target::setOidcIdTokenExpiresAt,
                target::setOidcIdTokenMetadata
        );
        if (oidcIdToken != null) {
            target.setOidcIdTokenClaims(writeMap(oidcIdToken.getClaims()));
        }

        OAuth2Authorization.Token<OAuth2UserCode> userCode = source.getToken(OAuth2UserCode.class);
        setTokenValues(
                userCode,
                target::setUserCodeValue,
                target::setUserCodeIssuedAt,
                target::setUserCodeExpiresAt,
                target::setUserCodeMetadata
        );

        OAuth2Authorization.Token<OAuth2DeviceCode> deviceCode = source.getToken(OAuth2DeviceCode.class);
        setTokenValues(
                deviceCode,
                target::setDeviceCodeValue,
                target::setDeviceCodeIssuedAt,
                target::setDeviceCodeExpiresAt,
                target::setDeviceCodeMetadata
        );

        return target;
    }
}
