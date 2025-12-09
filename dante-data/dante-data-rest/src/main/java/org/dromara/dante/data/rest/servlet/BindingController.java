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

package org.dromara.dante.data.rest.servlet;

import org.dromara.dante.core.domain.BaseEntity;
import org.dromara.dante.core.domain.Result;
import org.dromara.dante.data.commons.service.BaseService;

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
