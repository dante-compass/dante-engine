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

package org.dromara.dante.assistant.oss.entity.argument;

import org.dromara.dante.assistant.oss.definition.argument.AbstractArgument;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>Description: 上传目录请求参数实体 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/29 17:25
 */
@Schema(name = "上传目录请求参数实体", title = "上传目录请求参数实体")
public class UploadDirectoryArgument extends AbstractArgument {

    private String source;
    private String bucketName;
    private String prefix;
    private String delimiter;
    private Boolean followSymbolicLinks;
    private Integer maxDepth;

    private UploadFileArgument argument;

    public UploadFileArgument getArgument() {
        return argument;
    }

    public void setArgument(UploadFileArgument argument) {
        this.argument = argument;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public Boolean getFollowSymbolicLinks() {
        return followSymbolicLinks;
    }

    public void setFollowSymbolicLinks(Boolean followSymbolicLinks) {
        this.followSymbolicLinks = followSymbolicLinks;
    }

    public Integer getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(Integer maxDepth) {
        this.maxDepth = maxDepth;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
