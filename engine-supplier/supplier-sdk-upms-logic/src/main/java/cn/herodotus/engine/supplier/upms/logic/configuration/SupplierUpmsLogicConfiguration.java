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

package cn.herodotus.engine.supplier.upms.logic.configuration;

import cn.herodotus.engine.access.all.configuration.AccessAllConfiguration;
import cn.herodotus.engine.access.all.processor.AccessHandlerStrategyFactory;
import cn.herodotus.engine.oauth2.core.definition.handler.SocialAuthenticationHandler;
import cn.herodotus.engine.supplier.upms.logic.processor.DefaultSocialAuthenticationHandler;
import cn.herodotus.engine.supplier.upms.logic.service.security.SysSocialUserService;
import cn.herodotus.engine.supplier.upms.logic.service.security.SysUserService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * <p>Description: UPMS SDK 模块配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/2/16 19:00
 */
@Configuration(proxyBeanMethods = false)
@EntityScan(basePackages = {
        "cn.herodotus.engine.supplier.upms.logic.entity.security",
        "cn.herodotus.engine.supplier.upms.logic.entity.hr",
})
@EnableJpaRepositories(basePackages = {
        "cn.herodotus.engine.supplier.upms.logic.repository.security",
        "cn.herodotus.engine.supplier.upms.logic.repository.hr",
})
@ComponentScan(basePackages = {
        "cn.herodotus.engine.supplier.upms.logic.service.security",
        "cn.herodotus.engine.supplier.upms.logic.service.hr",
        "cn.herodotus.engine.supplier.upms.logic.service.assistant",
})
@Import({AccessAllConfiguration.class})
public class SupplierUpmsLogicConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SupplierUpmsLogicConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- SDK [Supplier Upms Logic] Auto Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    public SocialAuthenticationHandler socialAuthenticationHandler(SysUserService sysUserService, SysSocialUserService sysSocialUserService, AccessHandlerStrategyFactory accessHandlerStrategyFactory) {
        DefaultSocialAuthenticationHandler defaultSocialAuthenticationHandler = new DefaultSocialAuthenticationHandler(sysUserService, sysSocialUserService, accessHandlerStrategyFactory);
        log.trace("[Herodotus] |- Bean [Default Social Authentication Handler] Auto Configure.");
        return defaultSocialAuthenticationHandler;
    }
}
