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

package org.dromara.dante.assistant.oss.entity.domain;

import com.google.common.base.MoreObjects;
import org.dromara.dante.assistant.oss.definition.domain.OssDomain;

import java.time.LocalDateTime;

/**
 * <p>Description: 存储桶通用实体对象 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/21 22:55
 */
public class BucketDomain implements OssDomain {

    private String bucketName;

    private LocalDateTime creationDate;
    /**
     * BucketRegion表示存储桶所在的亚马逊网络服务（AWS）区域。如果请求中至少包含一个有效参数，则该参数将包含在响应中。
     */
    private String bucketRegion;
    /**
     * S3存储桶的亚马逊资源名称（ARN）。ARN在所有亚马逊网络服务中唯一标识亚马逊网络服务资源。
     * 此参数仅支持用于S3目录存储桶
     */
    private String bucketArn;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getBucketRegion() {
        return bucketRegion;
    }

    public void setBucketRegion(String bucketRegion) {
        this.bucketRegion = bucketRegion;
    }

    public String getBucketArn() {
        return bucketArn;
    }

    public void setBucketArn(String bucketArn) {
        this.bucketArn = bucketArn;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("bucketName", bucketName)
                .add("creationDate", creationDate)
                .add("bucketRegion", bucketRegion)
                .add("bucketArn", bucketArn)
                .toString();
    }
}
