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

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.oidc.OidcClientRegistration;
import org.springframework.security.oauth2.server.authorization.oidc.converter.RegisteredClientOidcClientRegistrationConverter;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>Description: RegisteredClient 转 OidcClientRegistration 转换器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/5/16 16:29
 */
public class RegisteredClientToOidcClientRegistrationConverter implements Converter<RegisteredClient, OidcClientRegistration> {

    private final List<String> clientMetadata;
    private final RegisteredClientOidcClientRegistrationConverter delegate;

    public RegisteredClientToOidcClientRegistrationConverter(List<String> clientMetadata) {
        this.clientMetadata = clientMetadata;
        this.delegate = new RegisteredClientOidcClientRegistrationConverter();
    }

    @Override
    public OidcClientRegistration convert(RegisteredClient registeredClient) {
        OidcClientRegistration clientRegistration = this.delegate.convert(registeredClient);

        Map<String, Object> claims = new HashMap<>(clientRegistration.getClaims());
        if (CollectionUtils.isNotEmpty(this.clientMetadata)) {
            ClientSettings clientSettings = registeredClient.getClientSettings();
            claims.putAll(this.clientMetadata.stream()
                    .filter(metadata -> clientSettings.getSetting(metadata) != null)
                    .collect(Collectors.toMap(Function.identity(), clientSettings::getSetting)));
        }

        return OidcClientRegistration.withClaims(claims).build();
    }
}
