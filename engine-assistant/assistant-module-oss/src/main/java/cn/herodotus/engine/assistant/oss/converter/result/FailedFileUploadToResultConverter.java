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

import cn.herodotus.engine.assistant.oss.definition.domain.ChecksumDomain;
import cn.herodotus.engine.assistant.oss.definition.domain.ObjectLockDomain;
import cn.herodotus.engine.assistant.oss.definition.domain.PutObjectDomain;
import cn.herodotus.engine.assistant.oss.definition.domain.SsekmsDomain;
import cn.herodotus.engine.assistant.oss.entity.result.FailedFileUploadResult;
import cn.herodotus.engine.core.foundation.founction.ListConverter;
import cn.hutool.v7.core.date.DateUtil;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.transfer.s3.model.FailedFileUpload;

import java.nio.file.Path;

/**
 * <p>Description: {@link FailedFileUpload} 转 {@link FailedFileUploadResult} 转换器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/29 21:43
 */
public class FailedFileUploadToResultConverter implements ListConverter<FailedFileUpload, FailedFileUploadResult> {
    @Override
    public FailedFileUploadResult from(FailedFileUpload source) {

        FailedFileUploadResult target = new FailedFileUploadResult();

        PutObjectRequest request = source.request().putObjectRequest();
        Path path = source.request().source();

        target.setException(source.exception());
        target.setFile(path.toString());
        target.setAcl(request.aclAsString());

        ChecksumDomain checksum = new ChecksumDomain();
        checksum.setChecksumCRC32(request.checksumCRC32());
        checksum.setChecksumCRC32C(request.checksumCRC32C());
        checksum.setChecksumSHA1(request.checksumSHA1());
        checksum.setChecksumSHA256(request.checksumSHA256());
        target.setChecksum(checksum);

        target.setChecksumAlgorithm(request.checksumAlgorithmAsString());
        target.setGrantFullControl(request.grantFullControl());
        target.setGrantRead(request.grantRead());
        target.setGrantReadACP(request.grantReadACP());
        target.setGrantWriteACP(request.grantWriteACP());

        PutObjectDomain putObject = new PutObjectDomain();
        putObject.setBucketKeyEnabled(request.bucketKeyEnabled());
        putObject.setCacheControl(request.cacheControl());
        putObject.setContentDisposition(request.contentDisposition());
        putObject.setContentEncoding(request.contentEncoding());
        putObject.setContentLanguage(request.contentLanguage());
        putObject.setContentLength(request.contentLength());
        putObject.setContentType(request.contentType());
        putObject.setExpires(DateUtil.toLocalDateTime(request.expires()));
        putObject.setMetadata(request.metadata());

        ObjectLockDomain objectLock = new ObjectLockDomain();
        objectLock.setObjectLockLegalHoldStatus(request.objectLockLegalHoldStatusAsString());
        objectLock.setObjectLockMode(request.objectLockModeAsString());
        objectLock.setObjectLockRetainUntilDate(DateUtil.toLocalDateTime(request.objectLockRetainUntilDate()));
        putObject.setObjectLock(objectLock);

        putObject.setServerSideEncryption(request.serverSideEncryptionAsString());

        SsekmsDomain ssekms = new SsekmsDomain();
        ssekms.setSsekmsEncryptionContext(request.ssekmsEncryptionContext());
        ssekms.setSsekmsKeyId(request.ssekmsKeyId());
        ssekms.setSseCustomerAlgorithm(request.sseCustomerAlgorithm());
        ssekms.setSseCustomerKey(request.sseCustomerKey());
        ssekms.setSseCustomerKeyMD5(request.sseCustomerKeyMD5());
        putObject.setSsekms(ssekms);

        putObject.setStorageClass(request.storageClassAsString());

        target.setMetadata(putObject);
        target.setTagging(request.tagging());
        target.setWebsiteRedirectLocation(request.websiteRedirectLocation());
        target.setObjectName(request.key());
        target.setRequestPayer(request.requestPayerAsString());
        target.setExpectedBucketOwner(request.expectedBucketOwner());
        target.setBucketName(request.bucket());

        return target;
    }
}
