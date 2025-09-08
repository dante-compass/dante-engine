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

package cn.herodotus.engine.core.definition.constant;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * <p>Description: Web配置常量 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/5/3 23:03
 */
public interface SecurityResources {

    String MATCHER_WEBJARS = "/webjars/**";
    String MATCHER_STATIC = "/static/**";

    List<String> DEFAULT_IGNORED_STATIC_RESOURCES = Lists.newArrayList(MATCHER_WEBJARS, MATCHER_STATIC,
            "/error/**",
            "/herodotus/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/openapi.json",
            "/favicon.ico"
    );
    List<String> DEFAULT_PERMIT_ALL_RESOURCES = Lists.newArrayList(
            "/open/**",
            "/stomp/ws",
            "/oauth2/sign-out",
            "/login*"
    );

    List<String> DEFAULT_HAS_AUTHENTICATED_RESOURCES = Lists.newArrayList(
            SystemConstants.OAUTH2_DEVICE_VERIFICATION_SUCCESS_URI,
            SystemConstants.OAUTH2_DEVICE_VERIFICATION_FAILURE_URI,
            SystemConstants.OAUTH2_AUTHORIZATION_CONSENT_URI,
            "/engine-rest/**"
    );
}
