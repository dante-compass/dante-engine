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

package cn.herodotus.engine.core.autoconfigure.client.servlet.feign;

import cn.herodotus.engine.core.foundation.condition.ConditionalOnServletApplication;
import feign.*;
import feign.codec.ErrorDecoder;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.AnnotatedParameterProcessor;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p> Description : 自定义通用的Feign Fallback处理工厂(基于Sentinel) </p>
 * <p>
 *
 * @author : gengwei.zheng
 * @date : 2020/3/1 18:35
 * @see <a href="https://blog.csdn.net/ttzommed/article/details/90669320">参考文档</a>
 */
@AutoConfiguration
@ConditionalOnClass(Feign.class)
@ConditionalOnServletApplication
public class FeignAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(FeignAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Starter [Feign] Configure.");
    }

    @Bean
    public Contract feignContract(List<AnnotatedParameterProcessor> parameterProcessors, ConversionService feignConversionService, FeignClientProperties feignClientProperties) {
        log.info("[Herodotus] |- Bean [Herodotus FeignInnerContract] Configure.");
        return new FeignInnerContract(parameterProcessors, feignConversionService, feignClientProperties);
    }

    @Bean
    @ConditionalOnMissingBean(FeignRequestInterceptor.class)
    public RequestInterceptor feignRequestInterceptor() {
        FeignRequestInterceptor feignRequestInterceptor = new FeignRequestInterceptor();
        log.trace("[Herodotus] |- Bean [Feign Request Interceptor] Configure.");
        return feignRequestInterceptor;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }

    /**
     * Feign Logger 配置
     * <p>
     * 1. NONE（默认） --- 不记录任何日志
     * 2. BASIC ---	仅记录请求方法，URL，响应状态代码以及执行时间（适合生产环境）
     * 3. HEADERS --- 记录BASIC级别的基础上，记录请求和响应的header
     * 4. FULL --- 记录请求和响应header，body和元数据
     *
     * @return feign 日志级别
     */
    @Bean
    public feign.Logger.Level logger() {
        return feign.Logger.Level.BASIC;
    }

    /**
     * FeignClient超时设置
     * feign超时设置有3种方式：配置文件直接配置FeignClient、自定义Request.Options及配置文件配置Ribbon，优先级从高到低如下。
     * 1、配置文件里对特定FeignClient配置属性： feign.client.config.demo.connectTimeout=1000，feign.client.config.demo.readTimeout=2000 ；
     * 2、自定义对特定FeignClient生效的Request.Options类型的Bean；
     * 3、配置文件里对所有FeienClient属性的配置：feign.client.config.default.connectTimeout=1000，feign.client.config.default.readTimeout=5000
     * 4、对全体FeignClient生效的Request.Options类型的Bean；
     * 5、特定服务的ribbon配置：demo.ribbon.ConnectTimeout=1000，demo.ribbon.ReadTimeout=5000
     * 6、全体服务的ribbon配置：ribbon.ConnectTimeout=1000，ribbon.ReadTimeout=5000
     * 7、Ribbon默认配置：默认连接超时和读取超时都是1000，即1秒
     * <p>
     * 总结一下：
     * 1、FeignClient的直接配置高于Ribbon的配置
     * 2、特定服务的配置高于全体服务的配置
     * 3、配置文件的配置高于自定义Request.Options
     * 4、如果有特定服务的Options和全体服务的配置文件配置，遵循第二条规则，以特定服务的Options为准；
     * 5、如果有特性服务的Ribbon配置和全体服务的FeignClient配置，遵循第一条规则，以FeingClient的配置为准
     * <p>
     * 最佳实践：
     * 1、不要采用Ribbon配置而要直接配置FeignClient，即配置feign.client.xx
     * 2、配置文件配置全体FeignClient的超时设置，同时对特定服务有特殊设置的，也在配置文件里配置
     * <p>
     *
     * @see <a href="https://blog.csdn.net/weixin_36244726/article/details/103953852"></a>
     */
    @Bean
    public Request.Options options() {
        return new Request.Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true);
    }

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default();
    }
}
