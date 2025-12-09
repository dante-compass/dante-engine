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

package org.dromara.dante.assistant.oss.definition.argument;

import org.dromara.dante.assistant.oss.definition.domain.SseCustomerDomain;
import com.google.common.base.MoreObjects;

import java.time.LocalDateTime;

/**
 * <p>Description: 获取对象通用参数 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/22 23:47
 */
public abstract class AbstractGetObjectArgument extends AbstractObjectVersionArgument {

    private String ifMatch;

    private LocalDateTime ifModifiedSince;

    private String ifNoneMatch;

    private LocalDateTime ifUnmodifiedSince;

    private String range;

    private String responseCacheControl;

    private String responseContentDisposition;

    private String responseContentEncoding;

    private String responseContentLanguage;

    private String responseContentType;

    private LocalDateTime responseExpires;

    private SseCustomerDomain sseCustomer = new SseCustomerDomain();

    private Integer partNumber;

    private String checksumMode;

    public String getChecksumMode() {
        return checksumMode;
    }

    public void setChecksumMode(String checksumMode) {
        this.checksumMode = checksumMode;
    }

    public String getIfMatch() {
        return ifMatch;
    }

    public void setIfMatch(String ifMatch) {
        this.ifMatch = ifMatch;
    }

    public LocalDateTime getIfModifiedSince() {
        return ifModifiedSince;
    }

    public void setIfModifiedSince(LocalDateTime ifModifiedSince) {
        this.ifModifiedSince = ifModifiedSince;
    }

    public String getIfNoneMatch() {
        return ifNoneMatch;
    }

    public void setIfNoneMatch(String ifNoneMatch) {
        this.ifNoneMatch = ifNoneMatch;
    }

    public LocalDateTime getIfUnmodifiedSince() {
        return ifUnmodifiedSince;
    }

    public void setIfUnmodifiedSince(LocalDateTime ifUnmodifiedSince) {
        this.ifUnmodifiedSince = ifUnmodifiedSince;
    }

    public Integer getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(Integer partNumber) {
        this.partNumber = partNumber;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getResponseCacheControl() {
        return responseCacheControl;
    }

    public void setResponseCacheControl(String responseCacheControl) {
        this.responseCacheControl = responseCacheControl;
    }

    public String getResponseContentDisposition() {
        return responseContentDisposition;
    }

    public void setResponseContentDisposition(String responseContentDisposition) {
        this.responseContentDisposition = responseContentDisposition;
    }

    public String getResponseContentEncoding() {
        return responseContentEncoding;
    }

    public void setResponseContentEncoding(String responseContentEncoding) {
        this.responseContentEncoding = responseContentEncoding;
    }

    public String getResponseContentLanguage() {
        return responseContentLanguage;
    }

    public void setResponseContentLanguage(String responseContentLanguage) {
        this.responseContentLanguage = responseContentLanguage;
    }

    public String getResponseContentType() {
        return responseContentType;
    }

    public void setResponseContentType(String responseContentType) {
        this.responseContentType = responseContentType;
    }

    public LocalDateTime getResponseExpires() {
        return responseExpires;
    }

    public void setResponseExpires(LocalDateTime responseExpires) {
        this.responseExpires = responseExpires;
    }

    public SseCustomerDomain getSseCustomer() {
        return sseCustomer;
    }

    public void setSseCustomer(SseCustomerDomain sseCustomer) {
        this.sseCustomer = sseCustomer;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("checksumMode", checksumMode)
                .add("ifMatch", ifMatch)
                .add("ifModifiedSince", ifModifiedSince)
                .add("ifNoneMatch", ifNoneMatch)
                .add("ifUnmodifiedSince", ifUnmodifiedSince)
                .add("range", range)
                .add("responseCacheControl", responseCacheControl)
                .add("responseContentDisposition", responseContentDisposition)
                .add("responseContentEncoding", responseContentEncoding)
                .add("responseContentLanguage", responseContentLanguage)
                .add("responseContentType", responseContentType)
                .add("responseExpires", responseExpires)
                .add("sseCustomer", sseCustomer)
                .add("partNumber", partNumber)
                .toString();
    }
}
