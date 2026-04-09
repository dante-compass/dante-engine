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

package org.dromara.dante.persistence.commons.definition;

import org.dromara.dante.core.constant.SystemConstants;
import org.dromara.dante.security.definition.AuthenticationManager;
import org.dromara.dante.security.domain.RegisteredClientTransmitter;
import org.dromara.dante.spring.context.ServiceContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>Description: OAuth2 认证管理 </p>
 * <p>
 * 增强的认证管理器定义。主要用于添加或删除 Spring Authorization Server 标准数据表 oauth2_registered_client 中的数据。
 * 通过这种方式，变相实现业务功能可以开启和关闭认证的功能
 *
 * @author : gengwei.zheng
 * @date : 2024/10/9 15:04
 */
public interface EnhanceAuthenticationManager extends AuthenticationManager {

    /**
     * 保存认证资源信息
     *
     * @param transmitter 手动创建 oauth2_registered_client 数据的必要信息 {@link RegisteredClientTransmitter}
     */

    void resource(RegisteredClientTransmitter transmitter);

    /**
     * 开启认证
     * <p>
     * 开启认证，即在 oauth2_registered_client 添加对应的数据
     *
     * @param transmitter 手动创建 oauth2_registered_client 数据的必要信息 {@link RegisteredClientTransmitter}
     */
    @Override
    default void enable(RegisteredClientTransmitter transmitter) {

        ClientSettings.Builder clientSettings = ClientSettings.builder();
        clientSettings.requireProofKey(false);
        clientSettings.requireAuthorizationConsent(true);
        clientSettings.tokenEndpointAuthenticationSigningAlgorithm(SignatureAlgorithm.RS256);

        Set<AuthorizationGrantType> authorizationGrantTypes = new HashSet<>(Set.of(AuthorizationGrantType.CLIENT_CREDENTIALS));

        if (transmitter.isRegistrationClient()) {
            clientSettings.setting(SystemConstants.PARAMETER__PRODUCT_KEY, transmitter.getParentClientId());
            authorizationGrantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
            authorizationGrantTypes.add(AuthorizationGrantType.DEVICE_CODE);
        }

        RegisteredClient registeredClient = RegisteredClient.withId(transmitter.getId())
                .clientId(transmitter.getClientId())
                .clientName(transmitter.getClientName())
                .clientSecret(transmitter.getClientSecret())
                .scope(StringUtils.collectionToCommaDelimitedString(List.of(SystemConstants.SCOPE_CLIENT_CREATE, SystemConstants.SCOPE_CLIENT_READ)))
                .authorizationGrantTypes((types) -> types.addAll(authorizationGrantTypes))
                .clientAuthenticationMethods((methods -> methods.addAll(Set.of(ClientAuthenticationMethod.CLIENT_SECRET_POST, ClientAuthenticationMethod.CLIENT_SECRET_BASIC))))
                .redirectUri(ServiceContextHolder.getIotServiceUri())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                        .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
                        .authorizationCodeTimeToLive(Duration.ofMinutes(5))
                        .accessTokenTimeToLive(Duration.ofMinutes(10))
                        .refreshTokenTimeToLive(Duration.ofHours(1))
                        .build())
                .clientSettings(clientSettings.build())
                .build();
        enable(registeredClient);
        resource(transmitter);
    }

    /**
     * 开启认证
     * <p>
     * 开启认证，即在 oauth2_registered_client 添加对应的数据
     *
     * @param registeredClient {@link RegisteredClient}
     */
    void enable(RegisteredClient registeredClient);
}
