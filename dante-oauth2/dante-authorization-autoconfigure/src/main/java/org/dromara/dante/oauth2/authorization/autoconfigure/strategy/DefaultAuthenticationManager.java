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

import org.dromara.dante.oauth2.commons.strategy.DisableAuthenticationEventManager;
import org.dromara.dante.oauth2.commons.strategy.EnableAuthenticationEventManager;
import org.dromara.dante.security.definition.AuthenticationManager;
import org.dromara.dante.security.domain.RegisteredClientTransmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: 认证管理器 </p>
 * <p>
 * 将动态开启认证管理器 {@link EnableAuthenticationEventManager} 和 将动态关闭认证管理器 {@link DisableAuthenticationEventManager} 封装在一起。
 * 一方面方便 Bean 的注入和配置，另一方面便于其它组件远程控制认证
 *
 * @author : gengwei.zheng
 * @date : 2024/10/9 17:54
 */
public class DefaultAuthenticationManager implements AuthenticationManager {

    private static final Logger log = LoggerFactory.getLogger(DefaultAuthenticationManager.class);

    private final DisableAuthenticationEventManager disableAuthenticationEventManager;
    private final EnableAuthenticationEventManager enableAuthenticationEventManager;

    public DefaultAuthenticationManager(DisableAuthenticationEventManager disableAuthenticationEventManager, EnableAuthenticationEventManager enableAuthenticationEventManager) {
        this.disableAuthenticationEventManager = disableAuthenticationEventManager;
        this.enableAuthenticationEventManager = enableAuthenticationEventManager;
    }

    @Override
    public void disable(String id) {
        log.debug("[Herodotus] |- [AUTHENTICATION-SWITCH] Start Authentication disable process!");
        disableAuthenticationEventManager.postProcess(id);
    }

    @Override
    public void enable(RegisteredClientTransmitter registeredClientTransmitter) {
        log.debug("[Herodotus] |- [AUTHENTICATION-SWITCH] Start Authentication enable process!");
        enableAuthenticationEventManager.postProcess(registeredClientTransmitter);
    }
}
