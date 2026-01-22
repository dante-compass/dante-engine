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

package org.dromara.dante.data.commons.service;

import org.dromara.dante.core.domain.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * <p>Description: 基于 Spring Data 生态的核心 Service 定义 </p>
 * <p>
 * 该接口定义适配 Spring Data 生态所有组件的通用“读”操作，即所有 Spring Data 组件均支持的“读”操作，以便于支持 Spring Data 不同类型数据源的切换。
 * 将“读”操作单独提取出来，主要是为了解决 JPA 对视图（View）的安全性操作。以避免对视图（View）的增、删、改操作
 *
 * @author : gengwei.zheng
 * @date : 2025/3/29 16:01
 */
public interface BaseReadableService<E extends BaseEntity, ID extends Serializable> {

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
     * @param id 数据 ID
     * @return true 存在，false 不存在
     */
    default boolean existsById(ID id) {
        throw new UnsupportedOperationException();
    }
}
