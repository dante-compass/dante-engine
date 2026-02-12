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

package org.dromara.dante.assistant.oss.entity.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;
import org.dromara.dante.assistant.oss.definition.result.AbstractResult;
import org.dromara.dante.assistant.oss.enums.ObjectRetentionMode;

import java.time.LocalDateTime;

/**
 * <p>Description: 对象属性信息响应结果实体 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/23 23:46
 */
@Schema(name = "对象属性信息响应结果实体", title = "对象属性信息响应结果实体")
public class GetObjectAttributesResult extends AbstractResult {

    @Schema(name = "存储桶名称")
    private String bucketName;

    @Schema(name = "文件名")
    private String objectName;

    @Schema(name = "文件锁定是否开启")
    private Boolean lockEnabled;

    @Schema(name = "文件留存是否开启")
    private Boolean lockLegalHold;

    @Schema(name = "保留至什么日期")
    private LocalDateTime retainUntilDate;

    @Schema(name = "保留至什么日期")
    private ObjectRetentionMode retentionMode;

    @Schema(name = "是否为删除标记", description = "对象位于已启用版本控制的存储桶不能物理删除，只能逻辑删除，该标记标记是否逻辑删除")
    private Boolean deleteMarker;

    @Schema(name = "最新修改时间")
    private LocalDateTime lastModified;

    @Schema(name = "版本 ID")
    private String versionId;

    @Schema(name = "ETag")
    @JsonProperty(value = "eTag")
    private String tag;

    @Schema(name = "文件大小")
    private Long size;

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

    public Boolean getLockEnabled() {
        return lockEnabled;
    }

    public void setLockEnabled(Boolean lockEnabled) {
        this.lockEnabled = lockEnabled;
    }

    public Boolean getLockLegalHold() {
        return lockLegalHold;
    }

    public void setLockLegalHold(Boolean lockLegalHold) {
        this.lockLegalHold = lockLegalHold;
    }

    public LocalDateTime getRetainUntilDate() {
        return retainUntilDate;
    }

    public void setRetainUntilDate(LocalDateTime retainUntilDate) {
        this.retainUntilDate = retainUntilDate;
    }

    public ObjectRetentionMode getRetentionMode() {
        return retentionMode;
    }

    public void setRetentionMode(ObjectRetentionMode retentionMode) {
        this.retentionMode = retentionMode;
    }

    public Boolean getDeleteMarker() {
        return deleteMarker;
    }

    public void setDeleteMarker(Boolean deleteMarker) {
        this.deleteMarker = deleteMarker;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("bucketName", bucketName)
                .add("objectName", objectName)
                .add("lockEnabled", lockEnabled)
                .add("lockLegalHold", lockLegalHold)
                .add("retainUntilDate", retainUntilDate)
                .add("retentionMode", retentionMode)
                .add("deleteMarker", deleteMarker)
                .add("lastModified", lastModified)
                .add("versionId", versionId)
                .add("tag", tag)
                .add("size", size)
                .addValue(super.toString())
                .toString();
    }
}
