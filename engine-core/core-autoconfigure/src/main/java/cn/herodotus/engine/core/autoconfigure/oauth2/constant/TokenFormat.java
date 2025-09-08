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

package cn.herodotus.engine.core.autoconfigure.oauth2.constant;

import cn.herodotus.engine.core.definition.constant.BaseConstants;
import cn.herodotus.engine.core.foundation.condition.ConditionEnum;
import org.springframework.core.env.Environment;

/**
 * <p>Description: Access Token 格式 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/12/9 22:33
 */
public enum TokenFormat implements ConditionEnum {

    /**
     * 一种自包含的、无状态的令牌。无需服务端验证。
     */
    JWT {
        @Override
        public boolean isActive(Environment environment) {
            return isActive(environment, BaseConstants.ITEM_AUTHORIZATION_TOKEN_FORMAT);
        }

        @Override
        public String getConstant() {
            return name();
        }
    },

    /**
     * 不透明令牌。需要发送回授权服务器进行验证
     */
    OPAQUE {
        @Override
        public boolean isActive(Environment environment) {
            return !JWT.isActive(environment);
        }

        @Override
        public String getConstant() {
            return name();
        }
    };
}
