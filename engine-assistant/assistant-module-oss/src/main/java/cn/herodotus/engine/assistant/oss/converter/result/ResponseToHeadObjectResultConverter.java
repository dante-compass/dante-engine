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
import cn.herodotus.engine.assistant.oss.definition.domain.ObjectLockDomain;
import cn.herodotus.engine.assistant.oss.definition.domain.PutObjectDomain;
import cn.herodotus.engine.assistant.oss.definition.domain.SsekmsDomain;
import cn.herodotus.engine.assistant.oss.entity.result.HeadObjectResult;
import cn.herodotus.engine.assistant.oss.utils.OssUtils;
import cn.hutool.v7.core.date.DateFormatPool;
import cn.hutool.v7.core.date.DateUtil;
import cn.hutool.v7.core.date.TimeUtil;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

/**
 * <p>Description: HeadObjectResponse 转 HeadObjectResult 转换器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/25 18:09
 */
public class ResponseToHeadObjectResultConverter implements ResponseConverter<HeadObjectResponse, HeadObjectResult> {

    @Override
    public HeadObjectResult getInstance(HeadObjectResponse source) {
        return new HeadObjectResult();
    }

    @Override
    public void prepare(HeadObjectResponse source, HeadObjectResult target) {

        target.setAcceptRanges(source.acceptRanges());
        target.setArchiveStatus(source.archiveStatusAsString());
        target.setETag(OssUtils.unwrapETag(source.eTag()));
        target.setExpiration(source.expiration());
        target.setExpiresString(source.expiresString());
        target.setLastModified(DateUtil.toLocalDateTime(source.lastModified()));
        target.setMissingMeta(source.missingMeta());
        target.setPartsCount(source.partsCount());
        target.setReplicationStatus(source.replicationStatusAsString());
        target.setRestore(source.restore());
        target.setDeleteMarker(source.deleteMarker());
        target.setRequestCharged(source.requestChargedAsString());
        target.setVersionId(source.versionId());

        ChecksumDomain checksum = new ChecksumDomain();
        checksum.setChecksumCRC32(source.checksumCRC32());
        checksum.setChecksumCRC32C(source.checksumCRC32C());
        checksum.setChecksumSHA1(source.checksumSHA1());
        checksum.setChecksumSHA256(source.checksumSHA256());
        target.setChecksum(checksum);

        PutObjectDomain domain = new PutObjectDomain();
        domain.setBucketKeyEnabled(source.bucketKeyEnabled());
        domain.setCacheControl(source.cacheControl());

        domain.setContentDisposition(source.contentDisposition());
        domain.setContentEncoding(source.contentEncoding());
        domain.setContentLanguage(source.contentLanguage());
        domain.setContentLength(source.contentLength());
        domain.setContentType(source.contentType());
        domain.setExpires(TimeUtil.parse(source.expiresString(), DateFormatPool.NORM_DATETIME_PATTERN));
        domain.setMetadata(source.metadata());

        domain.setServerSideEncryption(source.serverSideEncryptionAsString());

        domain.setStorageClass(source.storageClassAsString());

        SsekmsDomain ssekms = new SsekmsDomain();
        ssekms.setSsekmsEncryptionContext(ssekms.getSsekmsEncryptionContext());
        ssekms.setSsekmsKeyId(ssekms.getSsekmsKeyId());
        ssekms.setSseCustomerAlgorithm(ssekms.getSseCustomerAlgorithm());
        ssekms.setSseCustomerKey(ssekms.getSseCustomerKey());
        ssekms.setSseCustomerKeyMD5(ssekms.getSseCustomerKeyMD5());
        domain.setSsekms(ssekms);

        ObjectLockDomain objectLock = new ObjectLockDomain();
        objectLock.setObjectLockLegalHoldStatus(objectLock.getObjectLockLegalHoldStatus());
        objectLock.setObjectLockMode(objectLock.getObjectLockMode());
        objectLock.setObjectLockRetainUntilDate(objectLock.getObjectLockRetainUntilDate());
        domain.setObjectLock(objectLock);

        target.setMetadata(domain);

        ResponseConverter.super.prepare(source, target);
    }
}
