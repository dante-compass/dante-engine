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
import org.dromara.dante.assistant.oss.definition.domain.ChecksumDomain;
import org.dromara.dante.assistant.oss.definition.domain.ObjectLockDomain;
import org.dromara.dante.assistant.oss.definition.domain.PutObjectDomain;
import org.dromara.dante.assistant.oss.definition.domain.SsekmsDomain;
import org.dromara.dante.assistant.oss.entity.argument.PutObjectArgument;
import org.dromara.dante.core.utils.TimeUtils;
import org.springframework.core.convert.converter.Converter;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * <p>Description: PutObjectArgument 转 PutObjectRequest 转换器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/25 17:41
 */
public class ArgumentToPutObjectRequestConverter implements Converter<PutObjectArgument, PutObjectRequest> {
    @Override
    public PutObjectRequest convert(PutObjectArgument source) {

        PutObjectRequest.Builder builder = PutObjectRequest.builder();
        builder.bucket(source.getBucketName());
        builder.key(source.getObjectName());
        builder.requestPayer(source.getRequestPayer());
        builder.expectedBucketOwner(source.getExpectedBucketOwner());
        builder.acl(source.getAcl());
        builder.checksumAlgorithm(source.getChecksumAlgorithm());

        builder.grantFullControl(source.getGrantFullControl());
        builder.grantRead(source.getGrantRead());
        builder.grantReadACP(source.getGrantReadACP());
        builder.grantWriteACP(source.getGrantWriteACP());
        builder.websiteRedirectLocation(source.getWebsiteRedirectLocation());
        builder.tagging(source.getTagging());

        ChecksumDomain checksum = source.getChecksum();
        if (ObjectUtils.isNotEmpty(checksum)) {
            builder.checksumCRC32(checksum.getChecksumCRC32());
            builder.checksumCRC32C(checksum.getChecksumCRC32C());
            builder.checksumSHA1(checksum.getChecksumSHA1());
            builder.checksumSHA256(checksum.getChecksumSHA256());
        }

        PutObjectDomain domain = source.getMetadata();

        builder.cacheControl(domain.getCacheControl());
        builder.contentDisposition(domain.getContentDisposition());
        builder.contentEncoding(domain.getContentEncoding());
        builder.contentLanguage(domain.getContentLanguage());
        builder.contentLength(domain.getContentLength());
        builder.contentType(domain.getContentType());
        builder.expires(TimeUtils.toInstant(domain.getExpires()));
        builder.metadata(domain.getMetadata());
        builder.serverSideEncryption(domain.getServerSideEncryption());
        builder.storageClass(domain.getStorageClass());
        builder.bucketKeyEnabled(domain.getBucketKeyEnabled());

        SsekmsDomain ssekms = domain.getSsekms();
        if (ObjectUtils.isNotEmpty(ssekms)) {
            builder.ssekmsEncryptionContext(ssekms.getSsekmsEncryptionContext());
            builder.ssekmsKeyId(ssekms.getSsekmsKeyId());
            builder.sseCustomerAlgorithm(ssekms.getSseCustomerAlgorithm());
            builder.sseCustomerKey(ssekms.getSseCustomerKey());
            builder.sseCustomerKeyMD5(ssekms.getSseCustomerKeyMD5());
        }

        ObjectLockDomain objectLock = domain.getObjectLock();
        if (ObjectUtils.isNotEmpty(objectLock)) {
            builder.objectLockLegalHoldStatus(objectLock.getObjectLockLegalHoldStatus());
            builder.objectLockMode(objectLock.getObjectLockMode());
            builder.objectLockRetainUntilDate(TimeUtils.toInstant(objectLock.getObjectLockRetainUntilDate()));
        }

        return builder.build();
    }
}
