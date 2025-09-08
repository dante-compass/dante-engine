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

package cn.herodotus.engine.web.api.servlet;

import cn.herodotus.engine.core.definition.domain.BaseEntity;
import cn.herodotus.engine.core.definition.domain.Result;
import cn.herodotus.engine.data.core.service.BasePageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>Description: {@link Page} 类型分页基础 Controller </p>
 * <p>
 * 多定义一层接口，用于区分 {@link Page} 类型。
 *
 * @param <E>  实体
 * @param <ID> 实体 ID
 * @param <S>  Service
 * @author : gengwei.zheng
 * @date : 2025/3/30 17:21
 */
public interface PageController<E extends BaseEntity, ID extends Serializable, S extends BasePageService<E, ID>> extends BindingController<E, ID, S> {

    /**
     * 查询分页数据
     *
     * @param pageNumber 当前页码，起始页码 0
     * @param pageSize   每页显示数据条数
     * @return 包装成 {@link Result} 的查询结果
     */
    default Result<Map<String, Object>> findByPage(Integer pageNumber, Integer pageSize) {
        Page<E> data = getService().findByPage(pageNumber, pageSize);
        return resultFromPage(data);
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
        Page<E> data = getService().findByPage(pageNumber, pageSize, direction, properties);
        return resultFromPage(data);
    }
}
