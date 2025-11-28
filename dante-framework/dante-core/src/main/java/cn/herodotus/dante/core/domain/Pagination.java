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

package cn.herodotus.dante.core.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: Open API 响应返回内容统一定义</p>
 *
 * @author : gengwei.zheng
 * @date : 2024/5/4 10:49
 */
public non-sealed interface Pagination extends BaseDomain {

    /**
     * Page 对象转换为 Map
     *
     * @param content       数据实体 List
     * @param totalPages    分页总页数
     * @param totalElements 总的数据条目
     * @param <D>           {@link BaseDomain} 子类型
     * @return Map
     */
    default <D extends BaseDomain> Map<String, Object> with(List<D> content, int totalPages, long totalElements) {
        Map<String, Object> result = new HashMap<>(8);
        result.put("content", content);
        result.put("totalPages", totalPages);
        result.put("totalElements", totalElements);
        return result;
    }

    /**
     * Slice 对象信息转换为 Map
     *
     * @param content     数据实体 List
     * @param hasNext     是否有上一页
     * @param hasPrevious 是否有下一页
     * @param <D>         {@link BaseDomain} 子类型
     * @return Map
     */
    default <D extends BaseDomain> Map<String, Object> with(List<D> content, boolean hasNext, boolean hasPrevious) {
        Map<String, Object> result = new HashMap<>(8);
        result.put("content", content);
        result.put("hasNext", hasNext);
        result.put("hasPrevious", hasPrevious);
        return result;
    }
}
