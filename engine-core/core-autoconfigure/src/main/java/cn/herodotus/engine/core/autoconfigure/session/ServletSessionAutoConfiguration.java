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

package cn.herodotus.engine.core.autoconfigure.session;

import cn.herodotus.engine.core.autoconfigure.jackson2.Jackson2AutoConfiguration;
import cn.herodotus.engine.core.foundation.condition.ConditionalOnServletApplication;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.SessionRepositoryCustomizer;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

/**
 * <p>Description: Servlet 环境 Spring Session 自动配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/4/6 16:03
 */
@AutoConfiguration(after = Jackson2AutoConfiguration.class)
@ConditionalOnServletApplication
@ConditionalOnClass({SessionRepository.class})
@EnableRedisIndexedHttpSession
public class ServletSessionAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ServletSessionAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [Servlet Spring Session] Configure.");
    }

    @Bean
    public SpringSessionRememberMeServices springSessionRememberMeServices() {
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        log.trace("[Herodotus] |- Bean [Spring Session Remember Me Services] Configure.");
        return rememberMeServices;
    }

    @Bean
    public SessionRepositoryCustomizer<RedisIndexedSessionRepository> sessionRepositoryCustomizer() {
        // 关闭 Spring Session 自身的清理任务，让 Redis 自行清理过期内容
        // 如果不关闭，经常会出现 java.lang.IllegalStateException: LettuceConnectionFactory has been STOPPED. Use start() to initialize it 问题
        // cleanup-cron 参数默认值通常是 "0 * * * * *"（每分钟跑一次）。通过将其设置为一个无效的 cron 表达式（如 "* * * 31 2 ?"），Spring Session 就不会创建那个后台调度任务，自然也就不会在关闭时发生冲突了
        // * * * 31 2 ? 此表达式是指2月31日执行，但是该日期不存在，所以永远不会执行
        return (sessionRepository) -> sessionRepository.setCleanupCron("* * * 31 2 ?");
    }

    @Configuration(proxyBeanMethods = false)
    @EnableSpringHttpSession
    static class SpringHttpSessionConfiguration {

    }
}
