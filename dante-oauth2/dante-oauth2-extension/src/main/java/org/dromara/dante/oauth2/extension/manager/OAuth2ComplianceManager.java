/*
 * Copyright 2020-2030 码匠君<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 是 Dante Cloud 系统核心组件库，采用 APACHE LICENSE 2.0 开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1. 请不要删除和修改根目录下的LICENSE文件。
 * 2. 请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3. 请保留源码和相关描述文件的项目出处，作者声明等。
 * 4. 分发源码时候，请注明软件出处 <https://gitee.com/dromara/dante-cloud>
 * 5. 在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/dromara/dante-cloud>
 * 6. 若您的项目无法满足以上几点，可申请商业授权
 */

package org.dromara.dante.oauth2.extension.manager;

import org.dromara.dante.cache.commons.exception.MaximumLimitExceededException;
import org.dromara.dante.oauth2.extension.converter.RequestToUserLoggingConverter;
import org.dromara.dante.oauth2.extension.entity.OAuth2UserLogging;
import org.dromara.dante.oauth2.extension.service.OAuth2UserLoggingService;
import org.dromara.dante.oauth2.extension.stamp.SignInFailureLimitedStampManager;
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
