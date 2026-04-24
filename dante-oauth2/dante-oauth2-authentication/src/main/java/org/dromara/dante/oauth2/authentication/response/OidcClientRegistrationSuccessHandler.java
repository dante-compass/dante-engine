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

package org.dromara.dante.oauth2.authentication.response;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.dante.core.constant.SystemConstants;
import org.dromara.dante.oauth2.commons.strategy.OidcClientRegistrationSuccessEventManager;
import org.dromara.dante.security.domain.OAuth2AuthorizationResource;
import org.dromara.dante.security.domain.RegisteredClientTransmitter;
import org.dromara.dante.security.service.OAuth2AuthorizationResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.oidc.OidcClientRegistration;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcClientRegistrationAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.oidc.http.converter.OidcClientRegistrationHttpMessageConverter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/**
 * <p>Description: 客户端自动注册成功后续逻辑处理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/5/23 17:37
 */
public class OidcClientRegistrationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(OidcClientRegistrationSuccessHandler.class);

    private final RegisteredClientRepository registeredClientRepository;
    private final OAuth2AuthorizationResourceService authorizationResourceService;
    private final OidcClientRegistrationSuccessEventManager oidcClientRegistrationSuccessEventManager;

    private final HttpMessageConverter<OidcClientRegistration> clientRegistrationHttpMessageConverter =
            new OidcClientRegistrationHttpMessageConverter();

    public OidcClientRegistrationSuccessHandler(RegisteredClientRepository registeredClientRepository, OAuth2AuthorizationResourceService authorizationResourceService, OidcClientRegistrationSuccessEventManager oidcClientRegistrationSuccessEventManager) {
        this.registeredClientRepository = registeredClientRepository;
        this.authorizationResourceService = authorizationResourceService;
        this.oidcClientRegistrationSuccessEventManager = oidcClientRegistrationSuccessEventManager;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OidcClientRegistrationAuthenticationToken clientRegistrationAuthenticationToken =
                (OidcClientRegistrationAuthenticationToken) authentication;

        OidcClientRegistration clientRegistration = clientRegistrationAuthenticationToken.getClientRegistration();

        // 1. 此处判断 oauth2_registered_client 中是否存在新注册的 Client 无意义，因为肯定存在否则不会走到这里
        // 2. 这种方式会导致 oauth2_authorization_resource 表中，出现多条相同 clientId 信息存在，导致查询出错。
        // RegisteredClient registeredClient = registeredClientRepository.findByClientId(clientRegistration.getClientId());
        // 3.检测 oauth2_authorization_resource 中是否有新注册的 Client。可以规避相同客户端的重复注册
        OAuth2AuthorizationResource authorizationResource = authorizationResourceService.findByClientId(clientRegistration.getClientId());
        if (ObjectUtils.isEmpty(authorizationResource) && StringUtils.isNotBlank(clientRegistration.getRegistrationAccessToken())) {
            log.debug("[Herodotus] |- [OIDC-CLIENT-REGISTRATION] Sync OidcClientRegistration to business entity!");
            RegisteredClient registeredClient = registeredClientRepository.findByClientId(clientRegistration.getClientId());
            if (ObjectUtils.isNotEmpty(registeredClient)) {
                Converter<OidcClientRegistration, RegisteredClientTransmitter> toTransmitter = new OidcClientRegistrationToRegisteredClientTransmitterConverter(registeredClient.getId());
                RegisteredClientTransmitter transmitter = toTransmitter.convert(clientRegistration);
                authorizationResourceService.process(transmitter);
                log.debug("[Herodotus] |- [OIDC-CLIENT-REGISTRATION] Sync client information to authorization resource!");
                oidcClientRegistrationSuccessEventManager.postProcess(transmitter);
            }
        }

        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        if (HttpMethod.POST.name().equals(request.getMethod())) {
            httpResponse.setStatusCode(HttpStatus.CREATED);
        } else {
            httpResponse.setStatusCode(HttpStatus.OK);
        }
        this.clientRegistrationHttpMessageConverter.write(clientRegistration, null, httpResponse);
    }

    static class OidcClientRegistrationToRegisteredClientTransmitterConverter implements Converter<OidcClientRegistration, RegisteredClientTransmitter> {

        private final String id;

        public OidcClientRegistrationToRegisteredClientTransmitterConverter(String id) {
            this.id = id;
        }

        @Override
        public RegisteredClientTransmitter convert(OidcClientRegistration source) {
            RegisteredClientTransmitter target = new RegisteredClientTransmitter();
            target.setId(id);
            target.setClientId(source.getClientId());
            target.setClientName(source.getClientName());
            target.setClientSecret(source.getClientSecret());
            target.setRedirectUris(org.springframework.util.StringUtils.collectionToCommaDelimitedString(source.getRedirectUris()));
            // 在 OidcClientRegistrationToRegisteredClientConverter 中已经判断过 ProductKey 是否为空，这里就不重复判断。
            target.setParentClientId(source.getClaims().get(SystemConstants.PARAMETER__PRODUCT_KEY).toString());
            return target;
        }
    }
}
