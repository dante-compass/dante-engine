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

package cn.herodotus.engine.oauth2.authorization.autoconfigure;

import cn.herodotus.engine.core.foundation.condition.ConditionalOnArchitecture;
import cn.herodotus.engine.core.foundation.enums.Architecture;
import cn.herodotus.engine.logic.upms.annotation.EnableHerodotusLogicUpms;
import cn.herodotus.engine.logic.upms.service.security.SysUserService;
import cn.herodotus.engine.oauth2.authorization.autoconfigure.condition.ConditionalOnUpmsService;
import cn.herodotus.engine.oauth2.authorization.autoconfigure.listener.*;
import cn.herodotus.engine.oauth2.authorization.autoconfigure.processor.AttributeTransmitterDistributeProcessor;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: Upms 服务消息配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/10/27 23:46
 */
@AutoConfiguration
@ConditionalOnUpmsService
@EnableHerodotusLogicUpms
@ComponentScan(basePackages = {
        "cn.herodotus.engine.oauth2.authorization.autoconfigure.processor",
})
public class OAuth2UpmsServiceAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OAuth2UpmsServiceAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [OAuth2 Upms Service] Configure.");
    }

    @Configuration(proxyBeanMethods = false)
    public static class UpmsLocalListenerConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public LocalAccountStatusChangedListener localAccountStatusChangedListener(SysUserService sysUserService) {
            LocalAccountStatusChangedListener listener = new LocalAccountStatusChangedListener(sysUserService);
            log.trace("[Herodotus] |- Bean [Local Account Status Changed Listener] Configure.");
            return listener;
        }

        @Bean
        @ConditionalOnMissingBean
        public LocalRestMappingGatherListener localRequestMappingGatherListener(AttributeTransmitterDistributeProcessor attributeTransmitterDistributeProcessor) {
            LocalRestMappingGatherListener listener = new LocalRestMappingGatherListener(attributeTransmitterDistributeProcessor);
            log.trace("[Herodotus] |- Bean [Local Request Mapping Gather Listener] Configure.");
            return listener;
        }

        @Bean
        @ConditionalOnMissingBean
        public SysAttributeChangeListener sysAttributeChangeListener(AttributeTransmitterDistributeProcessor attributeTransmitterDistributeProcessor) {
            SysAttributeChangeListener listener = new SysAttributeChangeListener(attributeTransmitterDistributeProcessor);
            log.trace("[Herodotus] |- Bean [SysAttribute Change Listener] Configure.");
            return listener;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnArchitecture(Architecture.DISTRIBUTED)
    public static class UpmsRemoteListenerConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public RemoteAccountStatusChangedListener remoteAccountStatusChangedListener(SysUserService sysUserService) {
            RemoteAccountStatusChangedListener listener = new RemoteAccountStatusChangedListener(sysUserService);
            log.trace("[Herodotus] |- Bean [Remote Account Status Changed Listener] Configure.");
            return listener;
        }

        @Bean
        @ConditionalOnMissingBean
        public RemoteRestMappingGatherListener remoteRequestMappingGatherListener(AttributeTransmitterDistributeProcessor attributeTransmitterDistributeProcessor) {
            RemoteRestMappingGatherListener listener = new RemoteRestMappingGatherListener(attributeTransmitterDistributeProcessor);
            log.trace("[Herodotus] |- Bean [Remote Request Mapping Gather Listener] Configure.");
            return listener;
        }
    }
}
