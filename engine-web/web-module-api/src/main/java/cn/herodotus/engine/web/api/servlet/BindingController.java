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
import cn.herodotus.engine.data.core.service.BaseService;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * <p>Description: 与 Service 绑定 Controller </p>
 *
 * @param <E>  实体
 * @param <ID> 实体 ID
 * @param <S>  Service
 * @author : gengwei.zheng
 * @date : 2025/3/29 23:02
 */
public interface BindingController<E extends BaseEntity, ID extends Serializable, S extends BaseService<E, ID>> extends PaginationController {

    /**
     * 获取 Service
     *
     * @return Service
     */
    S getService();

    /**
     * 查询所有数据
     *
     * @return 包装成 {@link Result} 的 {@link List} 类型查询结果
     */
    default Result<List<E>> findAll() {
        List<E> domains = getService().findAll();
        return result(domains);
    }

    /**
     * 根据实体 ID 查询指定实体数据
     *
     * @param id 实体Id
     * @return 装成 {@link Result} 的查询结果
     */
    default Result<E> findById(ID id) {
        Optional<E> domain = getService().findById(id);
        return result(domain.orElse(null));
    }

    /**
     * 保存或更新实体
     *
     * @param domain 实体参数
     * @return 用Result包装的实体
     */
    default Result<E> save(E domain) {
        E savedDomain = getService().save(domain);
        return result(savedDomain);
    }

    /**
     * 删除数据
     *
     * @param id 实体ID
     * @return 包装成 {@link Result} 的 String 类型查询结果。JPA 删除操作没有返回值，所以无法判断操作成功与否。
     */
    default Result<String> delete(ID id) {
        Result<String> result = result(String.valueOf(id));
        getService().deleteById(id);
        return result;
    }
}
