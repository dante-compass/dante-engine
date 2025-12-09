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

import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;
import org.dromara.dante.assistant.oss.definition.domain.ChecksumDomain;
import org.dromara.dante.assistant.oss.definition.domain.PutObjectDomain;

import java.time.LocalDateTime;

/**
 * <p>Description: writeGetObjectResponse 操作请求参数实体 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/22 23:57
 */
@Schema(name = "writeGetObjectResponse 操作请求参数实体", title = "writeGetObjectResponse 操作请求参数实体")
public class WriteGetObjectResponseArgument extends PutObjectDomain {

    private String requestRoute;

    private String requestToken;

    private Integer statusCode;

    private String errorCode;

    private String errorMessage;

    private String acceptRanges;

    private Boolean deleteMarker;

    private String eTag;

    private String expiration;

    private LocalDateTime lastModified;

    private Integer missingMeta;

    private Integer partsCount;

    private String replicationStatus;

    private String requestCharged;

    private String restore;

    private String versionId;

    private Integer tagCount;

    private ChecksumDomain checksum = new ChecksumDomain();

    public String getAcceptRanges() {
        return acceptRanges;
    }

    public void setAcceptRanges(String acceptRanges) {
        this.acceptRanges = acceptRanges;
    }

    public ChecksumDomain getChecksum() {
        return checksum;
    }

    public void setChecksum(ChecksumDomain checksum) {
        this.checksum = checksum;
    }

    public Boolean getDeleteMarker() {
        return deleteMarker;
    }

    public void setDeleteMarker(Boolean deleteMarker) {
        this.deleteMarker = deleteMarker;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getETag() {
        return eTag;
    }

    public void setETag(String eTag) {
        this.eTag = eTag;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public Integer getMissingMeta() {
        return missingMeta;
    }

    public void setMissingMeta(Integer missingMeta) {
        this.missingMeta = missingMeta;
    }

    public Integer getPartsCount() {
        return partsCount;
    }

    public void setPartsCount(Integer partsCount) {
        this.partsCount = partsCount;
    }

    public String getReplicationStatus() {
        return replicationStatus;
    }

    public void setReplicationStatus(String replicationStatus) {
        this.replicationStatus = replicationStatus;
    }

    public String getRequestCharged() {
        return requestCharged;
    }

    public void setRequestCharged(String requestCharged) {
        this.requestCharged = requestCharged;
    }

    public String getRequestRoute() {
        return requestRoute;
    }

    public void setRequestRoute(String requestRoute) {
        this.requestRoute = requestRoute;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    public String getRestore() {
        return restore;
    }

    public void setRestore(String restore) {
        this.restore = restore;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getTagCount() {
        return tagCount;
    }

    public void setTagCount(Integer tagCount) {
        this.tagCount = tagCount;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("acceptRanges", acceptRanges)
                .add("requestRoute", requestRoute)
                .add("requestToken", requestToken)
                .add("statusCode", statusCode)
                .add("errorCode", errorCode)
                .add("errorMessage", errorMessage)
                .add("deleteMarker", deleteMarker)
                .add("eTag", eTag)
                .add("expiration", expiration)
                .add("lastModified", lastModified)
                .add("missingMeta", missingMeta)
                .add("partsCount", partsCount)
                .add("replicationStatus", replicationStatus)
                .add("requestCharged", requestCharged)
                .add("restore", restore)
                .add("versionId", versionId)
                .add("tagCount", tagCount)
                .add("checksum", checksum)
                .toString();
    }
}
