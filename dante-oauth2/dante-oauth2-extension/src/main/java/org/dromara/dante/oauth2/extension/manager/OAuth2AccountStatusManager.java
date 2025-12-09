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

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.dante.data.commons.enums.DataItemStatus;
import org.dromara.dante.message.core.definition.strategy.AccountStatusChangedEventManager;
import org.dromara.dante.message.core.domain.AccountStatus;
import org.dromara.dante.oauth2.extension.stamp.LockedAccountStampManager;
import org.dromara.dante.security.domain.HerodotusUser;
import org.dromara.dante.security.service.EnhanceUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * <p>Description: 账户锁定处理服务 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/7/8 19:25
 */
@Component
public class OAuth2AccountStatusManager {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AccountStatusManager.class);

    private final UserDetailsService userDetailsService;
    private final AccountStatusChangedEventManager accountStatusChangedEventManager;
    private final LockedAccountStampManager lockedAccountStampManager;

    public OAuth2AccountStatusManager(UserDetailsService userDetailsService, AccountStatusChangedEventManager accountStatusChangedEventManager, LockedAccountStampManager lockedAccountStampManager) {
        this.userDetailsService = userDetailsService;
        this.lockedAccountStampManager = lockedAccountStampManager;
        this.accountStatusChangedEventManager = accountStatusChangedEventManager;
    }

    private EnhanceUserDetailsService getUserDetailsService() {
        return (EnhanceUserDetailsService) userDetailsService;
    }

    private String getUserId(String username) {
        EnhanceUserDetailsService enhanceUserDetailsService = getUserDetailsService();
        HerodotusUser user = enhanceUserDetailsService.loadHerodotusUserByUsername(username);
        if (ObjectUtils.isNotEmpty(user)) {
            return user.getUserId();
        }

        log.warn("[Herodotus] |- Can not found the userid for [{}]", username);
        return null;
    }

    public void lock(String username) {
        String userId = getUserId(username);
        if (ObjectUtils.isNotEmpty(userId)) {
            log.debug("[Herodotus] |- [A1] Start disable account status changed process for [{}]", userId);
            accountStatusChangedEventManager.postProcess(new AccountStatus(userId, DataItemStatus.LOCKING.name()));
            lockedAccountStampManager.put(userId, username);
            log.info("[Herodotus] |- User count [{}] has been locked, and record into cache!", username);
        }
    }

    public void enable(String userId) {
        if (ObjectUtils.isNotEmpty(userId)) {
            log.debug("[Herodotus] |- [A1] Start enable account status changed process for [{}]", userId);
            accountStatusChangedEventManager.postProcess(new AccountStatus(userId, DataItemStatus.ENABLE.name()));
        }
    }

    public void releaseFromCache(String username) {
        String userId = getUserId(username);
        if (ObjectUtils.isNotEmpty(userId)) {
            String value = lockedAccountStampManager.get(userId);
            if (StringUtils.isNotEmpty(value)) {
                this.lockedAccountStampManager.delete(userId);
                log.info("[Herodotus] |- User count [{}] locked info has been release!", username);
            }
        }
    }
}
