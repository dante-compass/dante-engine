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

import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;
import org.dromara.dante.assistant.oss.definition.result.AbstractUploadResult;
import org.dromara.dante.assistant.oss.entity.domain.SsekmsDomain;

import java.time.LocalDateTime;

/**
 * <p>Description: 创建分片上传响应结果对象实体 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/23 23:59
 */
@Schema(name = "创建分片上传响应结果对象实体", title = "创建分片上传响应结果对象实体")
public class CreateMultipartUploadResult extends AbstractUploadResult {

    private LocalDateTime abortDate;

    private String abortRuleId;

    private String uploadId;

    private String serverSideEncryption;

    private SsekmsDomain ssekms = new SsekmsDomain();

    private String checksumAlgorithm;

    public LocalDateTime getAbortDate() {
        return abortDate;
    }

    public void setAbortDate(LocalDateTime abortDate) {
        this.abortDate = abortDate;
    }

    public String getAbortRuleId() {
        return abortRuleId;
    }

    public void setAbortRuleId(String abortRuleId) {
        this.abortRuleId = abortRuleId;
    }

    public String getChecksumAlgorithm() {
        return checksumAlgorithm;
    }

    public void setChecksumAlgorithm(String checksumAlgorithm) {
        this.checksumAlgorithm = checksumAlgorithm;
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

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("abortDate", abortDate)
                .add("abortRuleId", abortRuleId)
                .add("uploadId", uploadId)
                .add("serverSideEncryption", serverSideEncryption)
                .add("ssekms", ssekms)
                .add("checksumAlgorithm", checksumAlgorithm)
                .toString();
    }
}
