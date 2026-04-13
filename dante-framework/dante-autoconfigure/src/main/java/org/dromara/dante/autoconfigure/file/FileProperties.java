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

import com.google.common.base.MoreObjects;
import org.dromara.dante.core.constant.BaseConstants;
import org.dromara.dante.core.constant.SystemConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>Description: 服务本地文件操作配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/10/12 12:21
 */
@ConfigurationProperties(prefix = BaseConstants.PROPERTY_PREFIX_FILE)
public class FileProperties {
    /**
     * 本地文件默认存储位置。默认位置：user.home
     */
    private String destination;

    private JsonSchema jsonSchema = new JsonSchema();

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public JsonSchema getJsonSchema() {
        return jsonSchema;
    }

    public void setJsonSchema(JsonSchema jsonSchema) {
        this.jsonSchema = jsonSchema;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("destination", destination)
                .add("jsonSchema", jsonSchema)
                .toString();
    }

    public static class JsonSchema {
        /**
         * JsonSchema 文件存储使用存储桶。默认为：herodotus-cloud
         */
        private String bucketName = SystemConstants.DEFAULT_BUCKET_NAME;

        /**
         * JsonSchema 本地文件存储默认文件夹。默认为：jsonschema
         */
        private String directory = SystemConstants.DEFAULT_JSON_SCHEMA_DIRECTORY;

        public String getBucketName() {
            return bucketName;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }

        public String getDirectory() {
            return directory;
        }

        public void setDirectory(String directory) {
            this.directory = directory;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("bucketName", bucketName)
                    .add("directory", directory)
                    .toString();
        }
    }
}
