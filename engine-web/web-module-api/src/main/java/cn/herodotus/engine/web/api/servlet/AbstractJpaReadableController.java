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

package cn.herodotus.engine.web.api.servlet;

import cn.herodotus.engine.core.definition.domain.BaseEntity;
import cn.herodotus.engine.data.core.jpa.service.BaseJpaReadableService;

import java.io.Serializable;

/**
 * <p>Description: Jpa 只读基础 Controller 定义 </p>
 * <p>
 * 多定义一层抽象类，用于指定 {@link BaseJpaReadableService} 类型，方便子类更加精确的定位类型。
 *
 * @param <E>  实体
 * @param <ID> 实体 ID
 * @author : gengwei.zheng
 * @date : 2021/7/5 17:21
 */
public abstract class AbstractJpaReadableController<E extends BaseEntity, ID extends Serializable> extends AbstractReadableController<E, ID, BaseJpaReadableService<E, ID>> {

}
