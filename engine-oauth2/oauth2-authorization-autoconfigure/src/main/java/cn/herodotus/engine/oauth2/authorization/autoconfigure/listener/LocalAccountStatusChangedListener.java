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

package cn.herodotus.engine.oauth2.authorization.autoconfigure.listener;

import cn.herodotus.engine.data.core.enums.DataItemStatus;
import cn.herodotus.engine.logic.upms.service.security.SysUserService;
import cn.herodotus.engine.message.core.domain.AccountStatus;
import cn.herodotus.engine.message.core.event.AccountStatusChangedEvent;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * <p>Description: 本地用户状态变更监听 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/7/10 17:59
 */
@Component
public class LocalAccountStatusChangedListener implements ApplicationListener<AccountStatusChangedEvent> {

    private static final Logger log = LoggerFactory.getLogger(LocalAccountStatusChangedListener.class);

    private final SysUserService sysUserService;

    public LocalAccountStatusChangedListener(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Override
    public void onApplicationEvent(AccountStatusChangedEvent event) {

        log.info("[Herodotus] |- Account status changed LOCAL listener, response event!");

        AccountStatus accountStatus = event.getData();
        if (ObjectUtils.isNotEmpty(accountStatus)) {
            DataItemStatus dataItemStatus = DataItemStatus.valueOf(accountStatus.getStatus());
            if (ObjectUtils.isNotEmpty(dataItemStatus)) {
                log.debug("[Herodotus] |- [A2] Account status changed process BEGIN!");
                sysUserService.changeStatus(accountStatus.getUserId(), dataItemStatus);
            }
        }
    }
}
