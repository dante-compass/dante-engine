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

package cn.herodotus.dante.autoconfigure.jackson.initializer;

import cn.herodotus.dante.core.jackson.JacksonUtils;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

/**
 * <p>Description: Jackson 初始化器 </p>
 * <p>
 * 在 3.5.0.0 及以前版本中，{@link JacksonUtils} 存在以下问题：
 * <p>
 * 问题一：原有 {@link JacksonUtils} 是采用一个技巧：向静态类中注入 {@link ObjectMapper} 以实现{@link JacksonUtils}类中的 {@link ObjectMapper} 是系统配置化的后的对象，以保证整个系统 Jackson 操作的统一。
 * 1. 这种实现方式看着很怪，不够优雅
 * 2. 工具类不像工具类，Bean 不像 Bean
 * <p>
 * 问题二：这就使得 {@link JacksonUtils} 必须依赖于 Spring 环境
 * 1. 因为问题一的方式，导致 {@link JacksonUtils} 引入了 Spring 注解，所以必须依赖于 Spring 环境。在非 Spring 环境下就无法使用 {@link JacksonUtils}。
 * 2. 这就使得 {@link JacksonUtils} 必须放置在 core-foundation 模块中，
 * 3. 那么导致想要使用 {@link JacksonUtils} 必须依赖 core-foundation 模块中，间接引入 core-definition 模块。多了一层引用。
 * <p>
 * 定义该初始化器的目的主要是为了解决这个问题问题：
 * 1. 将 {@link JacksonUtils} 变为纯净的工具类
 * 2. 将 {@link JacksonUtils} 移动到 core-definition 模块，这样在非 Spring 环境下也可以直接使用 {@link JacksonUtils}
 * 3. 系统启动之后，将系统组装配置后的 {@link ObjectMapper} 设置到 {@link JacksonUtils} 中，以实现整个系统体系 Jackson ObjectMapper 的统一化配置 。
 *
 * @author : gengwei.zheng
 * @date : 2025/6/10 12:34
 */
@Component
public class JacksonInitializer {

    private final ObjectMapper objectMapper;

    public JacksonInitializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        JacksonUtils.setObjectMapper(objectMapper);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
