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

package cn.herodotus.engine.core.autoconfigure.oauth2.definition;

import cn.herodotus.engine.core.definition.constant.SystemConstants;
import cn.herodotus.engine.core.identity.domain.HerodotusUser;
import cn.herodotus.engine.core.identity.domain.UserPrincipal;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;

import java.util.HashSet;
import java.util.List;

/**
 * <p>Description: UserPrincipal 工具类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/4/8 15:48
 */
public class UserPrincipalConverter {

    public static UserPrincipal toUserPrincipal(HerodotusUser herodotusUser) {
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId(herodotusUser.getUserId());
        userPrincipal.setName(herodotusUser.getUsername());
        userPrincipal.setRoles(herodotusUser.getRoles());
        userPrincipal.setAvatar(herodotusUser.getAvatar());
        userPrincipal.setEmployeeId(herodotusUser.getEmployeeId());
        return userPrincipal;
    }

    public static UserPrincipal toUserPrincipal(OAuth2AuthenticatedPrincipal oauth2AuthenticatedPrincipal) {
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId(oauth2AuthenticatedPrincipal.getAttribute(SystemConstants.SCOPE_OPENID));
        userPrincipal.setName(oauth2AuthenticatedPrincipal.getName());
        List<String> roles = oauth2AuthenticatedPrincipal.getAttribute(SystemConstants.ROLES);
        if (CollectionUtils.isNotEmpty(roles)) {
            userPrincipal.setRoles(new HashSet<>(roles));
        }
        userPrincipal.setAvatar(oauth2AuthenticatedPrincipal.getAttribute(SystemConstants.AVATAR));
        userPrincipal.setEmail(oauth2AuthenticatedPrincipal.getAttribute(SystemConstants.SCOPE_EMAIL));
        userPrincipal.setEmployeeId(oauth2AuthenticatedPrincipal.getAttribute(SystemConstants.EMPLOYEE_ID));
        return userPrincipal;
    }

    public static UserPrincipal toUserPrincipal(Jwt jwt) {
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId(jwt.getClaimAsString(SystemConstants.SCOPE_OPENID));
        userPrincipal.setName(jwt.getClaimAsString(JwtClaimNames.SUB));
        userPrincipal.setRoles(jwt.getClaim(SystemConstants.ROLES));
        userPrincipal.setEmail(jwt.getClaim(SystemConstants.SCOPE_EMAIL));
        userPrincipal.setAvatar(jwt.getClaimAsString(SystemConstants.AVATAR));
        userPrincipal.setEmployeeId(jwt.getClaimAsString(SystemConstants.EMPLOYEE_ID));
        return userPrincipal;
    }
}
