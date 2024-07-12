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

package cn.herodotus.engine.access.all.configuration;

import cn.herodotus.engine.access.all.controller.JustAuthAccessController;
import cn.herodotus.engine.access.all.controller.PhoneNumberAccessController;
import cn.herodotus.engine.access.all.controller.WxappAccessController;
import cn.herodotus.engine.access.all.customizer.AccessErrorCodeMapperBuilderCustomizer;
import cn.herodotus.engine.access.all.processor.AccessHandlerStrategyFactory;
import cn.herodotus.engine.access.justauth.annotation.ConditionalOnJustAuthEnabled;
import cn.herodotus.engine.access.justauth.configuration.AccessJustAuthConfiguration;
import cn.herodotus.engine.access.sms.annotation.ConditionalOnSmsEnabled;
import cn.herodotus.engine.access.sms.configuration.AccessSmsConfiguration;
import cn.herodotus.engine.access.wxapp.annotation.ConditionalOnWxappEnabled;
import cn.herodotus.engine.access.wxapp.configuration.AccessWxappConfiguration;
import cn.herodotus.engine.access.wxmpp.configuration.AccessWxmppConfiguration;
import cn.herodotus.engine.assistant.definition.function.ErrorCodeMapperBuilderCustomizer;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>Description: Access 业务模块配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/26 14:59
 */
@Configuration(proxyBeanMethods = false)
@Import({
        AccessJustAuthConfiguration.class,
        AccessSmsConfiguration.class,
        AccessWxappConfiguration.class,
        AccessWxmppConfiguration.class
})
public class AccessAllConfiguration {

    private static final Logger log = LoggerFactory.getLogger(AccessAllConfiguration.class);

    @PostConstruct
    public void init() {
        log.debug("[Herodotus] |- SDK [Access All] Auto Configure.");
    }

    @Bean
    @ConditionalOnMissingBean(AccessHandlerStrategyFactory.class)
    public AccessHandlerStrategyFactory accessHandlerStrategyFactory() {
        AccessHandlerStrategyFactory accessHandlerStrategyFactory = new AccessHandlerStrategyFactory();
        log.trace("[Herodotus] |- Bean [Access Handler Strategy Factory] Auto Configure.");
        return accessHandlerStrategyFactory;
    }

    @Bean
    public ErrorCodeMapperBuilderCustomizer accessErrorCodeMapperBuilderCustomizer() {
        AccessErrorCodeMapperBuilderCustomizer customizer = new AccessErrorCodeMapperBuilderCustomizer();
        log.debug("[Herodotus] |- Strategy [Access ErrorCodeMapper Builder Customizer] Auto Configure.");
        return customizer;
    }

    @Configuration(proxyBeanMethods = false)
    static class ControllerConfiguration {

        @PostConstruct
        public void init() {
            log.debug("[Herodotus] |- SDK [Access All Controller] Auto Configure.");
        }

        @Bean
        @ConditionalOnSmsEnabled
        @ConditionalOnMissingBean
        public PhoneNumberAccessController phoneNumberAccessController() {
            PhoneNumberAccessController phoneNumberAuthenticationController = new PhoneNumberAccessController();
            log.trace("[Herodotus] |- Bean [Phone Number Access Controller] Auto Configure.");
            return phoneNumberAuthenticationController;
        }

        @Bean
        @ConditionalOnJustAuthEnabled
        @ConditionalOnMissingBean
        public JustAuthAccessController justAuthSignInController() {
            JustAuthAccessController justAuthAuthenticationController = new JustAuthAccessController();
            log.trace("[Herodotus] |- Bean [Just Auth Access Controller] Auto Configure.");
            return justAuthAuthenticationController;
        }

        @Bean
        @ConditionalOnWxappEnabled
        @ConditionalOnMissingBean
        public WxappAccessController wxappAccessController() {
            WxappAccessController wxappAccessController = new WxappAccessController();
            log.trace("[Herodotus] |- Bean [Wxapp Access Controller] Auto Configure.");
            return wxappAccessController;
        }
    }
}
