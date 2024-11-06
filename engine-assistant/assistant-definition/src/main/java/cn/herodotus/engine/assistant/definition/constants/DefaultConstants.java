/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2020-2030 郑庚伟 ZHENGGENGWEI (码匠君), <herodotus@aliyun.com> Licensed under the AGPL License
 *
 * This file is part of Herodotus Engine.
 *
 * Herodotus Engine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Herodotus Engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.herodotus.cn>.
 */

package cn.herodotus.engine.assistant.definition.constants;

import org.dromara.hutool.core.date.DateFormatPool;

/**
 * <p>Description: 默认常量合集 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/5/30 10:27
 */
public interface DefaultConstants {

    String AUTHORIZATION_ENDPOINT = "/oauth2/authorize";
    String TOKEN_ENDPOINT = "/oauth2/token";
    String TOKEN_REVOCATION_ENDPOINT = "/oauth2/revoke";
    String TOKEN_INTROSPECTION_ENDPOINT = "/oauth2/introspect";
    String DEVICE_AUTHORIZATION_ENDPOINT = "/oauth2/device_authorization";
    String DEVICE_VERIFICATION_ENDPOINT = "/oauth2/device_verification";
    String JWK_SET_ENDPOINT = "/oauth2/jwks";
    String OIDC_CLIENT_REGISTRATION_ENDPOINT = "/connect/register";
    String OIDC_LOGOUT_ENDPOINT = "/connect/logout";
    String OIDC_USER_INFO_ENDPOINT = "/userinfo";

    String AUTHORIZATION_CONSENT_URI = "/oauth2/consent";
    String DEVICE_ACTIVATION_URI = "/oauth2/device_activation";
    String DEVICE_VERIFICATION_SUCCESS_URI = "/device_activated";

    /**
     * 默认租户ID
     */
    String TENANT_ID = "public";
    /**
     * 默认树形结构根节点
     */
    String TREE_ROOT_ID = SymbolConstants.ZERO;
    /**
     * 默认的时间日期格式
     */
    String DATE_TIME_FORMAT = DateFormatPool.NORM_DATETIME_PATTERN;
}
