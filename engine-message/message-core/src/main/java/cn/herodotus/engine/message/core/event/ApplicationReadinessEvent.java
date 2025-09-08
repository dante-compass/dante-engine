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

package cn.herodotus.engine.message.core.event;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * <p>Description: 应用准就绪 Event </p>
 * <p>
 * Spring Boot 生命周期事件中，在启动过程最后一个事件就是 {@link ApplicationReadyEvent}。在大多数场景下，需要在服务启动之后做一些操作，使用这个事件就足够了。
 * 结合 Herodotus Cloud 自身需求。有些场景的操作需要在 RequestMapping Scan 完成，并且已经正确存储了数据之后，再进行。所以 {@link ApplicationReadyEvent} 就无法满足需求。
 * <p>
 * 定义 {@link ApplicationReadinessEvent} 事件。这个事件是在 RequestMapping 扫描完成，并且 <code>cn.herodotus.engine.oauth2.authorization.autoconfigure.processor.AttributeTransmitterDistributeProcessor</code> 已经正确存储了数据后才会发出。
 * <p>
 * 如果是在微服务环境下，不同服务发送 RequestMapping 数据时机不同，会出现发送多个 {@link ApplicationReadinessEvent} 情况。所以目前仅在单体环境下开启。
 *
 * @author : gengwei.zheng
 * @date : 2025/3/2 22:35
 */
public class ApplicationReadinessEvent extends ApplicationEvent {

    public ApplicationReadinessEvent(Object source) {
        super(source);
    }

    public ApplicationReadinessEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
