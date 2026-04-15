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
import org.dromara.dante.oauth2.commons.strategy.OAuth2DeviceVerificationSuccessEventManager;
import org.dromara.dante.security.domain.DeviceVerificationTransmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2DeviceVerificationAuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

/**
 * <p>Description: 设备校验成功处理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/2/28 21:52
 */
public class OAuth2DeviceVerificationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuth2DeviceVerificationSuccessHandler.class);

    private final OAuth2DeviceVerificationSuccessEventManager deviceVerificationSuccessEventManager;

    public OAuth2DeviceVerificationSuccessHandler(OAuth2DeviceVerificationSuccessEventManager deviceVerificationSuccessEventManager) {
        this.deviceVerificationSuccessEventManager = deviceVerificationSuccessEventManager;
    }

    public OAuth2DeviceVerificationSuccessHandler(String defaultTargetUrl, OAuth2DeviceVerificationSuccessEventManager deviceVerificationSuccessEventManager) {
        super(defaultTargetUrl);
        setAlwaysUseDefaultTargetUrl(true);
        this.deviceVerificationSuccessEventManager = deviceVerificationSuccessEventManager;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2DeviceVerificationAuthenticationToken deviceVerificationAuthenticationToken = (OAuth2DeviceVerificationAuthenticationToken) authentication;

        if (deviceVerificationAuthenticationToken.isAuthenticated()) {
            log.debug("[Herodotus] |- [OAUTH2-DEVICE-VERIFICATION] Sync verification status to business entity!");
            DeviceVerificationTransmitter transmitter = new DeviceVerificationTransmitter();
            transmitter.setClientId(deviceVerificationAuthenticationToken.getClientId());
            deviceVerificationSuccessEventManager.postProcess(transmitter);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
