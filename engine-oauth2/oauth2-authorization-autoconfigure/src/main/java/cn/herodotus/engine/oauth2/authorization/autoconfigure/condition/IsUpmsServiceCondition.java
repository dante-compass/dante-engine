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

package cn.herodotus.engine.oauth2.authorization.autoconfigure.condition;

import cn.herodotus.engine.core.foundation.condition.ConditionalOnServletApplication;
import cn.herodotus.engine.rest.servlet.upms.config.RestServletUpmsConfiguration;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

/**
 * <p>Description: 判断是否为 Upms 服务条件 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/10/27 20:45
 */
public final class IsUpmsServiceCondition extends AllNestedConditions {

    public IsUpmsServiceCondition() {
        super(ConfigurationPhase.PARSE_CONFIGURATION);
    }

    @ConditionalOnClass(RestServletUpmsConfiguration.class)
    static final class OnRestServletService {

    }

    @ConditionalOnServletApplication
    static final class OnServletApplication {

    }
}
