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

package cn.herodotus.engine.web.service.initializer;

import cn.herodotus.engine.core.definition.builder.EnumDictionaryBuilder;
import cn.herodotus.engine.core.definition.domain.Dictionary;
import cn.herodotus.engine.message.core.definition.strategy.EnumDictionaryGatherEventManager;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.util.List;

/**
 * <p>Description: 枚举数据字典收集器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/8/23 17:09
 */
public class EnumDictionaryGather implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger log = LoggerFactory.getLogger(EnumDictionaryGather.class);

    private final EnumDictionaryBuilder enumDictionaryBuilder;
    private final EnumDictionaryGatherEventManager enumDictionaryGatherEventManager;

    public EnumDictionaryGather(EnumDictionaryBuilder enumDictionaryBuilder, EnumDictionaryGatherEventManager enumDictionaryGatherEventManager) {
        this.enumDictionaryBuilder = enumDictionaryBuilder;
        this.enumDictionaryGatherEventManager = enumDictionaryGatherEventManager;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        log.debug("[Herodotus] |- [E1] Application is READY, start to scan enum dictionary!");

        List<Dictionary> dictionaries = enumDictionaryBuilder.getDictionaries();

        if (CollectionUtils.isNotEmpty(dictionaries)) {
            log.debug("[Herodotus] |- [E2] Enum dictionary scan found [{}] resources in current service, go to next stage!", dictionaries.size());
            enumDictionaryGatherEventManager.postProcess(dictionaries);
        } else {
            log.debug("[Herodotus] |- [E2] Enum dictionary scan can not find any resources in current service!");
        }

        log.info("[Herodotus] |- Enum dictionary scan for current service FINISHED!");
    }
}
