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

package cn.herodotus.engine.core.autoconfigure.async;

import cn.herodotus.engine.core.definition.constant.SystemConstants;
import cn.herodotus.engine.core.foundation.condition.ConditionalOnServletApplication;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnThreading;
import org.springframework.boot.autoconfigure.thread.Threading;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * <p>Description: 异步操作配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/11/15 22:47
 */
@AutoConfiguration
@EnableAsync
public class AsyncAutoConfiguration implements AsyncConfigurer {

    private static final Logger log = LoggerFactory.getLogger(AsyncAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [Async] Configure.");
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnThreading(Threading.VIRTUAL)
    @ConditionalOnServletApplication
    static class AsyncVirtualThreadConfiguration implements AsyncConfigurer {
        @Override
        public Executor getAsyncExecutor() {
            log.info("[Herodotus] |- Virtual thread for [@Async] is enabled.");
            return new TaskExecutorAdapter(Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name(SystemConstants.VIRTUAL_ASYNC_PREFIX, 1).factory()));
        }
    }
}
