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

package org.dromara.dante.assistant.oss.service.base;

import cn.hutool.v7.core.date.DateUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.dromara.dante.assistant.oss.converter.result.ResponseToGetObjectAttributesResultConverter;
import org.dromara.dante.assistant.oss.converter.result.ResponseToListBucketDetailsResultConverter;
import org.dromara.dante.assistant.oss.definition.service.AbstractS3AsyncClientService;
import org.dromara.dante.assistant.oss.entity.domain.BucketDetailsDomain;
import org.dromara.dante.assistant.oss.entity.result.GetObjectAttributesResult;
import org.dromara.dante.assistant.oss.entity.result.ListBucketDetailsResult;
import org.dromara.dante.assistant.oss.enums.BucketVersioning;
import org.dromara.dante.assistant.oss.pool.S3AsyncClientObjectPool;
import org.dromara.dante.assistant.oss.utils.OssUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * <p>Description: 存储桶组合逻辑 Service </p>
 *
 * @author : gengwei.zheng
 * @date : 2026/2/6 14:30
 */
@Service
public class S3AsyncClientCompositeService extends AbstractS3AsyncClientService {

    private static final Logger log = LoggerFactory.getLogger(S3AsyncClientCompositeService.class);

    public S3AsyncClientCompositeService(S3AsyncClientObjectPool objectPool) {
        super(objectPool);
    }


    /**
     * 获取存储桶相关信息。
     * <p>
     * 以后有需要扩展的信息，可以再这里添加.
     * <p>
     * 传递 {@link S3AsyncClient}，方便在同一个 client 中，完成所有操作
     *
     * @param client S3 异步客户端 {@link S3AsyncClient}
     * @param bucket 存储桶 {@link Bucket}
     * @return 存储桶详情 {@link BucketDetailsDomain}
     */
    private CompletableFuture<BucketDetailsDomain> getBucketDetails(S3AsyncClient client, Bucket bucket) {
        // 使用同一个客户端并行获取策略和版本信息
        CompletableFuture<GetBucketPolicyResponse> policyFuture = client.getBucketPolicy(b -> b.bucket(bucket.name())).exceptionally(e -> null);
        CompletableFuture<GetBucketVersioningResponse> versioningFuture = client.getBucketVersioning(b -> b.bucket(bucket.name())).exceptionally(e -> null);
        CompletableFuture<GetObjectLockConfigurationResponse> objectLockFuture = client.getObjectLockConfiguration(b -> b.bucket(bucket.name())).exceptionally(e -> null);

        return CompletableFuture.allOf(policyFuture, versioningFuture, objectLockFuture)
                .thenApply(v -> {

                    GetBucketPolicyResponse policyResponse = policyFuture.join();
                    GetBucketVersioningResponse versioningResponse = versioningFuture.join();
                    GetObjectLockConfigurationResponse objectLockResponse = objectLockFuture.join();

                    BucketDetailsDomain details = new BucketDetailsDomain();
                    details.setCreationDate(DateUtil.toLocalDateTime(bucket.creationDate()));
                    details.setBucketName(bucket.name());
                    details.setBucketRegion(bucket.bucketRegion());
                    details.setDoesPublic(OssUtils.isBucketPublic(policyResponse));

                    if (ObjectUtils.isNotEmpty(versioningResponse) && ObjectUtils.isNotEmpty(versioningResponse.status())) {
                        details.setVersioning(BucketVersioning.get(versioningResponse.status().name()));
                    }

                    details.setObjectLockEnabled(ObjectUtils.isNotEmpty(objectLockResponse) && ObjectUtils.isNotEmpty(objectLockResponse.objectLockConfiguration()));

                    return details;
                });
    }

    /**
     * 获取包含更详细存储桶信息的列表。
     * <p>
     * 使用同一个 client 完成多项操作。
     *
     * @param client  S3 异步客户端 {@link S3AsyncClient}
     * @param request 请求 {@link ListBucketsRequest}
     * @return 存储桶详情列表 {@link ListBucketDetailsResult}
     */
    private CompletableFuture<ListBucketDetailsResult> listBucketDetails(S3AsyncClient client, ListBucketsRequest request) {
        return client.listBuckets(request)
                .thenCompose(listBucketsResponse -> {
                    // 并行获取每个bucket的详细信息
                    List<CompletableFuture<BucketDetailsDomain>> futures = listBucketsResponse.buckets().stream()
                            .map(bucket -> getBucketDetails(client, bucket))
                            .toList();

                    return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                            .thenApply(v -> {
                                List<BucketDetailsDomain> details = futures.stream()
                                        .map(CompletableFuture::join)
                                        .toList();

                                Converter<ListBucketsResponse, ListBucketDetailsResult> converter = new ResponseToListBucketDetailsResultConverter(details);
                                return converter.convert(listBucketsResponse);
                            });
                });
    }

    public CompletableFuture<ListBucketDetailsResult> listBucketDetails(ListBucketsRequest request) {
        return toFuture(client -> listBucketDetails(client, request));
    }


    /**
     * 获取对象详细信息
     * <p>
     * 以后有需要扩展的信息，可以再这里添加.
     * <p>
     * 传递 {@link S3AsyncClient}，方便在同一个 client 中，完成所有操作
     *
     * @param client  S3 异步客户端 {@link S3AsyncClient}
     * @param request 获取对象属性请求参数 {@link GetObjectAttributesRequest}
     * @return 对象详情 {@link GetObjectAttributesResult}
     */
    private CompletableFuture<GetObjectAttributesResult> getObjectAttributes(S3AsyncClient client, GetObjectAttributesRequest request) {

        String bucketName = request.bucket();
        String objectName = request.key();

        return client.getObjectAttributes(request)
                .thenCompose(getObjectAttributesResponse -> {
                    // 使用同一个客户端并行获取策略和版本信息
                    CompletableFuture<GetObjectLockConfigurationResponse> objectLockFuture = client.getObjectLockConfiguration(b -> b.bucket(bucketName)).exceptionally(e -> {
                        // 可能没有 lock 的情况
                        log.warn("[Herodotus] |- S3 getObjectLockConfiguration for [{}] catch error.", bucketName, e);
                        return null;
                    });

                    CompletableFuture<GetObjectLegalHoldResponse> objectLegalHoldFuture = client.getObjectLegalHold(b -> b.bucket(bucketName).key(objectName)).exceptionally(e -> {
                        // 可能没有 legalHold 的情况
                        log.warn("[Herodotus] |- S3 getObjectLegalHold for object [{}] in bucket [{}] catch error.", objectName, bucketName, e);
                        return null;
                    });

                    CompletableFuture<GetObjectRetentionResponse> objectRetentionFuture = client.getObjectRetention(b -> b.bucket(bucketName).key(objectName)).exceptionally(e -> {
                        // 可能没有 legalHold 的情况
                        log.warn("[Herodotus] |- S3 getObjectLegalHold for object [{}] in bucket [{}] catch error.", objectName, bucketName, e);
                        return null;
                    });

                    return CompletableFuture.allOf(objectLockFuture, objectLegalHoldFuture, objectRetentionFuture)
                            .thenApply(v -> {
                                GetObjectLockConfigurationResponse objectLockResponse = objectLockFuture.join();
                                GetObjectLegalHoldResponse objectLegalHoldResponse = objectLegalHoldFuture.join();
                                GetObjectRetentionResponse objectRetentionResponse = objectRetentionFuture.join();

                                Converter<GetObjectAttributesResponse, GetObjectAttributesResult> toDetails = new ResponseToGetObjectAttributesResultConverter(bucketName, objectName, objectLockResponse, objectLegalHoldResponse, objectRetentionResponse);

                                return toDetails.convert(getObjectAttributesResponse);
                            });
                });
    }

    public CompletableFuture<GetObjectAttributesResult> getObjectAttributes(GetObjectAttributesRequest request) {
        return toFuture(client -> getObjectAttributes(client, request));
    }
}
