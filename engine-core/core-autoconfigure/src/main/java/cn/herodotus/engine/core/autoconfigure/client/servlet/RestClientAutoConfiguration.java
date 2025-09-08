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

package cn.herodotus.engine.core.autoconfigure.client.servlet;

import cn.herodotus.engine.core.foundation.condition.ConditionalOnArchitecture;
import cn.herodotus.engine.core.foundation.condition.ConditionalOnServletApplication;
import cn.herodotus.engine.core.foundation.enums.Architecture;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.client.RestClientBuilderConfigurer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * <p>Description: RestClient 配置 </p>
 * <p>
 * RestClient 原始配置 {@link org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration}
 *
 * @author : gengwei.zheng
 * @date : 2024/12/4 20:16
 */
@AutoConfiguration
@ConditionalOnClass(RestClient.class)
@ConditionalOnServletApplication
public class RestClientAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RestClientAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [Rest Client] Configure.");
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnArchitecture(Architecture.MONOCOQUE)
    static class RestClientMonocoqueBuilderConfiguration {
        @Bean
        public RestClient.Builder restClientBuilder(RestClientBuilderConfigurer restClientBuilderConfigurer) {
            return restClientBuilderConfigurer.configure(RestClient.builder());
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnArchitecture(Architecture.DISTRIBUTED)
    static class RestClientDistributedBuilderConfiguration {
        @Bean
        @LoadBalanced
        public RestClient.Builder restClientBuilder(RestClientBuilderConfigurer restClientBuilderConfigurer) {
            return restClientBuilderConfigurer.configure(RestClient.builder());
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public RestClient restClient(RestClient.Builder restClientBuilder) {
        RestClient restClient = restClientBuilder.build();
        log.trace("[Herodotus] |- Bean [RestClient] Configure.");
        return restClient;
    }
}
