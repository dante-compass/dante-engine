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

package cn.herodotus.engine.oauth2.authorization.autoconfigure.listener;

import cn.herodotus.engine.message.core.domain.RestMapping;
import cn.herodotus.engine.message.core.event.RestMappingGatherEvent;
import cn.herodotus.engine.oauth2.authorization.autoconfigure.processor.AttributeTransmitterDistributeProcessor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>Description: 本地RequestMapping收集监听 </p>
 * <p>
 * 主要在单体式架构，以及 UUA 服务自身使用
 *
 * @author : gengwei.zheng
 * @date : 2021/8/8 22:02
 */
@Component
public class LocalRestMappingGatherListener implements ApplicationListener<RestMappingGatherEvent> {

    private static final Logger log = LoggerFactory.getLogger(LocalRestMappingGatherListener.class);

    private final AttributeTransmitterDistributeProcessor attributeTransmitterDistributeProcessor;

    public LocalRestMappingGatherListener(AttributeTransmitterDistributeProcessor attributeTransmitterDistributeProcessor) {
        this.attributeTransmitterDistributeProcessor = attributeTransmitterDistributeProcessor;
    }

    @Override
    public void onApplicationEvent(RestMappingGatherEvent event) {

        log.info("[Herodotus] |- Rest mapping gather LOCAL listener, response event!");

        List<RestMapping> restMappings = event.getData();
        if (CollectionUtils.isNotEmpty(restMappings)) {
            log.debug("[Herodotus] |- [R4] Request mapping process BEGIN!");
            attributeTransmitterDistributeProcessor.postRestMappings(restMappings);
        }
    }
}
