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

package org.dromara.dante.oauth2.extension.converter;

import com.google.common.net.HttpHeaders;
import jakarta.servlet.http.HttpServletRequest;
import org.dromara.dante.persistence.commons.domain.HerodotusUserLogging;
import org.dromara.dante.security.utils.SecurityUtils;
import org.dromara.dante.web.servlet.utils.HeaderUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;

/**
 * <p>Description: 请求转成 {@link HerodotusUserLogging} </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/12/12 17:18
 */
public class RequestToUserLoggingConverter implements Converter<HttpServletRequest, HerodotusUserLogging> {

    private final String principal;
    private final String clientId;
    private final String operation;

    public RequestToUserLoggingConverter(OAuth2AccessTokenAuthenticationToken token) {
        this(SecurityUtils.getUsername(token), token.getRegisteredClient().getId(), "登录系统");
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
    public HerodotusUserLogging convert(HttpServletRequest source) {

        HerodotusUserLogging target = new HerodotusUserLogging(source.getHeader(HttpHeaders.USER_AGENT));
        target.setPrincipalName(principal);
        target.setClientId(clientId);
        target.setIp(HeaderUtils.getIp(source));
        target.setOperation(operation);

        return target;
    }
}
