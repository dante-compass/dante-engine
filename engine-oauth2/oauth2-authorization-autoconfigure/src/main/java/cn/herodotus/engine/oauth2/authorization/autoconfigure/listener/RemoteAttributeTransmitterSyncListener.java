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

import cn.herodotus.engine.core.definition.utils.Jackson2Utils;
import cn.herodotus.engine.core.identity.domain.AttributeTransmitter;
import cn.herodotus.engine.oauth2.authorization.autoconfigure.bus.RemoteAttributeTransmitterSyncEvent;
import cn.herodotus.engine.oauth2.authorization.processor.SecurityAttributeAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.bus.ServiceMatcher;
import org.springframework.context.ApplicationListener;

import java.util.Optional;

/**
 * <p>Description: Security Metadata 数据同步监听 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/8/6 12:23
 */
public class RemoteAttributeTransmitterSyncListener implements ApplicationListener<RemoteAttributeTransmitterSyncEvent> {

    private static final Logger log = LoggerFactory.getLogger(RemoteAttributeTransmitterSyncListener.class);

    private final SecurityAttributeAnalyzer securityAttributeAnalyzer;
    private final ServiceMatcher serviceMatcher;

    public RemoteAttributeTransmitterSyncListener(SecurityAttributeAnalyzer securityAttributeAnalyzer, ServiceMatcher serviceMatcher) {
        this.securityAttributeAnalyzer = securityAttributeAnalyzer;
        this.serviceMatcher = serviceMatcher;
    }

    @Override
    public void onApplicationEvent(RemoteAttributeTransmitterSyncEvent event) {

        if (!serviceMatcher.isFromSelf(event)) {
            log.info("[Herodotus] |- Remote attribute transmitter sync listener, response service [{}] event!", event.getOriginService());

            String data = event.getData();

            log.debug("[Herodotus] |- Got attribute transmitter from service [{}], current [{}] start to process security attributes.", event.getOriginService(), event.getDestinationService());

            Optional.ofNullable(data)
                    .flatMap(value -> Optional.ofNullable(Jackson2Utils.toList(value, AttributeTransmitter.class)))
                    .ifPresent(securityAttributeAnalyzer::processAttributeTransmitters);
        }
    }
}
