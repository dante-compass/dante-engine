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

package org.dromara.dante.servlet.container.autoconfigure.oauth2;

import cn.herodotus.dante.security.definition.BearerTokenResolver;
import cn.herodotus.dante.security.domain.UserPrincipal;
import cn.herodotus.dante.security.utils.SecurityUtils;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

/**
 * <p>Description: 阻塞式 Bearer Token 处理器 </p>
 * <p>
 * 根据 AccessToken 动态判断是否为 JWT Token，然后动态选择解析器
 *
 * @author : gengwei_zheng
 * @date : 2026/5/15 22:33
 */
public class HerodotusServletBearerTokenResolver implements BearerTokenResolver {

    private final HerodotusServletJwtTokenResolver jwtTokenResolver;
    private final HerodotusServletOpaqueTokenResolver opaqueTokenResolver;

    public HerodotusServletBearerTokenResolver(JwtDecoder jwtDecoder, OpaqueTokenIntrospector opaqueTokenIntrospector) {
        this.jwtTokenResolver = new HerodotusServletJwtTokenResolver(jwtDecoder);
        this.opaqueTokenResolver = new HerodotusServletOpaqueTokenResolver(opaqueTokenIntrospector);
    }

    @Override
    public UserPrincipal resolve(String accessToken) {
        return SecurityUtils.isJwtToken(accessToken) ? jwtTokenResolver.resolve(accessToken) : opaqueTokenResolver.resolve(accessToken);
    }
}
