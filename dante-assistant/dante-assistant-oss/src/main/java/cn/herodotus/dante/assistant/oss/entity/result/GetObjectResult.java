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

import cn.herodotus.dante.assistant.oss.definition.domain.ChecksumDomain;
import cn.herodotus.dante.assistant.oss.definition.domain.PutObjectDomain;
import cn.herodotus.dante.assistant.oss.definition.result.AbstractResult;
import com.google.common.base.MoreObjects;

import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * <p>Description: 获取对象响应结果对象实体 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/26 17:42
 */
public class GetObjectResult extends AbstractResult {

    private Boolean deleteMarker;

    private String acceptRanges;

    private String expiration;

    private String restore;

    private LocalDateTime lastModified;

    private String eTag;

    private Integer missingMeta;

    private String versionId;

    private String websiteRedirectLocation;

    private String requestCharged;

    private String replicationStatus;

    private Integer partsCount;

    private Integer tagCount;

    private String expiresString;

    private InputStream inputStream;

    private ChecksumDomain checksum = new ChecksumDomain();

    private PutObjectDomain metadata = new PutObjectDomain();

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

    public String getExpiresString() {
        return expiresString;
    }

    public void setExpiresString(String expiresString) {
        this.expiresString = expiresString;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public PutObjectDomain getMetadata() {
        return metadata;
    }

    public void setMetadata(PutObjectDomain metadata) {
        this.metadata = metadata;
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

    public String getRestore() {
        return restore;
    }

    public void setRestore(String restore) {
        this.restore = restore;
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

    public String getWebsiteRedirectLocation() {
        return websiteRedirectLocation;
    }

    public void setWebsiteRedirectLocation(String websiteRedirectLocation) {
        this.websiteRedirectLocation = websiteRedirectLocation;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("acceptRanges", acceptRanges)
                .add("deleteMarker", deleteMarker)
                .add("expiration", expiration)
                .add("restore", restore)
                .add("lastModified", lastModified)
                .add("eTag", eTag)
                .add("missingMeta", missingMeta)
                .add("versionId", versionId)
                .add("websiteRedirectLocation", websiteRedirectLocation)
                .add("requestCharged", requestCharged)
                .add("replicationStatus", replicationStatus)
                .add("partsCount", partsCount)
                .add("tagCount", tagCount)
                .add("expiresString", expiresString)
                .add("checksum", checksum)
                .add("metadata", metadata)
                .toString();
    }
}
