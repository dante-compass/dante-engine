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

package cn.herodotus.engine.core.autoconfigure.oauth2.servlet;

import cn.herodotus.engine.core.autoconfigure.oauth2.definition.UserPrincipalConverter;
import cn.herodotus.engine.core.identity.domain.UserPrincipal;
import cn.herodotus.engine.core.identity.oauth2.BearerTokenResolver;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

/**
 * <p>Description: Servlet 环境 JWT Token 手动解析器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/4/8 16:48
 */
public class HerodotusServletJwtTokenResolver implements BearerTokenResolver {

    private final JwtDecoder jwtDecoder;

    private static final Logger log = LoggerFactory.getLogger(HerodotusServletJwtTokenResolver.class);

    public HerodotusServletJwtTokenResolver(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public UserPrincipal resolve(String token) {

        if (StringUtils.isBlank(token)) {
            throw new IllegalArgumentException("token can not be null");
        }

        Jwt jwt = getJwt(token);
        if (ObjectUtils.isNotEmpty(jwt)) {
            UserPrincipal userPrincipal = UserPrincipalConverter.toUserPrincipal(jwt);
            log.debug("[Herodotus] |- Resolve JWT token to principal details [{}]", userPrincipal);
            return userPrincipal;
        }

        return null;
    }

    private Jwt getJwt(String token) {
        try {
            return this.jwtDecoder.decode(token);
        } catch (BadJwtException failed) {
            log.error("[Herodotus] |- Failed to decode since the JWT was invalid");
        } catch (JwtException failed) {
            log.error("[Herodotus] |- Failed to decode JWT, catch exception", failed);
        }

        return null;
    }
}
