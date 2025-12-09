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

package org.dromara.dante.assistant.oss.converter.argument;

import org.apache.commons.lang3.ObjectUtils;
import org.dromara.dante.assistant.oss.entity.argument.UploadPartArgument;
import org.springframework.core.convert.converter.Converter;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;

/**
 * <p>Description: {@link UploadPartArgument} 转 {@link UploadPartRequest} 转换器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/26 17:29
 */
public class ArgumentToUploadPartRequestConverter implements Converter<UploadPartArgument, UploadPartRequest> {

    @Override
    public UploadPartRequest convert(UploadPartArgument source) {

        UploadPartRequest.Builder builder = UploadPartRequest.builder();

        builder.bucket(source.getBucketName());
        builder.key(source.getObjectName());
        builder.contentLength(source.getContentLength());
        builder.contentMD5(source.getContentMD5());
        builder.checksumAlgorithm(source.getChecksumAlgorithm());
        builder.partNumber(source.getPartNumber());
        builder.uploadId(source.getUploadId());
        builder.requestPayer(source.getRequestPayer());
        builder.expectedBucketOwner(source.getExpectedBucketOwner());
        builder.sdkPartType(source.getSdkPartType());

        if (ObjectUtils.isNotEmpty(source.getSse())) {
            builder.sseCustomerAlgorithm(source.getSse().getSseCustomerAlgorithm());
            builder.sseCustomerKey(source.getSse().getSseCustomerKey());
            builder.sseCustomerKeyMD5(source.getSse().getSseCustomerKeyMD5());
        }

        if (ObjectUtils.isNotEmpty(source.getChecksum())) {
            builder.checksumCRC32(source.getChecksum().getChecksumCRC32());
            builder.checksumCRC32C(source.getChecksum().getChecksumCRC32C());
            builder.checksumSHA1(source.getChecksum().getChecksumSHA1());
            builder.checksumSHA256(source.getChecksum().getChecksumSHA256());
        }

        return builder.build();
    }
}
