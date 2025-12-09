/*
 * Copyright 2020-2030 码匠君<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 是 Dante Cloud 系统核心组件库，采用 APACHE LICENSE 2.0 开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1. 请不要删除和修改根目录下的LICENSE文件。
 * 2. 请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3. 请保留源码和相关描述文件的项目出处，作者声明等。
 * 4. 分发源码时候，请注明软件出处 <https://gitee.com/dromara/dante-cloud>
 * 5. 在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/dromara/dante-cloud>
 * 6. 若您的项目无法满足以上几点，可申请商业授权
 */

package org.dromara.dante.data.mongodb.service;

import org.dromara.dante.data.commons.service.BasePageService;
import org.dromara.dante.data.mongodb.repository.BaseMongoRepository;
import org.dromara.dante.core.domain.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * <p>Description: MongoDB 基础 Service 定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/12/19 15:26
 */
public interface BaseMongoService<E extends BaseEntity, ID extends Serializable> extends BasePageService<E, ID> {

    /**
     * 获取对应的 Mongo Repository
     *
     * @return {@link BaseMongoRepository}
     */
    BaseMongoRepository<E, ID> getRepository();

    /**
     * 查询全部
     *
     * @return 全部数据列表
     */
    @Override
    default List<E> findAll() {
        return getRepository().findAll();
    }

    /**
     * 分页查询
     *
     * @param pageable {@link Pageable}
     * @return 分页数据
     */
    @Override
    default Page<E> findByPage(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    /**
     * 根据 ID 查询
     *
     * @param id ID
     * @return 数据对象
     */
    @Override
    default Optional<E> findById(ID id) {
        return getRepository().findById(id);
    }

    /**
     * 查询多个 ID 对应的数据
     *
     * @param ids {@link Iterable}
     * @return 数据对象 {@link List}
     */
    @Override
    default List<E> findAllById(Iterable<ID> ids) {
        return getRepository().findAllById(ids);
    }

    /**
     * 数据是否存在
     *
     * @param id 数据ID
     * @return true 存在，false 不存在
     */
    @Override
    default boolean existsById(ID id) {
        return getRepository().existsById(id);
    }

    /**
     * 保存或更新数据
     *
     * @param domain 对应的实体
     * @return 保存后的实体
     */
    @Override
    default E save(E domain) {
        return getRepository().save(domain);
    }

    /**
     * 批量保存数据
     *
     * @param entities 数据实体 {@link Iterable}
     * @return 保存后的实体集合 {@link List}
     */
    @Override
    default List<E> saveAll(Iterable<E> entities) {
        return getRepository().saveAll(entities);
    }

    /**
     * 删除实体对应的数据
     *
     * @param domain 数据对象实体
     */
    @Override
    default void delete(E domain) {
        getRepository().delete(domain);
    }

    /**
     * 根据 ID 删除
     *
     * @param id ID
     */
    @Override
    default void deleteById(ID id) {
        getRepository().deleteById(id);
    }

    /**
     * 清空全部数据
     */
    @Override
    default void deleteAll() {
        getRepository().deleteAll();
    }

    /**
     * 批量删除指定的数据
     *
     * @param entities 数据实体 {@link Iterable}
     */
    @Override
    default void deleteAll(Iterable<E> entities) {
        getRepository().deleteAll(entities);
    }

    /**
     * 根据指定的 ID 批量删除数据。
     *
     * @param ids {@link Iterable}
     */
    @Override
    default void deleteAllById(Iterable<? extends ID> ids) {
        getRepository().deleteAllById(ids);
    }

    /**
     * 排序查询全部
     *
     * @param sort {@link Sort}
     * @return 全部数据列表
     */
    @Override
    default List<E> findAll(Sort sort) {
        return getRepository().findAll(sort);
    }
}
