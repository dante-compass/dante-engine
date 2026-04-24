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

package org.dromara.dante.autoconfigure.file;

import org.dromara.dante.core.support.file.FileTemplate;
import org.dromara.dante.core.support.file.JsonSchemaFileManager;
import org.dromara.dante.core.support.file.OssTransformer;

/**
 * <p>Description: 默认的 JsonSchema文件管理器 </p>
 * <p>
 * 新加默认的 {@link DefaultJsonSchemaFileManager} 保证在没有添加 rpc-client-oss-spring-boot-starter 模块时仍旧可以使用 {@link JsonSchemaFileManager}。这种情况下对象存储的操作将会失效，仅能操作本地文件。
 *
 * @author : gengwei.zheng
 * @date : 2025/5/20 14:17
 */
public class DefaultJsonSchemaFileManager implements JsonSchemaFileManager {

    private final FileProperties fileProperties;
    private final FileTemplate fileTemplate;
    private final OssTransformer ossTransformer;

    public DefaultJsonSchemaFileManager(FileProperties fileProperties, FileTemplate fileTemplate, OssTransformer ossTransformer) {
        this.fileProperties = fileProperties;
        this.fileTemplate = fileTemplate;
        this.ossTransformer = ossTransformer;
    }

    @Override
    public OssTransformer getOssTransformer() {
        return ossTransformer;
    }

    @Override
    public FileTemplate getFileTemplate() {
        return fileTemplate;
    }

    @Override
    public String getDefaultDirectory() {
        return fileProperties.getJsonSchema().getDirectory();
    }

    @Override
    public String getDefaultBucketName() {
        return fileProperties.getJsonSchema().getBucketName();
    }
}
