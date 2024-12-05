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

package cn.herodotus.engine.rest.autoconfigure;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.client.RestClientBuilderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

/**
 * <p>Description: RestClient 配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/12/4 20:16
 */
@AutoConfiguration(after = {org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration.class, RestTemplateAutoConfiguration.class})
public class RestClientAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RestClientAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [Rest Client] Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    RestClient.Builder restClientBuilder(RestClientBuilderConfigurer restClientBuilderConfigurer, RestTemplate restTemplate) {
        return restClientBuilderConfigurer.configure(RestClient.builder(restTemplate));
    }

    @Bean
    @ConditionalOnMissingBean
    public RestClient restClient(RestClient.Builder restClientBuilder) {
        RestClient restClient = restClientBuilder.build();
        log.trace("[Herodotus] |- Bean [RestClient] Configure.");
        return restClient;
    }
}
