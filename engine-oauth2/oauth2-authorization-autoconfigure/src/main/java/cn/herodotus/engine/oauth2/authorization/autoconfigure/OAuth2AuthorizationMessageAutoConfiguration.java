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
import cn.herodotus.engine.core.foundation.condition.ConditionalOnReactiveApplication;
import cn.herodotus.engine.core.foundation.condition.ConditionalOnServletApplication;
import cn.herodotus.engine.core.foundation.enums.Architecture;
import cn.herodotus.engine.message.core.definition.strategy.RestMappingScanEventManager;
import cn.herodotus.engine.oauth2.authorization.autoconfigure.listener.RemoteAttributeTransmitterSyncListener;
import cn.herodotus.engine.oauth2.authorization.autoconfigure.strategy.DefaultRestMappingScanEventManager;
import cn.herodotus.engine.oauth2.authorization.processor.SecurityAttributeAnalyzer;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.bus.ServiceMatcher;
import org.springframework.cloud.bus.StreamBusBridge;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

/**
 * <p>Description: 资源型服务消息配置  </p>
 * <p>
 * 本配置类配置所有服务均需要用到的消息相关内容配置。
 *
 * @author : gengwei.zheng
 * @date : 2024/8/21 17:54
 */
@AutoConfiguration
public class OAuth2AuthorizationMessageAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthorizationMessageAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [OAuth2 Authorization Message] Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnServletApplication
    public RestMappingScanEventManager servletRequestMappingScanEventManager(SecurityAttributeAnalyzer analyzer) {
        DefaultRestMappingScanEventManager manager = new DefaultRestMappingScanEventManager(analyzer);
        log.trace("[Herodotus] |- Bean [Servlet Request Mapping Scan Manager] Configure.");
        return manager;
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(StreamBusBridge.class)
    @ConditionalOnArchitecture(Architecture.DISTRIBUTED)
    @RemoteApplicationEventScan({
            "cn.herodotus.engine.oauth2.authorization.autoconfigure.bus"
    })
    static class BusMessageConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public RemoteAttributeTransmitterSyncListener remoteSecurityMetadataSyncListener(SecurityAttributeAnalyzer analyzer, ServiceMatcher serviceMatcher) {
            RemoteAttributeTransmitterSyncListener listener = new RemoteAttributeTransmitterSyncListener(analyzer, serviceMatcher);
            log.trace("[Herodotus] |- Bean [Security Metadata Refresh Listener] Configure.");
            return listener;
        }
    }
}
