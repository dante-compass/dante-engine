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

package org.dromara.dante.oauth2.authorization.definition;

import com.nimbusds.jose.JOSEObject;
import com.nimbusds.jose.util.Base64URL;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.config.Customizer;

/**
 * <p>Description: 提取 OAuth2ResourceServerCustomizer 通用内容 </p>
 *
 * @author : gengwei_zheng
 * @date : 2026/5/14 17:57
 */
public abstract class AbstractOAuth2ResourceServerCustomizer<T> implements Customizer<T> {

    /**
     * 判断 AccessToken 是否为 JWT Token
     *
     * @param accessToken Token
     * @return true 是 JWT token，false Token 为空或者是 Opaque Token
     */
    protected boolean isJwtToken(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            return false;
        }
        try {
            Base64URL[] parts = JOSEObject.split(accessToken);
            if (parts.length == 3) {
                // 3 parts expected for Signed JWT
                return true;
            }
        } catch (Exception ignored) {

        }

        return false;
    }
}
