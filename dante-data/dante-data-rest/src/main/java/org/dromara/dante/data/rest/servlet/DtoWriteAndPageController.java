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

import org.dromara.dante.core.domain.BaseDomain;
import org.dromara.dante.core.domain.BaseEntity;
import org.dromara.dante.core.domain.Result;
import org.dromara.dante.data.commons.service.BaseWriteAndPageService;
import org.springframework.core.convert.converter.Converter;

import java.io.Serializable;

/**
 * <p>Description: 基于 Dto 实体的与 Service 绑定 Controller </p>
 * <p>
 * 阻塞式环境（Servlet）与具体 Service 绑定的 Controller 通用定义。
 * 请求参数实体和响应结果实体均是继承 {@link BaseDomain} 的实体。需要实现 Dto 与 Entity 的转换
 *
 * @param <I>  请求参数实体
 * @param <O>  响应结果实体
 * @param <E>  实体
 * @param <ID> 实体 ID
 * @param <S>  Service
 * @author : gengwei.zheng
 * @date : 2025/3/29 23:02
 */
public interface DtoWriteAndPageController<I extends BaseDomain, O extends BaseDomain, E extends BaseEntity, ID extends Serializable, S extends BaseWriteAndPageService<E, ID>> extends DtoPageableController<O, E, ID, S> {

    /**
     * 将请求参数实体转为 Spring Data 实体的转换器
     *
     * @return 转换器
     */
    Converter<I, E> getToEntityConverter();

    /**
     * 保存或更新实体
     *
     * @param domain 实体参数
     * @return 用 Result 包装的实体
     */
    default Result<O> save(I domain) {
        E entity = getToEntityConverter().convert(domain);
        E savedDomain = getService().save(entity);
        O response = getToDtoConverter().convert(savedDomain);
        return result(response);
    }

    /**
     * 删除数据
     *
     * @param id 实体 ID
     * @return 包装成 {@link Result} 的 String 类型查询结果。JPA 删除操作没有返回值，所以无法判断操作成功与否。
     */
    default Result<String> delete(ID id) {
        Result<String> result = result(String.valueOf(id));
        getService().deleteById(id);
        return result;
    }
}
