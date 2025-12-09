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

package org.dromara.dante.assistant.oss.definition.domain;

import com.google.common.base.MoreObjects;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.WriteGetObjectResponseRequest;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>Description: PutObject 共性参数 </p>
 * <p>
 * {@link WriteGetObjectResponseRequest} 和 {@link PutObjectRequest} 共性参数
 *
 * @author : gengwei.zheng
 * @date : 2024/7/22 23:56
 */
public class PutObjectDomain implements OssDomain {

    private String cacheControl;

    private String contentDisposition;

    private String contentEncoding;

    private String contentLanguage;

    private Long contentLength;

    private String contentRange;

    private String contentType;

    private LocalDateTime expires;

    private Map<String, String> metadata;

    private String serverSideEncryption;

    private String storageClass;

    private SsekmsDomain ssekms = new SsekmsDomain();

    private Boolean bucketKeyEnabled;

    private ObjectLockDomain objectLock = new ObjectLockDomain();

    public Boolean getBucketKeyEnabled() {
        return bucketKeyEnabled;
    }

    public void setBucketKeyEnabled(Boolean bucketKeyEnabled) {
        this.bucketKeyEnabled = bucketKeyEnabled;
    }

    public String getCacheControl() {
        return cacheControl;
    }

    public void setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public String getContentLanguage() {
        return contentLanguage;
    }

    public void setContentLanguage(String contentLanguage) {
        this.contentLanguage = contentLanguage;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public String getContentRange() {
        return contentRange;
    }

    public void setContentRange(String contentRange) {
        this.contentRange = contentRange;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public void setExpires(LocalDateTime expires) {
        this.expires = expires;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public ObjectLockDomain getObjectLock() {
        return objectLock;
    }

    public void setObjectLock(ObjectLockDomain objectLock) {
        this.objectLock = objectLock;
    }

    public String getServerSideEncryption() {
        return serverSideEncryption;
    }

    public void setServerSideEncryption(String serverSideEncryption) {
        this.serverSideEncryption = serverSideEncryption;
    }

    public SsekmsDomain getSsekms() {
        return ssekms;
    }

    public void setSsekms(SsekmsDomain ssekms) {
        this.ssekms = ssekms;
    }

    public String getStorageClass() {
        return storageClass;
    }

    public void setStorageClass(String storageClass) {
        this.storageClass = storageClass;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("bucketKeyEnabled", bucketKeyEnabled)
                .add("cacheControl", cacheControl)
                .add("contentDisposition", contentDisposition)
                .add("contentEncoding", contentEncoding)
                .add("contentLanguage", contentLanguage)
                .add("contentLength", contentLength)
                .add("contentRange", contentRange)
                .add("contentType", contentType)
                .add("expires", expires)
                .add("metadata", metadata)
                .add("serverSideEncryption", serverSideEncryption)
                .add("storageClass", storageClass)
                .add("ssekms", ssekms)
                .add("objectLock", objectLock)
                .toString();
    }
}
