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

package cn.herodotus.dante.assistant.oss.entity.result;

import cn.herodotus.dante.assistant.oss.definition.result.AbstractObjectVersionIdResult;
import cn.herodotus.dante.assistant.oss.entity.domain.ChecksumDomain;
import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>Description: 完成分片上传响应结果对象实体 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/24 0:00
 */
@Schema(name = "完成分片上传响应结果对象实体", title = "完成分片上传响应结果对象实体")
public class CompleteMultipartUploadResult extends AbstractObjectVersionIdResult {

    private String bucketName;

    private String objectName;

    private String location;

    private String expiration;

    private ChecksumDomain checksum = new ChecksumDomain();

    private String serverSideEncryption;

    public ChecksumDomain getChecksum() {
        return checksum;
    }

    public void setChecksum(ChecksumDomain checksum) {
        this.checksum = checksum;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getServerSideEncryption() {
        return serverSideEncryption;
    }

    public void setServerSideEncryption(String serverSideEncryption) {
        this.serverSideEncryption = serverSideEncryption;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("bucketName", bucketName)
                .add("objectName", objectName)
                .add("location", location)
                .add("expiration", expiration)
                .add("checksum", checksum)
                .add("serverSideEncryption", serverSideEncryption)
                .addValue(super.toString())
                .toString();
    }
}
