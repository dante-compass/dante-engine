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

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * <p>Description: {@link Conditional @Conditional} 当指定的 OAuth2 Token 格式属性配置时条件匹配</p>
 *
 * @author : gengwei.zheng
 * @date : 2024/12/9 22:42
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnTokenFormatCondition.class)
public @interface ConditionalOnTokenFormat {

    /**
     * {@link TokenFormat accessTokenFormat} 属性必须配置.
     *
     * @return 预期的 AccessToken 格式
     */
    TokenFormat value();
}
