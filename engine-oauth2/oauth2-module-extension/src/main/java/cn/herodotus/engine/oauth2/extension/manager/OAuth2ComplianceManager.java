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

package cn.herodotus.engine.oauth2.extension.manager;

import cn.herodotus.engine.cache.core.exception.MaximumLimitExceededException;
import cn.herodotus.engine.oauth2.extension.converter.RequestToUserLoggingConverter;
import cn.herodotus.engine.oauth2.extension.entity.OAuth2UserLogging;
import cn.herodotus.engine.oauth2.extension.service.OAuth2UserLoggingService;
import cn.herodotus.engine.oauth2.extension.stamp.SignInFailureLimitedStampManager;
import cn.hutool.v7.crypto.SecureUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * <p>Description: OAuth2 应用合规管理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/1 16:37
 */
@Component
public class OAuth2ComplianceManager {

    private static final Logger log = LoggerFactory.getLogger(OAuth2ComplianceManager.class);

    private final OAuth2UserLoggingService userLoggingService;
    private final OAuth2AccountStatusManager accountStatusManager;
    private final SignInFailureLimitedStampManager stampManager;

    public OAuth2ComplianceManager(OAuth2UserLoggingService userLoggingService, OAuth2AccountStatusManager accountStatusManager, SignInFailureLimitedStampManager stampManager) {
        this.userLoggingService = userLoggingService;
        this.accountStatusManager = accountStatusManager;
        this.stampManager = stampManager;
    }

    /**
     * 清除登录失败标记
     *
     * @param principal 用户
     */
    private void cleanSignInFailureTimes(String principal) {
        String key = SecureUtil.md5(principal);
        boolean hasKey = stampManager.containKey(key);
        if (hasKey) {
            log.debug("[Herodotus] |- Clean sign in failure stamp for user [{}].", principal);
            stampManager.delete(key);
        }
    }

    /**
     * 登录失败计数
     *
     * @param principal 用户
     */
    public void countingSignInFailureTimes(String principal) {
        log.debug("[Herodotus] |- Parse the user name in failure event is [{}].", principal);

        int maxTimes = stampManager.getAuthenticationProperties().getSignInFailureLimited().getMaxTimes();
        Duration expire = stampManager.getAuthenticationProperties().getSignInFailureLimited().getExpire();
        try {
            int times = stampManager.counting(principal, maxTimes, expire, true, "AuthenticationFailureListener");
            log.debug("[Herodotus] |- Sign in user input password error [{}] items", times);
        } catch (MaximumLimitExceededException e) {
            log.warn("[Herodotus] |- User [{}] password error [{}] items, LOCK ACCOUNT!", principal, maxTimes);
            accountStatusManager.lock(principal);
        }
    }

    /**
     * 重新设置用户账号为可用状态。即解除账号锁定状态
     *
     * @param userId 用户 ID
     */
    public void enable(String userId) {
        accountStatusManager.enable(userId);
    }

    /**
     * 记录用户登录信息
     *
     * @param token   Token 信息
     * @param request 请求
     */
    public void signIn(OAuth2AccessTokenAuthenticationToken token, HttpServletRequest request) {
           Converter<HttpServletRequest, OAuth2UserLogging> toUserLogging = new RequestToUserLoggingConverter(token);
        OAuth2UserLogging userLogging = toUserLogging.convert(request);

        if (ObjectUtils.isNotEmpty(userLogging)) {
            OAuth2UserLogging result = userLoggingService.save(userLogging);
            if (ObjectUtils.isNotEmpty(result) && StringUtils.isNotBlank(result.getPrincipalName())) {
                // 清除登录失败标记
                cleanSignInFailureTimes(result.getPrincipalName());
            }
        }
    }

    /**
     * 记录用户登出信息
     *
     * @param authorization 认证信息
     * @param request       请求
     */
    public void signOut(OAuth2Authorization authorization, HttpServletRequest request) {
        Converter<HttpServletRequest, OAuth2UserLogging> toUserLogging = new RequestToUserLoggingConverter(authorization);
        OAuth2UserLogging userLogging = toUserLogging.convert(request);

        if (ObjectUtils.isNotEmpty(userLogging)) {
            userLoggingService.save(userLogging);
            accountStatusManager.releaseFromCache(authorization.getPrincipalName());
        }
    }
}
