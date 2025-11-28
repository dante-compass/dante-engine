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

package cn.herodotus.dante.assistant.oss.service.logic;

import cn.herodotus.dante.assistant.oss.definition.service.AbstractS3PresignerService;
import cn.herodotus.dante.assistant.oss.pool.S3PresignerObjectPool;
import cn.herodotus.dante.assistant.oss.properties.OssProperties;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.*;

import java.net.URL;
import java.time.Duration;
import java.util.Map;

/**
 * <p>Description: 预签名处理 Service </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/10/19 16:46
 */
@Service
public class PresignedUrlService extends AbstractS3PresignerService {

    private static final Logger log = LoggerFactory.getLogger(PresignedUrlService.class);

    private final OssProperties ossProperties;

    public PresignedUrlService(S3PresignerObjectPool objectPool, OssProperties ossProperties) {
        super(objectPool);
        this.ossProperties = ossProperties;
    }

    /**
     * 创建下载预签名地址
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @param expiration 有效期 {@link Duration}
     * @return 预签名地址
     */
    public String createDownloadPresignedUrl(String bucketName, String objectName, Duration expiration) {

        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(expiration)
                .getObjectRequest(objectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = process(presigner -> presigner.presignGetObject(presignRequest));

        URL url = presignedRequest.url();
        log.debug("[Herodotus] |- PreSigned URL to download a file with HTTP method [{}] to: [{}]", presignedRequest.httpRequest().method(), url.toString());
        return url.toExternalForm();
    }

    /**
     * 创建下载预签名地址
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @return 预签名地址
     */
    public String createDownloadPresignedUrl(String bucketName, String objectName) {
        return createDownloadPresignedUrl(bucketName, objectName, ossProperties.getPreSignedExpires());
    }

    /**
     * 创建上传预签名地址
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @param expiration 有效期 {@link Duration}
     * @param metadata   元数据 {@link Map}
     * @return 预签名地址
     */
    public String createUploadPresignedUrl(String bucketName, String objectName, Duration expiration, Map<String, String> metadata) {

        PutObjectRequest.Builder request = PutObjectRequest.builder();
        request.bucket(bucketName);
        request.key(objectName);
        if (MapUtils.isNotEmpty(metadata)) {
            request.metadata(metadata);
        }

        PutObjectPresignRequest preSignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(expiration)
                .putObjectRequest(request.build())
                .build();

        PresignedPutObjectRequest preSignedRequest = process(presigner -> presigner.presignPutObject(preSignRequest));

        URL url = preSignedRequest.url();
        log.debug("[Herodotus] |- PreSigned URL to upload a file with HTTP method [{}] to: [{}]", preSignedRequest.httpRequest().method(), url.toString());
        return url.toExternalForm();
    }

    /**
     * 创建上传预签名地址
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @param metadata   元数据 {@link Map}
     * @return 预签名地址
     */
    public String createUploadPresignedUrl(String bucketName, String objectName, Map<String, String> metadata) {
        return createUploadPresignedUrl(bucketName, objectName, ossProperties.getPreSignedExpires(), metadata);
    }

    /**
     * 创建上传预签名地址
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @return 预签名地址
     */
    public String createUploadPresignedUrl(String bucketName, String objectName) {
        return createUploadPresignedUrl(bucketName, objectName, null);
    }

    /**
     * 创建上传预签名地址
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @param expiration 有效期 {@link Duration}
     * @return 预签名地址
     */
    public String createDeletePresignedUrl(String bucketName, String objectName, Duration expiration) {

        DeleteObjectRequest.Builder request = DeleteObjectRequest.builder();
        request.bucket(bucketName);
        request.key(objectName);

        DeleteObjectPresignRequest preSignRequest = DeleteObjectPresignRequest.builder()
                .signatureDuration(expiration)
                .deleteObjectRequest(request.build())
                .build();

        PresignedDeleteObjectRequest preSignedRequest = process(presigner -> presigner.presignDeleteObject(preSignRequest));

        URL url = preSignedRequest.url();
        log.debug("[Herodotus] |- PreSigned URL to delete a file with HTTP method [{}] to: [{}]", preSignedRequest.httpRequest().method(), url.toString());
        return url.toExternalForm();
    }

    /**
     * 创建上传预签名地址
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @return 预签名地址
     */
    public String createDeletePresignedUrl(String bucketName, String objectName) {
        return createDeletePresignedUrl(bucketName, objectName, ossProperties.getPreSignedExpires());
    }
}
