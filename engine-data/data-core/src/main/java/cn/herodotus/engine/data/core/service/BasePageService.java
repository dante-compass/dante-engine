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

package cn.herodotus.engine.data.core.service;

import cn.herodotus.engine.core.definition.domain.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Description: Spring Data 生态支持 Page 方式分页的基础 Service </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/3/29 18:30
 */
public interface BasePageService<E extends BaseEntity, ID extends Serializable> extends BaseService<E, ID> {

    /**
     * 排序查询全部
     *
     * @param sort {@link Sort}
     * @return 全部数据列表
     */
    default List<E> findAll(Sort sort) {
        throw new UnsupportedOperationException();
    }

    /**
     * 分页查询
     *
     * @param pageable {@link Pageable}
     * @return 分页数据
     */
    default Page<E> findByPage(Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    /**
     * 查询分页数据
     *
     * @param pageNumber 当前页码, 起始页码 0
     * @param pageSize   每页显示的数据条数
     * @return 分页数据
     */
    default Page<E> findByPage(int pageNumber, int pageSize) {
        return findByPage(PageRequest.of(pageNumber, pageSize));
    }

    /**
     * 查询分页数据
     *
     * @param pageNumber 当前页码, 起始页码 0
     * @param pageSize   每页显示的数据条数
     * @param sort       {@link Sort}
     * @return 分页数据
     */
    default Page<E> findByPage(int pageNumber, int pageSize, Sort sort) {
        return findByPage(PageRequest.of(pageNumber, pageSize, sort));
    }

    /**
     * 查询分页数据
     *
     * @param pageNumber 当前页码, 起始页码 0
     * @param pageSize   每页显示的数据条数
     * @param direction  {@link Sort.Direction}
     * @param properties 排序的属性名称, 可以多个
     * @return 分页数据
     */
    default Page<E> findByPage(int pageNumber, int pageSize, Sort.Direction direction, String... properties) {
        return findByPage(PageRequest.of(pageNumber, pageSize, direction, properties));
    }
}
