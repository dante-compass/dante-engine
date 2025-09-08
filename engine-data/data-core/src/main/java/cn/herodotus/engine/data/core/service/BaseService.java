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

package cn.herodotus.engine.data.core.service;

import cn.herodotus.engine.core.definition.domain.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * <p>Description: 基于 Spring Data 生态的核心 Service 定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/3/29 16:01
 */
public interface BaseService<E extends BaseEntity, ID extends Serializable> {

    /**
     * 查询全部
     *
     * @return 全部数据列表
     */
    default List<E> findAll() {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据 ID 查询
     *
     * @param id ID
     * @return 数据对象
     */
    default Optional<E> findById(ID id) {
        throw new UnsupportedOperationException();
    }


    /**
     * 查询多个 ID 对应的数据
     *
     * @param ids {@link Iterable}
     * @return 数据对象 {@link List}
     */
    default List<E> findAllById(Iterable<ID> ids) {
        throw new UnsupportedOperationException();
    }

    /**
     * 数据是否存在
     *
     * @param id 数据ID
     * @return true 存在，false 不存在
     */
    default boolean existsById(ID id) {
        throw new UnsupportedOperationException();
    }

    /**
     * 保存或更新数据
     *
     * @param domain 对应的实体
     * @return 保存后的实体
     */
    default E save(E domain) {
        throw new UnsupportedOperationException();
    }

    /**
     * 批量保存数据
     *
     * @param entities 数据实体 {@link Iterable}
     * @return 保存后的实体集合 {@link List}
     */
    default List<E> saveAll(Iterable<E> entities) {
        throw new UnsupportedOperationException();
    }

    /**
     * 删除实体对应的数据
     *
     * @param domain 数据对象实体
     */
    default void delete(E domain) {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据 ID 删除
     *
     * @param id ID
     */
    default void deleteById(ID id) {
        throw new UnsupportedOperationException();
    }

    /**
     * 清空全部数据
     */
    default void deleteAll() {
        throw new UnsupportedOperationException();
    }

    /**
     * 批量删除指定的数据
     *
     * @param entities 数据实体 {@link Iterable}
     */
    default void deleteAll(Iterable<E> entities) {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据指定的 ID 批量删除数据。
     *
     * @param ids {@link Iterable}
     */
    default void deleteAllById(Iterable<? extends ID> ids) {
        throw new UnsupportedOperationException();
    }
}
