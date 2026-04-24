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

package org.dromara.dante.oauth2.authorization.autoconfigure.strategy;

import org.dromara.dante.oauth2.authorization.autoconfigure.bus.RemoteEnableAuthenticationEvent;
import org.dromara.dante.oauth2.commons.event.EnableAuthenticationEvent;
import org.dromara.dante.oauth2.commons.strategy.EnableAuthenticationEventManager;
import org.dromara.dante.security.domain.RegisteredClientTransmitter;
import org.dromara.dante.spring.context.ServiceContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: 默认的手动开启认证事件管理器 </p>
 * <p>
 * 目前主要用于物联网服务，实现一机一密和一型一密类型认证
 *
 * @author : gengwei.zheng
 * @date : 2024/8/21 16:09
 */
public class DefaultEnableAuthenticationEventManager implements EnableAuthenticationEventManager {

    private static final Logger log = LoggerFactory.getLogger(DefaultEnableAuthenticationEventManager.class);

    @Override
    public String getDestinationServiceName() {
        return ServiceContextHolder.getUaaServiceName();
    }

    @Override
    public void postLocalProcess(RegisteredClientTransmitter data) {
        log.debug("[Herodotus] |- [AUTHENTICATION-SWITCH] Publish local enable event.");
        publishEvent(new EnableAuthenticationEvent(data));
    }

    @Override
    public void postRemoteProcess(String data, String originService, String destinationService) {
        log.debug("[Herodotus] |- [AUTHENTICATION-SWITCH] Publish remote enable event.");
        publishEvent(new RemoteEnableAuthenticationEvent(data, originService, destinationService));
    }
}
