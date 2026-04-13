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

package org.dromara.dante.core.support.file;

import org.dromara.dante.core.constant.FileExtensions;
import org.dromara.dante.core.utils.WellFormedUtils;

import java.io.IOException;
import java.nio.file.Path;

/**
 * <p>Description: JsonSchema文件管理器 </p>
 * <p>
 * 多定义了一层 {@link JsonSchemaFileManager} 是为了方便进行跨模块的调用与定义。
 * 因为需要在 Herodotus Cloud 项目的 rpc-client-oss-spring-boot-starter 模块中，定义灵活启用对象存储机制。如果 rpc-client-oss-spring-boot-starter 模块中直接定义 JsonSchema {@link CompositeFileManager} 的实现类，其它模块中特别是物联网模块中无法进行调用。
 * <p>
 * 同时还要方便与 CertificateManager 的实现类进行区分。
 * <p>
 * 该定义还没有找到更合理的放置位置，如果后期有物联网相关的模块移动至 Herodotus Stirrup 中，再进行移动。暂时先放置在当前模块中。
 *
 * @author : gengwei.zheng
 * @date : 2025/5/19 15:06
 */
public interface JsonSchemaFileManager extends CompositeFileManager {

    @Override
    default Path getPath(String filename) throws IOException {
        // 如果文件名中没有以 .json 结尾，那么就补充上。
        String name = WellFormedUtils.robustness(filename, FileExtensions.SUFFIX_JSON, false, true);
        return CompositeFileManager.super.getPath(name);
    }
}
