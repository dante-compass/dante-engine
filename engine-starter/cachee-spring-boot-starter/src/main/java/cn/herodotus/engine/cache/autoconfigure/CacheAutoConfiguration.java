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

package cn.herodotus.engine.cache.autoconfigure;

import cn.herodotus.engine.cache.jetcache.config.CacheJetCacheConfiguration;
import cn.herodotus.engine.cache.redisson.config.CacheRedissonConfiguration;
import cn.herodotus.engine.core.autoconfigure.jackson2.Jackson2AutoConfiguration;
import com.alicp.jetcache.autoconfigure.JetCacheAutoConfiguration;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * <p>Description: 缓存相关模块统一自动配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/4/18 23:51
 */

@AutoConfiguration(after = {Jackson2AutoConfiguration.class, JetCacheAutoConfiguration.class})
@Import({
        CacheJetCacheConfiguration.class,
        CacheRedissonConfiguration.class,
})
public class CacheAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(CacheAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Starter [Cache] Configure.");
    }
}