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
 * along with this program.  If not, see <https://www.herodotus.cn>.
 */

package org.dromara.dante.oauth2.commons.enums;

import org.dromara.dante.oauth2.commons.constant.OAuth2Constants;
import org.dromara.dante.spring.condition.ConditionEnum;
import org.springframework.core.env.Environment;

/**
 * <p>Description: 系统核心数据数据存储模式 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/12/20 22:56
 */
public enum SysPersistence implements ConditionEnum {

    /**
     * 使用 JPA 作为 SYS 核心数据存储介质。
     */
    JPA {
        @Override
        public boolean isActive(Environment environment) {
            return isDefault(environment, OAuth2Constants.ITEM_PERSISTENCE_SYS);
        }

        @Override
        public String getConstant() {
            return name();
        }
    },

    /**
     * 使用 CASSANDRA 作为 SYS 核心数据存储介质。
     */
    CASSANDRA {
        @Override
        public boolean isActive(Environment environment) {
            return isActive(environment, OAuth2Constants.ITEM_PERSISTENCE_SYS);
        }

        @Override
        public String getConstant() {
            return name();
        }
    }
}
