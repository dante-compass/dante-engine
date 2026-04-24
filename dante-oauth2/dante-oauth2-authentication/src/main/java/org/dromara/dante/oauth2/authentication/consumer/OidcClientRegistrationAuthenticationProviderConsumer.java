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

package org.dromara.dante.oauth2.authentication.consumer;

import org.dromara.dante.core.constant.SystemConstants;
import org.dromara.dante.oauth2.authentication.converter.OidcClientRegistrationToRegisteredClientConverter;
import org.dromara.dante.oauth2.authentication.converter.RegisteredClientToOidcClientRegistrationConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.oidc.OidcClientRegistration;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcClientConfigurationAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcClientRegistrationAuthenticationProvider;

import java.util.List;
import java.util.function.Consumer;

/**
 * <p>Description: 客户端动态注册自定义属性 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/5/16 16:37
 */
public class OidcClientRegistrationAuthenticationProviderConsumer implements Consumer<List<AuthenticationProvider>> {

    private static final List<String> clientMetadata = List.of(SystemConstants.PARAMETER__PRODUCT_KEY);
    private final boolean isRemoteValidate;

    public OidcClientRegistrationAuthenticationProviderConsumer(boolean isRemoteValidate) {
        this.isRemoteValidate = isRemoteValidate;
    }

    @Override
    public void accept(List<AuthenticationProvider> authenticationProviders) {

        Converter<OidcClientRegistration, RegisteredClient> toRegisteredClientConverter =
                new OidcClientRegistrationToRegisteredClientConverter(clientMetadata, isRemoteValidate);
        Converter<RegisteredClient, OidcClientRegistration> toOidcClientRegistrationConverter =
                new RegisteredClientToOidcClientRegistrationConverter(clientMetadata);

        authenticationProviders.forEach((authenticationProvider) -> {
            if (authenticationProvider instanceof OidcClientRegistrationAuthenticationProvider provider) {
                provider.setRegisteredClientConverter(toRegisteredClientConverter);
                provider.setClientRegistrationConverter(toOidcClientRegistrationConverter);
            }
            if (authenticationProvider instanceof OidcClientConfigurationAuthenticationProvider provider) {
                provider.setClientRegistrationConverter(toOidcClientRegistrationConverter);
            }
        });

    }
}
