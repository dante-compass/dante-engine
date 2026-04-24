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

package org.dromara.dante.core.support.factory;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.dromara.dante.core.exception.NoHandlerFoundException;

import java.util.Map;
import java.util.Optional;

/**
 * <p>Description: 策略工厂定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/6/9 0:27
 */
public interface StrategyFactory<T> {

    /**
     * 获取处理器映射列表
     *
     * @return 映射关系 {@link Map}
     */
    Map<String, T> getHandlers();

    /**
     * 默认策略处理器名称。在没有指定名称的情况下，默认以该名称对应的处理器进行处理。
     * <p>
     * 默认情况下该方法返回 null，这种情况下 {@link #getHandler()} 将无法返回有效的处理器。
     *
     * @return 默认处理器名称。
     */
    default String getDefaultHandlerName() {
        return null;
    }

    /**
     * 名称转换器。
     * <p>
     * 对于特殊的名称可以进行一定的转换。默认返回 null，以防止随时被调用。
     *
     * @param name 处理器名称
     * @return 转换后的名称
     */
    default String convert(String name) {
        return null;
    }

    /**
     * 查找处理器。如果根据 name 没有找到处理器，则使用 {@link #convert(String)} 对名字转换后再次查找
     *
     * @param name 处理器名称
     * @return Optional类型查找结果 {@link Optional}
     */
    private Optional<T> findHandler(String name) {
        if (StringUtils.isBlank(name)) {
            return Optional.empty();
        }

        return Optional.ofNullable(getHandlers().get(name))
                .or(() -> {
                    String newName = convert(name);
                    if (StringUtils.isNotBlank(newName) && !Strings.CS.equals(newName, name)) {
                        return Optional.ofNullable(getHandlers().get(newName));
                    } else {
                        return Optional.empty();
                    }
                });
    }


    /**
     * 获取默认的处理器
     *
     * @return Optional类型处理器 {@link Optional}
     */
    default Optional<T> getHandler() {
        return Optional.ofNullable(getDefaultHandlerName()).flatMap(this::getHandler);
    }

    /**
     * 根据名称获取处理器
     *
     * @param name 处理器名称
     * @return Optional类型处理器
     */
    default Optional<T> getHandler(String name) {
        return findHandler(name);
    }

    /**
     * 根据名称获取处理器，如果处理器不存在则抛出异常
     *
     * @param name 处理器名称
     * @return 处理器
     */
    default T getHandlerOrThrow(String name) {
        return findHandler(name)
                .orElseThrow(() -> new NoHandlerFoundException("Cannot find handler for " + name));
    }

    /**
     * 获取默认的处理器，如果默认处理器没有配置，或者处理器不存在则抛出异常
     *
     * @return 处理器
     */
    default T getHandlerOrThrow() {
        String defaultName = getDefaultHandlerName();
        if (defaultName == null) {
            throw new NoHandlerFoundException("No default handler name configured");
        }
        return getHandlerOrThrow(defaultName);
    }
}
