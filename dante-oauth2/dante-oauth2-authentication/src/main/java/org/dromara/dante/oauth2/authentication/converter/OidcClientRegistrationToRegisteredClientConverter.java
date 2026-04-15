/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2020-2030 郑庚伟 ZHENGGENGWEI (码匠君), <herodotus@aliyun.com> Licensed under the AGPL License
 *
 * This file is part of Herodotus Stirrup.
 *
 * Herodotus Stirrup is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Herodotus Stirrup is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.herodotus.cn>.
 */

package org.dromara.dante.oauth2.authentication.converter;

import org.dromara.dante.core.constant.SymbolConstants;
import org.dromara.dante.core.constant.SystemConstants;
import org.dromara.dante.oauth2.authentication.utils.OAuth2EndpointUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.oidc.OidcClientRegistration;
import org.springframework.security.oauth2.server.authorization.oidc.converter.OidcClientRegistrationRegisteredClientConverter;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.util.List;

/**
 * <p>Description: OidcClientRegistration 转 RegisteredClient 转换器</p>
 *
 * @author : gengwei.zheng
 * @date : 2024/5/16 16:18
 */
public class OidcClientRegistrationToRegisteredClientConverter implements Converter<OidcClientRegistration, RegisteredClient> {

    private final List<String> clientMetadata;
    private final OidcClientRegistrationRegisteredClientConverter delegate;
    private final boolean isRemoteValidate;

    public OidcClientRegistrationToRegisteredClientConverter(List<String> clientMetadata, boolean isRemoteValidate) {
        this.clientMetadata = clientMetadata;
        this.isRemoteValidate = isRemoteValidate;
        this.delegate = new OidcClientRegistrationRegisteredClientConverter();
    }

    private OAuth2TokenFormat getTokenFormat() {
        if (isRemoteValidate) {
            return OAuth2TokenFormat.REFERENCE;
        } else {
            return OAuth2TokenFormat.SELF_CONTAINED;
        }
    }

    @Override
    public RegisteredClient convert(OidcClientRegistration oidcClientRegistration) {
        // 先使用 Spring Authorization Server 默认的 OidcClientRegistrationRegisteredClientConverter 将 OidcClientRegistration 转换为 RegisteredClient
        // 默认的 OidcClientRegistrationRegisteredClientConverter 减少转换错误
        RegisteredClient registeredClient = this.delegate.convert(oidcClientRegistration);

        // 默认的 OidcClientRegistrationRegisteredClientConverter 会设置一些默认值，不好进行修改，使用 from 重新生成一份 RegisteredClient.Builder 以便设定参数。
        RegisteredClient.Builder builder = RegisteredClient.from(registeredClient);

        // 自定义动态注册属性。
        ClientSettings.Builder clientSettingsBuilder = ClientSettings.withSettings(registeredClient.getClientSettings().getSettings());
        if (CollectionUtils.isNotEmpty(this.clientMetadata)) {
            // 检测是否有缺失的自定义参数，如果有则抛出错误
            this.clientMetadata.stream()
                    .filter(item -> !oidcClientRegistration.getClaims().containsKey(item))
                    .findAny()
                    .ifPresent(item -> OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, item));

            oidcClientRegistration.getClaims().forEach((claim, value) -> {
                if (this.clientMetadata.contains(claim)) {
                    // 自定义动态注册属性存入到客户端设置中
                    clientSettingsBuilder.setting(claim, value);

                    // 如果包含 ProductKey 同时 clientId 为空。那么就重新设置 clientId。物联网 clientId 格式为 {ProductKey}.{DeviceName}
                    if (Strings.CS.equals(claim, SystemConstants.PARAMETER__PRODUCT_KEY)) {
                        builder.clientId(value + SymbolConstants.PERIOD + oidcClientRegistration.getClientName());
                    }
                }
            });
        }

        builder.clientSettings(clientSettingsBuilder.build());

        builder.tokenSettings(TokenSettings.builder()
                .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
                .accessTokenFormat(getTokenFormat())
                .build());

        return builder.build();
    }
}
