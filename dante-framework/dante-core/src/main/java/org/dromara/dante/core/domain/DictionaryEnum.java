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

package org.dromara.dante.core.domain;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>Description: 数据字典定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/8/23 12:26
 */
public interface DictionaryEnum {

    /**
     * 枚举的实际值，用于 Jackson 确定枚举类型以及实际的交互值，以实现反序列化。
     * <p>
     * · String 类型的 Integer 对应枚举的 ordinal() 值
     * · String 类型对应枚举的 name() 值
     *
     * @return 数据字典枚举的值。目前主要为 Integer 或 String。
     */
    @JsonValue
    String getValue();

    /**
     * 枚举选项的展示值。主要用于前端展示信息。
     *
     * @return 展示值
     */
    String getLabel();

    default Dictionary getDictionary(String name, int ordinal) {
        Dictionary dictionary = new Dictionary();
        dictionary.setClazz(this.getClass().getName());
        dictionary.setCategory(this.getClass().getSimpleName());
        dictionary.setOrdinal(ordinal);
        dictionary.setName(name);
        dictionary.setLabel(getLabel());
        dictionary.setValue(getValue());
        return dictionary;
    }
}
