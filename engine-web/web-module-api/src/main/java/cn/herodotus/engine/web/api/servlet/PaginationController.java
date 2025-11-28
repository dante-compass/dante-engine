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

import cn.herodotus.dante.core.domain.BaseEntity;
import cn.herodotus.dante.core.domain.Result;
import cn.herodotus.engine.web.core.definition.Controller;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.Map;

/**
 * <p> Description : 可分页基础 Controller </p>
 * <p>
 * 提供 {@link Page} 和 {@link Slice} 分页结果返回处理
 *
 * @author : gengwei.zheng
 * @date : 2020/4/29 18:56
 */
public interface PaginationController extends Controller {

    /**
     * 数据分页对象转换为统一响应实体
     *
     * @param pages 分页查询结果 {@link Page}
     * @param <E>   {@link BaseEntity} 子类型
     * @return {@link Result} Map
     */
    default <E extends BaseEntity> Result<Map<String, Object>> resultFromPage(Page<E> pages) {
        if (null == pages) {
            return Result.failure("查询数据失败！");
        }

        if (CollectionUtils.isNotEmpty(pages.getContent())) {
            return Result.success("查询数据成功！", fromPage(pages));
        } else {
            return Result.empty("未查询到数据！");
        }
    }

    /**
     * 数据切片对象转换为统一响应实体
     *
     * @param slices 分页查询结果 {@link Slice}
     * @param <E>    {@link BaseEntity} 子类型
     * @return {@link Result} Map
     */
    default <E extends BaseEntity> Result<Map<String, Object>> resultFromSlice(Slice<E> slices) {
        if (null == slices) {
            return Result.failure("查询数据失败！");
        }

        if (CollectionUtils.isNotEmpty(slices.getContent())) {
            return Result.success("查询数据成功！", fromSlice(slices));
        } else {
            return Result.empty("未查询到数据！");
        }
    }

    /**
     * Page 对象转换为 Map
     *
     * @param pages 分页查询结果 {@link Page}
     * @param <E>   {@link BaseEntity} 子类型
     * @return Map
     */
    default <E extends BaseEntity> Map<String, Object> fromPage(Page<E> pages) {
        return with(pages.getContent(), pages.getTotalPages(), pages.getTotalElements());
    }

    /**
     * Slice 对象转换为 Map
     *
     * @param slices 分页查询结果 {@link Slice}
     * @param <E>    {@link BaseEntity} 子类型
     * @return Map
     */
    default <E extends BaseEntity> Map<String, Object> fromSlice(Slice<E> slices) {
        return with(slices.getContent(), slices.hasNext(), slices.hasPrevious());
    }
}
