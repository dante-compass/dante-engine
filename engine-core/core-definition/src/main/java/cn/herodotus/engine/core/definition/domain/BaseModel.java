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

package cn.herodotus.engine.core.definition.domain;

/**
 * <p>Description: 系统对象通用定义 </p>
 * <p>
 * 主要用于域对象的定义。
 * 与 {@link BaseEntity} 的区别是 {@link BaseEntity} 主要面向存储层，简单的说其对应的字段与数据库中的字段对应。
 * 与 {@link BaseDto} 的区别是 {@link BaseDto} 主要面向接口层
 *
 * @author : gengwei.zheng
 * @date : 2025/3/29 16:39
 */
public non-sealed interface BaseModel extends BaseDomain {
}
