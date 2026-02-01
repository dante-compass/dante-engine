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
import org.dromara.dante.assistant.oss.definition.domain.SsekmsDomain;
import org.dromara.dante.assistant.oss.entity.argument.WriteGetObjectResponseArgument;
import org.dromara.dante.core.utils.TimeUtils;
import org.springframework.core.convert.converter.Converter;
import software.amazon.awssdk.services.s3.model.WriteGetObjectResponseRequest;

/**
 * <p>Description: {@link WriteGetObjectResponseArgument} 转 {@link WriteGetObjectResponseRequest} 转换器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/25 17:58
 */
public class ArgumentToWriteGetObjectResponseRequestConverter implements Converter<WriteGetObjectResponseArgument, WriteGetObjectResponseRequest> {
    @Override
    public WriteGetObjectResponseRequest convert(WriteGetObjectResponseArgument source) {

        WriteGetObjectResponseRequest.Builder builder = WriteGetObjectResponseRequest.builder();


        builder.requestRoute(source.getRequestRoute());
        builder.requestToken(source.getRequestToken());
        builder.statusCode(source.getStatusCode());
        builder.errorCode(source.getErrorCode());
        builder.errorMessage(source.getErrorMessage());
        builder.acceptRanges(source.getAcceptRanges());
        builder.deleteMarker(source.getDeleteMarker());
        builder.eTag(source.getETag());
        builder.expiration(source.getExpiration());
        builder.lastModified(TimeUtils.toInstant(source.getLastModified()));
        builder.missingMeta(source.getMissingMeta());
        builder.partsCount(source.getPartsCount());
        builder.replicationStatus(source.getReplicationStatus());
        builder.requestCharged(source.getRequestCharged());
        builder.restore(source.getRestore());
        builder.versionId(source.getVersionId());
        builder.tagCount(source.getTagCount());

        builder.cacheControl(source.getCacheControl());
        builder.contentDisposition(source.getContentDisposition());
        builder.contentEncoding(source.getContentEncoding());
        builder.contentLanguage(source.getContentLanguage());
        builder.contentLength(source.getContentLength());
        builder.contentType(source.getContentType());
        builder.expires(TimeUtils.toInstant(source.getExpires()));
        builder.metadata(source.getMetadata());
        builder.serverSideEncryption(source.getServerSideEncryption());
        builder.storageClass(source.getStorageClass());
        builder.bucketKeyEnabled(source.getBucketKeyEnabled());

        ChecksumDomain checksum = source.getChecksum();
        if (ObjectUtils.isNotEmpty(checksum)) {
            builder.checksumCRC32(checksum.getChecksumCRC32());
            builder.checksumCRC32C(checksum.getChecksumCRC32C());
            builder.checksumSHA1(checksum.getChecksumSHA1());
            builder.checksumSHA256(checksum.getChecksumSHA256());
        }

        SsekmsDomain ssekms = source.getSsekms();
        if (ObjectUtils.isNotEmpty(ssekms)) {
            builder.ssekmsKeyId(ssekms.getSsekmsKeyId());
            builder.sseCustomerAlgorithm(ssekms.getSseCustomerAlgorithm());
            builder.sseCustomerKeyMD5(ssekms.getSseCustomerKeyMD5());
        }

        ObjectLockDomain objectLock = source.getObjectLock();
        if (ObjectUtils.isNotEmpty(objectLock)) {
            builder.objectLockLegalHoldStatus(objectLock.getObjectLockLegalHoldStatus());
            builder.objectLockMode(objectLock.getObjectLockMode());
            builder.objectLockRetainUntilDate(TimeUtils.toInstant(objectLock.getObjectLockRetainUntilDate()));
        }

        return builder.build();
    }
}
