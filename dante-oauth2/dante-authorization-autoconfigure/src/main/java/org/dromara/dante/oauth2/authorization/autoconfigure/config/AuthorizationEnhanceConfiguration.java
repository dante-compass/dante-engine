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

package org.dromara.dante.oauth2.authorization.autoconfigure.config;

import jakarta.annotation.PostConstruct;
import org.dromara.dante.oauth2.authorization.autoconfigure.strategy.DefaultAuthenticationManager;
import org.dromara.dante.oauth2.authorization.autoconfigure.strategy.DefaultDisableAuthenticationEventManager;
import org.dromara.dante.oauth2.authorization.autoconfigure.strategy.DefaultEnableAuthenticationEventManager;
import org.dromara.dante.security.definition.AuthenticationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: 资源型服务扩展消息配置 </p>
 * <p>
 * 本配置仅配置特殊需求的消息内容。使用时各服务独立引入
 *
 * @author : gengwei.zheng
 * @date : 2024/8/21 20:15
 */
@Configuration(proxyBeanMethods = false)
public class AuthorizationEnhanceConfiguration {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationEnhanceConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- Module [Authorization Service Enhance] Configure.");
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DefaultAuthenticationManager manager = new DefaultAuthenticationManager(new DefaultDisableAuthenticationEventManager(), new DefaultEnableAuthenticationEventManager());
        log.trace("[Herodotus] |- Bean [Authentication Manager] Configure.");
        return manager;
    }
}
