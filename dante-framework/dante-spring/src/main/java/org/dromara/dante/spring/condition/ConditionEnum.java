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

package org.dromara.dante.spring.condition;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.core.env.Environment;

/**
 * <p>Description: Spring 条件枚举 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/12/10 13:41
 */
public interface ConditionEnum {

    /**
     * 检测是否已经配置
     *
     * @param environment {@link Environment}
     * @return 否加密策略属性已经配置
     */
    boolean isActive(Environment environment);

    String getConstant();

    /**
     * 获取指定配置的 String 类型的值
     *
     * @param environment {@link Environment}
     * @param property    配置
     * @return 返回字符串
     */
    default String getString(Environment environment, String property) {
        return environment.getProperty(property, String.class);
    }

    /**
     * 属性是否已经配置。主要指设置了默认值以外的值。
     *
     * @param environment {@link Environment}
     * @param property    配置
     * @return 是否已经配置
     */
    default boolean isActive(Environment environment, String property) {
        String value = getString(environment, property);
        return StringUtils.isNotBlank(value) && Strings.CI.equals(value, getConstant());
    }

    /**
     * 是否为默认值
     *
     * @param environment {@link Environment}
     * @param property    配置
     * @return 是否为默认值
     */
    default boolean isDefault(Environment environment, String property) {
        String value = getString(environment, property);
        return StringUtils.isBlank(value) || Strings.CI.equals(value, getConstant());
    }
}
