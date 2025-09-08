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

import cn.herodotus.engine.core.foundation.condition.ConditionalOnServletApplication;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.http.client.HttpClientAutoConfiguration;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * <p>Description: RestTemplate 统一自动配置 </p>
 * <p>
 * Spring Boot 3.4.0 客户端配置修改为使用配置方式或者根据依赖包主动探测。参见：{@link HttpClientAutoConfiguration}
 * <code>HttpClientAutoConfiguration</code> 会构建 {@link ClientHttpRequestFactoryBuilder} 和 {@link ClientHttpRequestFactorySettings}，以此来构建其它客户端。
 *
 * @author : gengwei.zheng
 * @date : 2024/3/10 15:57
 */
@AutoConfiguration(after = {org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration.class})
@ConditionalOnServletApplication
public class RestTemplateAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RestTemplateAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [RestTemplate] Configure.");
    }

    /**
     * 使用 @LoadBalanced 注解表示使用 loadbalancer 实现客户端负载均衡
     *
     * @return RestTemplate
     */
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(RestTemplateBuilder restTemplateBuilder) {

        /*
         * 默认的 RestTemplate 有个机制是请求状态码非200 就抛出异常，会中断接下来的操作。
         * 如果不想中断对结果数据得解析，可以通过覆盖默认的 ResponseErrorHandler ，
         * 对hasError修改下，让他一直返回true，即是不检查状态码及抛异常了
         */
        ResponseErrorHandler responseErrorHandler = new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return true;
            }
        };

        RestTemplate restTemplate = restTemplateBuilder
                .errorHandler(responseErrorHandler)
                .build();
        log.debug("[Herodotus] |- Bean [LoadBalanced Rest Template] Configure.");
        return restTemplate;
    }
}
