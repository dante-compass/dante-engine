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

package cn.herodotus.engine.assistant.oss.converter.result;

import cn.herodotus.engine.assistant.oss.definition.converter.ResponseConverter;
import cn.herodotus.engine.assistant.oss.definition.domain.ChecksumDomain;
import cn.herodotus.engine.assistant.oss.definition.domain.SsekmsDomain;
import cn.herodotus.engine.assistant.oss.entity.result.PutObjectResult;
import cn.herodotus.engine.assistant.oss.utils.OssUtils;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

/**
 * <p>Description: PutObjectResponse 转 PutObjectResult 转换器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/25 23:45
 */
public class ResponseToPutObjectResultConverter implements ResponseConverter<PutObjectResponse, PutObjectResult> {

    @Override
    public void prepare(PutObjectResponse source, PutObjectResult target) {

        target.setExpiration(source.expiration());
        target.setETag(OssUtils.unwrapETag(source.eTag()));
        target.setVersionId(source.versionId());

        ChecksumDomain checksum = new ChecksumDomain();
        checksum.setChecksumCRC32(source.checksumCRC32());
        checksum.setChecksumCRC32C(source.checksumCRC32C());
        checksum.setChecksumSHA1(source.checksumSHA1());
        checksum.setChecksumSHA256(source.checksumSHA256());
        target.setChecksum(checksum);

        SsekmsDomain ssekms = new SsekmsDomain();
        ssekms.setSsekmsEncryptionContext(source.ssekmsEncryptionContext());
        ssekms.setSsekmsKeyId(source.ssekmsKeyId());
        ssekms.setSseCustomerAlgorithm(source.sseCustomerAlgorithm());
        ssekms.setSseCustomerKeyMD5(source.sseCustomerKeyMD5());
        target.setSsekms(ssekms);

        ResponseConverter.super.prepare(source, target);
    }

    @Override
    public PutObjectResult getInstance(PutObjectResponse source) {
        return new PutObjectResult();
    }
}
