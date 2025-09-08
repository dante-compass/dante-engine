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
 * along with this program.  If not, see <https://www.herodotus.vip>.
 */

package cn.herodotus.engine.oauth2.extension.converter;

import cn.herodotus.engine.oauth2.extension.entity.OAuth2UserLogging;
import cn.herodotus.engine.oauth2.core.utils.OAuth2AuthenticationUtils;
import cn.herodotus.engine.web.core.utils.HeaderUtils;
import cn.hutool.v7.http.server.servlet.ServletUtil;
import cn.hutool.v7.http.useragent.UserAgent;
import cn.hutool.v7.http.useragent.UserAgentUtil;
import com.google.common.net.HttpHeaders;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;

/**
 * <p>Description: 请求转成 {@link OAuth2UserLogging} </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/12/12 17:18
 */
public class RequestToUserLoggingConverter implements Converter<HttpServletRequest, OAuth2UserLogging> {

    private final String principal;
    private final String clientId;
    private final String operation;

    public RequestToUserLoggingConverter(OAuth2AccessTokenAuthenticationToken token) {
        this(OAuth2AuthenticationUtils.getUsername(token), token.getRegisteredClient().getId(), "登录系统");
    }

    public RequestToUserLoggingConverter(OAuth2Authorization authorization) {
        this(authorization.getPrincipalName(), authorization.getRegisteredClientId(), "退出系统");
    }

    private RequestToUserLoggingConverter(String principal, String clientId, String operation) {
        this.principal = principal;
        this.clientId = clientId;
        this.operation = operation;
    }

    @Override
    public OAuth2UserLogging convert(HttpServletRequest source) {

        OAuth2UserLogging target = new OAuth2UserLogging();
        target.setPrincipalName(principal);
        target.setClientId(clientId);
        target.setIp(HeaderUtils.getIp(source));
        target.setOperation(operation);

        withUserAgent(target, source);

        return target;
    }

    private void withUserAgent(OAuth2UserLogging target, HttpServletRequest source) {
        UserAgent userAgent = UserAgentUtil.parse(source.getHeader(HttpHeaders.USER_AGENT));
        if (ObjectUtils.isNotEmpty(userAgent)) {
            target.setMobile(userAgent.isMobile());
            target.setOsName(userAgent.getOs().getName());
            target.setBrowserName(userAgent.getBrowser().getName());
            target.setMobileBrowser(userAgent.getBrowser().isMobile());
            target.setEngineName(userAgent.getEngine().getName());
            target.setMobilePlatform(userAgent.getPlatform().isMobile());
            target.setIphoneOrIpod(userAgent.getPlatform().isIPhoneOrIPod());
            target.setIpad(userAgent.getPlatform().isIPad());
            target.setIos(userAgent.getPlatform().isIos());
            target.setAndroid(userAgent.getPlatform().isAndroid());
        }
    }
}
