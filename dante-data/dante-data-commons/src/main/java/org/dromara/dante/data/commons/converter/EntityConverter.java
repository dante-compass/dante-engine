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

package org.dromara.dante.data.commons.converter;

import org.springframework.core.convert.converter.Converter;

/**
 * <p>Description: 多 Spring Data Module 实体转换器定义 </p>
 *
 * @param <S> 转换源对象。不同的 Spring Data Module 对应的实体。
 * @param <T> 转换目标对象
 * @author : gengwei.zheng
 * @date : 2025/4/17 16:28
 */

public interface EntityConverter<S, T> extends Converter<S, T> {

    /**
     * 构造转换结果实体的实例
     *
     * @return 结果实体实例
     */
    T getInstance();

    /**
     * 转换子类中的专有属性
     *
     * @param source 转换源对象
     * @param target 转换目标对象
     */
    void prepare(S source, T target);

    /**
     * 转换父类中共性属性
     *
     * @param source 转换源对象
     * @param target 转换目标对象
     */
    void then(S source, T target);

    @Override
    default T convert(S source) {
        T target = getInstance();
        prepare(source, target);
        then(source, target);
        return target;
    }
}
