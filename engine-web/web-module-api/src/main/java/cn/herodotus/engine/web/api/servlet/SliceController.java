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
import cn.herodotus.engine.core.definition.domain.Result;
import cn.herodotus.engine.data.core.service.BaseSliceService;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>Description: {@link Slice} 类型分页基础 Controller </p>
 * <p>
 * 多定义一层接口，用于区分 {@link Slice} 类型。
 *
 * @param <E>  实体
 * @param <ID> 实体 ID
 * @param <S>  Service
 * @author : gengwei.zheng
 * @date : 2025/3/30 17:05
 */
public interface SliceController<E extends BaseEntity, ID extends Serializable, S extends BaseSliceService<E, ID>> extends BindingController<E, ID, S> {

    /**
     * 查询分页数据
     *
     * @param pageNumber 当前页码，起始页码 0
     * @param pageSize   每页显示数据条数
     * @return 包装成 {@link Result} 的查询结果
     */
    default Result<Map<String, Object>> findByPage(Integer pageNumber, Integer pageSize) {
        Slice<E> data = getService().findByPage(pageNumber, pageSize);
        return resultFromSlice(data);
    }

    /**
     * 查询分页数据
     *
     * @param pageNumber 当前页码, 起始页码 0
     * @param pageSize   每页显示的数据条数
     * @param direction  排序方向 {@link Sort.Direction}
     * @param properties 需要排序的字段
     * @return 包装成 {@link Result} 的 {@link Map} 类型查询结果
     */
    default Result<Map<String, Object>> findByPage(Integer pageNumber, Integer pageSize, Sort.Direction direction, String... properties) {
        Slice<E> data = getService().findByPage(pageNumber, pageSize, direction, properties);
        return resultFromSlice(data);
    }
}
