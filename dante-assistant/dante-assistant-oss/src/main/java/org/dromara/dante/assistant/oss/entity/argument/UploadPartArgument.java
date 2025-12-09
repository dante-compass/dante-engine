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
import org.dromara.dante.assistant.oss.definition.domain.SseCustomerDomain;

/**
 * <p>Description: 上传分片请求参数实体 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/23 11:21
 */
@Schema(name = "上传分片请求参数实体", title = "上传分片请求参数实体")
public class UploadPartArgument extends AbortMultipartUploadArgument {

    private Long contentLength;

    private String contentMD5;

    private String checksumAlgorithm;

    private ChecksumDomain checksum = new ChecksumDomain();

    private Integer partNumber;

    private SseCustomerDomain sse = new SseCustomerDomain();

    private String sdkPartType;

    public ChecksumDomain getChecksum() {
        return checksum;
    }

    public void setChecksum(ChecksumDomain checksum) {
        this.checksum = checksum;
    }

    public String getChecksumAlgorithm() {
        return checksumAlgorithm;
    }

    public void setChecksumAlgorithm(String checksumAlgorithm) {
        this.checksumAlgorithm = checksumAlgorithm;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public String getContentMD5() {
        return contentMD5;
    }

    public void setContentMD5(String contentMD5) {
        this.contentMD5 = contentMD5;
    }

    public Integer getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(Integer partNumber) {
        this.partNumber = partNumber;
    }

    public String getSdkPartType() {
        return sdkPartType;
    }

    public void setSdkPartType(String sdkPartType) {
        this.sdkPartType = sdkPartType;
    }

    public SseCustomerDomain getSse() {
        return sse;
    }

    public void setSse(SseCustomerDomain sse) {
        this.sse = sse;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("checksum", checksum)
                .add("contentLength", contentLength)
                .add("contentMD5", contentMD5)
                .add("checksumAlgorithm", checksumAlgorithm)
                .add("partNumber", partNumber)
                .add("sse", sse)
                .add("sdkPartType", sdkPartType)
                .toString();
    }
}
