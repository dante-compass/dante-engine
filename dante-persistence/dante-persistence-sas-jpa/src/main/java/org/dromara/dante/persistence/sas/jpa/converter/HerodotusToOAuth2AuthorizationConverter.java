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

import cn.hutool.v7.core.date.DateUtil;
import org.dromara.dante.oauth2.commons.utils.OAuth2AuthenticationUtils;
import org.dromara.dante.persistence.commons.converter.AbstractOAuth2EntityConverter;
import org.dromara.dante.persistence.commons.jackson.OAuth2JacksonProcessor;
import org.dromara.dante.persistence.sas.jpa.entity.HerodotusAuthorization;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.StringUtils;

/**
 * <p>Description: HerodotusAuthorization 转 OAuth2Authorization 转换器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/5/21 20:53
 */
public class HerodotusToOAuth2AuthorizationConverter extends AbstractOAuth2EntityConverter<HerodotusAuthorization, OAuth2Authorization> {

    private final RegisteredClientRepository registeredClientRepository;

    public HerodotusToOAuth2AuthorizationConverter(OAuth2JacksonProcessor jacksonProcessor, RegisteredClientRepository registeredClientRepository) {
        super(jacksonProcessor);
        this.registeredClientRepository = registeredClientRepository;
    }

    @Override
    public OAuth2Authorization convert(HerodotusAuthorization source) {
        RegisteredClient registeredClient = this.registeredClientRepository.findById(source.getRegisteredClientId());
        if (registeredClient == null) {
            throw new DataRetrievalFailureException(
                    "The RegisteredClient with id '" + source.getRegisteredClientId() + "' was not found in the RegisteredClientRepository.");
        }

        OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .id(source.getId())
                .principalName(source.getPrincipalName())
                .authorizationGrantType(OAuth2AuthenticationUtils.resolveAuthorizationGrantType(source.getAuthorizationGrantType()))
                .authorizedScopes(StringUtils.commaDelimitedListToSet(source.getAuthorizedScopes()))
                .attributes(attributes -> attributes.putAll(parseMap(source.getAttributes())));
        if (source.getState() != null) {
            builder.attribute(OAuth2ParameterNames.STATE, source.getState());
        }

        if (source.getAuthorizationCodeValue() != null) {
            OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(
                    source.getAuthorizationCodeValue(),
                    DateUtil.toInstant(source.getAuthorizationCodeIssuedAt()),
                    DateUtil.toInstant(source.getAuthorizationCodeExpiresAt()));
            builder.token(authorizationCode, metadata -> metadata.putAll(parseMap(source.getAuthorizationCodeMetadata())));
        }

        if (source.getAccessTokenValue() != null) {
            OAuth2AccessToken accessToken = new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER,
                    source.getAccessTokenValue(),
                    DateUtil.toInstant(source.getAccessTokenIssuedAt()),
                    DateUtil.toInstant(source.getAccessTokenExpiresAt()),
                    StringUtils.commaDelimitedListToSet(source.getAccessTokenScopes()));
            builder.token(accessToken, metadata -> metadata.putAll(parseMap(source.getAccessTokenMetadata())));
        }

        if (source.getRefreshTokenValue() != null) {
            OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(
                    source.getRefreshTokenValue(),
                    DateUtil.toInstant(source.getRefreshTokenIssuedAt()),
                    DateUtil.toInstant(source.getRefreshTokenExpiresAt()));
            builder.token(refreshToken, metadata -> metadata.putAll(parseMap(source.getRefreshTokenMetadata())));
        }

        if (source.getOidcIdTokenValue() != null) {
            OidcIdToken idToken = new OidcIdToken(
                    source.getOidcIdTokenValue(),
                    DateUtil.toInstant(source.getOidcIdTokenIssuedAt()),
                    DateUtil.toInstant(source.getOidcIdTokenExpiresAt()),
                    parseMap(source.getOidcIdTokenClaims()));
            builder.token(idToken, metadata -> metadata.putAll(parseMap(source.getOidcIdTokenMetadata())));
        }

        if (source.getUserCodeValue() != null) {
            OAuth2UserCode userCode = new OAuth2UserCode(
                    source.getUserCodeValue(),
                    DateUtil.toInstant(source.getUserCodeIssuedAt()),
                    DateUtil.toInstant(source.getUserCodeExpiresAt()));
            builder.token(userCode, metadata -> metadata.putAll(parseMap(source.getUserCodeMetadata())));
        }

        if (source.getDeviceCodeValue() != null) {
            OAuth2DeviceCode deviceCode = new OAuth2DeviceCode(
                    source.getDeviceCodeValue(),
                    DateUtil.toInstant(source.getDeviceCodeIssuedAt()),
                    DateUtil.toInstant(source.getDeviceCodeExpiresAt()));
            builder.token(deviceCode, metadata -> metadata.putAll(parseMap(source.getDeviceCodeMetadata())));
        }

        return builder.build();
    }
}
