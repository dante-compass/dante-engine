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

package org.dromara.dante.assistant.oss.service.manager;

import org.dromara.dante.assistant.oss.converter.OssProxyAddressConverter;
import org.dromara.dante.assistant.oss.entity.business.CreateMultipartUploadBusiness;
import org.dromara.dante.assistant.oss.properties.OssProperties;
import org.dromara.dante.assistant.oss.service.base.S3AsyncClientMultipartUploadService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.ListPartsRequest;
import software.amazon.awssdk.services.s3.model.ListPartsResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * <p>Description: 客户端大文件分片上传逻辑 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/30 0:24
 */
@Service
public class PresignedMultipartUploadService {

    private final S3AsyncClientMultipartUploadService s3AsyncClientMultipartUploadService;
    private final PresignedUrlService presignedUrlService;
    private final Converter<String, String> toProxy;

    public PresignedMultipartUploadService(OssProperties ossProperties, S3AsyncClientMultipartUploadService s3AsyncClientMultipartUploadService, PresignedUrlService presignedUrlService) {
        this.s3AsyncClientMultipartUploadService = s3AsyncClientMultipartUploadService;
        this.presignedUrlService = presignedUrlService;
        this.toProxy = new OssProxyAddressConverter(ossProperties);
    }

    /**
     * 第一步：创建分片上传请求, 返回 UploadId
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @return 大文件分片上传 UploadId
     */
    public CompletableFuture<CreateMultipartUploadResponse> createUploadId(String bucketName, String objectName) {
        // 直接使用原生请求对象，避免反复转换
        CreateMultipartUploadRequest request = CreateMultipartUploadRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .build();

        return s3AsyncClientMultipartUploadService.createMultipartUpload(request);
    }

    /**
     * 第二步：创建文件预上传地址
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @param uploadId   第一步中创建的 UploadId
     * @param partNumber 分片号
     * @return 预上传地址
     */
    public String createPresignedObjectUrl(String bucketName, String objectName, String uploadId, int partNumber) {

        Map<String, String> metadata = new HashMap<>();
        metadata.put("partNumber", String.valueOf(partNumber));
        metadata.put("uploadId", uploadId);

        return presignedUrlService.createUploadPresignedUrl(bucketName, objectName, metadata);
    }

    /**
     * 第三步：获取指定 uploadId 下所有的分片文件
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @param uploadId   第一步中创建的 UploadId
     * @return uploadId 对应的所有分片
     */
    public CompletableFuture<ListPartsResponse> listParts(String bucketName, String objectName, String uploadId) {

        ListPartsRequest request = ListPartsRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .uploadId(uploadId)
                .build();
        return s3AsyncClientMultipartUploadService.listParts(request);
    }

    /**
     * 创建大文件分片上传
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @param totalParts 分片总数
     * @return {@link CreateMultipartUploadBusiness}
     */
    public CreateMultipartUploadBusiness createMultipartUpload(String bucketName, String objectName, String uploadId, int totalParts) {
        CreateMultipartUploadBusiness entity = new CreateMultipartUploadBusiness(uploadId);

        // 从 1 开始才能保证 Minio 正确上传。
        for (int i = 1; i <= totalParts; i++) {
            String uploadUrl = createPresignedObjectUrl(bucketName, objectName, uploadId, i);
            entity.append(toProxy.convert(uploadUrl));
        }

        return entity;
    }
}
