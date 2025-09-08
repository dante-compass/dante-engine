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

package cn.herodotus.engine.core.autoconfigure.client.reactive;

import cn.herodotus.engine.core.definition.utils.NumberUtils;
import cn.herodotus.engine.core.foundation.condition.ConditionalOnArchitecture;
import cn.herodotus.engine.core.foundation.enums.Architecture;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ReactorResourceFactory;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.function.Function;

/**
 * <p>Description: WebClient 自动配置 </p>
 * <p>
 * 1. WebClient 底层支持三种运作机制：Apache HttpClient5、JdkClient 和 Reactor Netty。三种机制是通过 classpath 中的依赖进行动态切换，具体切换逻辑参见：<code>org.springframework.boot.autoconfigure.web.reactive.function.client.ClientHttpConnectorFactoryConfiguration</code>
 * 2. <code>ClientHttpConnectorFactoryConfiguration</code> 配置完成之后会配置出 <code>org.springframework.boot.autoconfigure.web.reactive.function.client.ClientHttpConnectorFactory</code>
 * 3. <code>ClientHttpConnectorFactory</code> 生成 {@link ClientHttpConnector}, 以此为基础扩展 WebClient。
 * <p>
 * 以 Netty 作为 WebClient 的底层，会在 <code>ClientHttpConnectorFactoryConfiguration</code> 中生成 Bean {@link ReactorResourceFactory}。{@link org.springframework.boot.autoconfigure.reactor.netty.ReactorNettyConfigurations.ReactorResourceFactoryConfiguration}
 * 最终还是以 Reactor Netty 作为 WebClient 的基础。因为在 RSocket 中也会主动 Import {@link org.springframework.boot.autoconfigure.reactor.netty.ReactorNettyConfigurations.ReactorResourceFactoryConfiguration}
 *
 * @author : gengwei.zheng
 * @date : 2024/3/10 15:37
 */
@AutoConfiguration
@ConditionalOnClass(WebClient.class)
@EnableConfigurationProperties(WebClientProperties.class)
public class WebClientAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(WebClientAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [WebClient] Configure.");
    }

    /**
     * 增加个 {@link ReactorResourceFactory} 配置。方便配置使用。
     *
     * @return {@link ReactorResourceFactory}
     */
    @Bean
    @ConditionalOnMissingBean
    public ReactorResourceFactory reactorResourceFactory() {
        ReactorResourceFactory factory = new ReactorResourceFactory();
        factory.setUseGlobalResources(true);
        log.trace("[Herodotus] |- Bean [Reactor Resource Factory] Configure.");
        return factory;
    }

    @Bean
    public Function<HttpClient, HttpClient> httpClientMapper(WebClientProperties webClientProperties) {
        return httpClient -> httpClient
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, NumberUtils.longToInt(webClientProperties.getConnectTimeout().toMillis()))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(NumberUtils.longToInt(webClientProperties.getReadTimeout().toSeconds())))
                        .addHandlerLast(new WriteTimeoutHandler(NumberUtils.longToInt(webClientProperties.getWriteTimeout().toSeconds()))));
    }

    /**
     * 在 WebClient.Builder Bean上配置了@LoadBalanced 才会开启 WebClient 负载均衡
     * <p>
     * 如果在非服务发现的环境下，引入了本包。这种情况下，如果给 WebClient.Builder Bean 配置了 @LoadBalanced 会出现 503 错误。因为 @LoadBalanced 是通过服务发现，根据服务名进行调用的。
     * 所以，增加了 @ConditionalOnArchitecture 条件，即在
     * · 分布式环境下才开启负载均衡，因为作为服务都需要读取配置中心配置，目前大多数读取配置都需要使用 bootstrap
     * · 单体环境下使用默认的 WebClient Builder {@link org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration}
     *
     * <code>org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerWebClientBuilderBeanPostProcessor</code>
     * <p>
     * 在 Spring Boot Admin 环境下，如果开启了 @LoadBalanced 会导致响应式服务无法连接到 Spring Boot Admin
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnArchitecture(Architecture.DISTRIBUTED)
    @ConditionalOnMissingClass({"de.codecentric.boot.admin.server.config.EnableAdminServer"})
    static class WebClientBuilderConfiguration {

        @Bean
        @LoadBalanced
        public WebClient.Builder webClientBuilder(ObjectProvider<WebClientCustomizer> customizerProvider) {
            WebClient.Builder builder = WebClient.builder();
            customizerProvider.orderedStream().forEach((customizer) -> customizer.customize(builder));
            log.debug("[Herodotus] |- Bean [LoadBalanced WebClient Builder] Configure.");
            return builder;
        }
    }

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder, ReactorResourceFactory reactorResourceFactory, Function<HttpClient, HttpClient> mapper) {
        ClientHttpConnector connector = new ReactorClientHttpConnector(reactorResourceFactory, mapper);
        WebClient webClient = webClientBuilder.clientConnector(connector).build();
        log.trace("[Herodotus] |- Bean [WebClient] Configure.");
        return webClient;
    }
}
