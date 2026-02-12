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

import org.dromara.dante.assistant.oss.definition.service.AbstractS3AsyncClientService;
import org.dromara.dante.assistant.oss.pool.S3AsyncClientObjectPool;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.*;

import java.util.concurrent.CompletableFuture;

/**
 * <p>Description: 对象存储 Bucket 基本操作Service </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/21 17:09
 */
@Service
public class S3AsyncClientBucketService extends AbstractS3AsyncClientService {

    public S3AsyncClientBucketService(S3AsyncClientObjectPool objectPool) {
        super(objectPool);
    }

    public CompletableFuture<HeadBucketResponse> headBucket(HeadBucketRequest request) {
        return toFuture(client -> client.headBucket(request));
    }

    public CompletableFuture<CreateBucketResponse> createBucket(CreateBucketRequest request) {
        return toFuture(client -> client.createBucket(request));
    }

    public CompletableFuture<DeleteBucketResponse> deleteBucket(DeleteBucketRequest request) {
        return toFuture(client -> client.deleteBucket(request));
    }

    public CompletableFuture<ListBucketsResponse> listBuckets(ListBucketsRequest request) {
        return toFuture(client -> client.listBuckets(request));
    }

    public CompletableFuture<GetBucketAbacResponse> getBucketAbac(GetBucketAbacRequest request) {
        return toFuture(client -> client.getBucketAbac(request));
    }

    public CompletableFuture<GetBucketAclResponse> getBucketAcl(GetBucketAclRequest request) {
        return toFuture(client -> client.getBucketAcl(request));
    }

    public CompletableFuture<GetBucketAccelerateConfigurationResponse> getBucketAccelerateConfiguration(GetBucketAccelerateConfigurationRequest request) {
        return toFuture(client -> client.getBucketAccelerateConfiguration(request));
    }

    public CompletableFuture<GetBucketAnalyticsConfigurationResponse> getBucketAnalyticsConfiguration(GetBucketAnalyticsConfigurationRequest request) {
        return toFuture(client -> client.getBucketAnalyticsConfiguration(request));
    }

    public CompletableFuture<GetBucketCorsResponse> getBucketCors(GetBucketCorsRequest request) {
        return toFuture(client -> client.getBucketCors(request));
    }

    public CompletableFuture<GetBucketEncryptionResponse> getBucketEncryption(GetBucketEncryptionRequest request) {
        return toFuture(client -> client.getBucketEncryption(request));
    }

    public CompletableFuture<GetBucketIntelligentTieringConfigurationResponse> getBucketIntelligentTieringConfiguration(GetBucketIntelligentTieringConfigurationRequest request) {
        return toFuture(client -> client.getBucketIntelligentTieringConfiguration(request));
    }

    public CompletableFuture<GetBucketInventoryConfigurationResponse> getBucketInventoryConfiguration(GetBucketInventoryConfigurationRequest request) {
        return toFuture(client -> client.getBucketInventoryConfiguration(request));
    }

    public CompletableFuture<GetBucketLifecycleConfigurationResponse> getBucketLifecycleConfiguration(GetBucketLifecycleConfigurationRequest request) {
        return toFuture(client -> client.getBucketLifecycleConfiguration(request));
    }

    public CompletableFuture<GetBucketLocationResponse> getBucketLocation(GetBucketLocationRequest request) {
        return toFuture(client -> client.getBucketLocation(request));
    }

    public CompletableFuture<GetBucketLoggingResponse> getBucketLogging(GetBucketLoggingRequest request) {
        return toFuture(client -> client.getBucketLogging(request));
    }

    public CompletableFuture<GetBucketMetadataConfigurationResponse> getBucketMetadataConfiguration(GetBucketMetadataConfigurationRequest request) {
        return toFuture(client -> client.getBucketMetadataConfiguration(request));
    }

    public CompletableFuture<GetBucketMetadataTableConfigurationResponse> getBucketMetadataTableConfiguration(GetBucketMetadataTableConfigurationRequest request) {
        return toFuture(client -> client.getBucketMetadataTableConfiguration(request));
    }

    public CompletableFuture<GetBucketMetricsConfigurationResponse> getBucketMetricsConfiguration(GetBucketMetricsConfigurationRequest request) {
        return toFuture(client -> client.getBucketMetricsConfiguration(request));
    }

    public CompletableFuture<GetBucketNotificationConfigurationResponse> getBucketNotificationConfiguration(GetBucketNotificationConfigurationRequest request) {
        return toFuture(client -> client.getBucketNotificationConfiguration(request));
    }

    public CompletableFuture<GetBucketOwnershipControlsResponse> getBucketOwnershipControls(GetBucketOwnershipControlsRequest request) {
        return toFuture(client -> client.getBucketOwnershipControls(request));
    }

    public CompletableFuture<GetBucketPolicyResponse> getBucketPolicy(GetBucketPolicyRequest request) {
        return toFuture(client -> client.getBucketPolicy(request));
    }

    public CompletableFuture<GetBucketPolicyStatusResponse> getBucketPolicyStatus(GetBucketPolicyStatusRequest request) {
        return toFuture(client -> client.getBucketPolicyStatus(request));
    }

    public CompletableFuture<GetBucketReplicationResponse> getBucketReplication(GetBucketReplicationRequest request) {
        return toFuture(client -> client.getBucketReplication(request));
    }

    public CompletableFuture<GetBucketRequestPaymentResponse> getBucketRequestPayment(GetBucketRequestPaymentRequest request) {
        return toFuture(client -> client.getBucketRequestPayment(request));
    }

    public CompletableFuture<GetBucketTaggingResponse> getBucketTagging(GetBucketTaggingRequest request) {
        return toFuture(client -> client.getBucketTagging(request));
    }

    public CompletableFuture<GetBucketVersioningResponse> getBucketVersioning(GetBucketVersioningRequest request) {
        return toFuture(client -> client.getBucketVersioning(request));
    }

    public CompletableFuture<GetBucketWebsiteResponse> getBucketWebsite(GetBucketWebsiteRequest request) {
        return toFuture(client -> client.getBucketWebsite(request));
    }

    public CompletableFuture<PutBucketAbacResponse> putBucketAbac(PutBucketAbacRequest request) {
        return toFuture(client -> client.putBucketAbac(request));
    }

    public CompletableFuture<PutBucketAclResponse> putBucketAcl(PutBucketAclRequest request) {
        return toFuture(client -> client.putBucketAcl(request));
    }

    public CompletableFuture<PutBucketAccelerateConfigurationResponse> putBucketAccelerateConfiguration(PutBucketAccelerateConfigurationRequest request) {
        return toFuture(client -> client.putBucketAccelerateConfiguration(request));
    }

    public CompletableFuture<PutBucketAnalyticsConfigurationResponse> putBucketAnalyticsConfiguration(PutBucketAnalyticsConfigurationRequest request) {
        return toFuture(client -> client.putBucketAnalyticsConfiguration(request));
    }

    public CompletableFuture<PutBucketCorsResponse> putBucketCors(PutBucketCorsRequest request) {
        return toFuture(client -> client.putBucketCors(request));
    }

    public CompletableFuture<PutBucketEncryptionResponse> putBucketEncryption(PutBucketEncryptionRequest request) {
        return toFuture(client -> client.putBucketEncryption(request));
    }

    public CompletableFuture<PutBucketIntelligentTieringConfigurationResponse> putBucketIntelligentTieringConfiguration(PutBucketIntelligentTieringConfigurationRequest request) {
        return toFuture(client -> client.putBucketIntelligentTieringConfiguration(request));
    }

    public CompletableFuture<PutBucketInventoryConfigurationResponse> putBucketInventoryConfiguration(PutBucketInventoryConfigurationRequest request) {
        return toFuture(client -> client.putBucketInventoryConfiguration(request));
    }

    public CompletableFuture<PutBucketLifecycleConfigurationResponse> putBucketLifecycleConfiguration(PutBucketLifecycleConfigurationRequest request) {
        return toFuture(client -> client.putBucketLifecycleConfiguration(request));
    }

    public CompletableFuture<PutBucketLoggingResponse> putBucketLogging(PutBucketLoggingRequest request) {
        return toFuture(client -> client.putBucketLogging(request));
    }

    public CompletableFuture<PutBucketMetricsConfigurationResponse> putBucketMetricsConfiguration(PutBucketMetricsConfigurationRequest request) {
        return toFuture(client -> client.putBucketMetricsConfiguration(request));
    }

    public CompletableFuture<PutBucketNotificationConfigurationResponse> putBucketNotificationConfiguration(PutBucketNotificationConfigurationRequest request) {
        return toFuture(client -> client.putBucketNotificationConfiguration(request));
    }

    public CompletableFuture<PutBucketOwnershipControlsResponse> putBucketOwnershipControls(PutBucketOwnershipControlsRequest request) {
        return toFuture(client -> client.putBucketOwnershipControls(request));
    }

    public CompletableFuture<PutBucketPolicyResponse> putBucketPolicy(PutBucketPolicyRequest request) {
        return toFuture(client -> client.putBucketPolicy(request));
    }

    public CompletableFuture<PutBucketReplicationResponse> putBucketReplication(PutBucketReplicationRequest request) {
        return toFuture(client -> client.putBucketReplication(request));
    }

    public CompletableFuture<PutBucketRequestPaymentResponse> putBucketRequestPayment(PutBucketRequestPaymentRequest request) {
        return toFuture(client -> client.putBucketRequestPayment(request));
    }

    public CompletableFuture<PutBucketTaggingResponse> putBucketTagging(PutBucketTaggingRequest request) {
        return toFuture(client -> client.putBucketTagging(request));
    }

    public CompletableFuture<PutBucketVersioningResponse> putBucketVersioning(PutBucketVersioningRequest request) {
        return toFuture(client -> client.putBucketVersioning(request));
    }

    public CompletableFuture<PutBucketWebsiteResponse> putBucketWebsite(PutBucketWebsiteRequest request) {
        return toFuture(client -> client.putBucketWebsite(request));
    }

    public CompletableFuture<DeleteBucketAnalyticsConfigurationResponse> deleteBucketAnalyticsConfiguration(DeleteBucketAnalyticsConfigurationRequest request) {
        return toFuture(client -> client.deleteBucketAnalyticsConfiguration(request));
    }

    public CompletableFuture<DeleteBucketCorsResponse> deleteBucketCors(DeleteBucketCorsRequest request) {
        return toFuture(client -> client.deleteBucketCors(request));
    }

    public CompletableFuture<DeleteBucketEncryptionResponse> deleteBucketEncryption(DeleteBucketEncryptionRequest request) {
        return toFuture(client -> client.deleteBucketEncryption(request));
    }

    public CompletableFuture<DeleteBucketIntelligentTieringConfigurationResponse> deleteBucketIntelligentTieringConfiguration(DeleteBucketIntelligentTieringConfigurationRequest request) {
        return toFuture(client -> client.deleteBucketIntelligentTieringConfiguration(request));
    }

    public CompletableFuture<DeleteBucketInventoryConfigurationResponse> deleteBucketInventoryConfiguration(DeleteBucketInventoryConfigurationRequest request) {
        return toFuture(client -> client.deleteBucketInventoryConfiguration(request));
    }

    public CompletableFuture<DeleteBucketMetadataConfigurationResponse> deleteBucketMetadataConfiguration(DeleteBucketMetadataConfigurationRequest request) {
        return toFuture(client -> client.deleteBucketMetadataConfiguration(request));
    }

    public CompletableFuture<DeleteBucketMetadataTableConfigurationResponse> deleteBucketMetadataTableConfiguration(DeleteBucketMetadataTableConfigurationRequest request) {
        return toFuture(client -> client.deleteBucketMetadataTableConfiguration(request));
    }

    public CompletableFuture<DeleteBucketMetricsConfigurationResponse> deleteBucketMetricsConfiguration(DeleteBucketMetricsConfigurationRequest request) {
        return toFuture(client -> client.deleteBucketMetricsConfiguration(request));
    }

    public CompletableFuture<DeleteBucketOwnershipControlsResponse> deleteBucketOwnershipControls(DeleteBucketOwnershipControlsRequest request) {
        return toFuture(client -> client.deleteBucketOwnershipControls(request));
    }

    public CompletableFuture<DeleteBucketPolicyResponse> deleteBucketPolicy(DeleteBucketPolicyRequest request) {
        return toFuture(client -> client.deleteBucketPolicy(request));
    }

    public CompletableFuture<DeleteBucketReplicationResponse> deleteBucketReplication(DeleteBucketReplicationRequest request) {
        return toFuture(client -> client.deleteBucketReplication(request));
    }

    public CompletableFuture<DeleteBucketTaggingResponse> deleteBucketTagging(DeleteBucketTaggingRequest request) {
        return toFuture(client -> client.deleteBucketTagging(request));
    }

    public CompletableFuture<DeleteBucketWebsiteResponse> deleteBucketWebsite(DeleteBucketWebsiteRequest request) {
        return toFuture(client -> client.deleteBucketWebsite(request));
    }

    public CompletableFuture<ListBucketAnalyticsConfigurationsResponse> listBucketAnalyticsConfigurations(ListBucketAnalyticsConfigurationsRequest request) {
        return toFuture(client -> client.listBucketAnalyticsConfigurations(request));
    }

    public CompletableFuture<ListBucketIntelligentTieringConfigurationsResponse> listBucketIntelligentTieringConfigurations(ListBucketIntelligentTieringConfigurationsRequest request) {
        return toFuture(client -> client.listBucketIntelligentTieringConfigurations(request));
    }

    public CompletableFuture<ListBucketInventoryConfigurationsResponse> listBucketInventoryConfigurations(ListBucketInventoryConfigurationsRequest request) {
        return toFuture(client -> client.listBucketInventoryConfigurations(request));
    }

    public CompletableFuture<ListBucketMetricsConfigurationsResponse> listBucketMetricsConfigurations(ListBucketMetricsConfigurationsRequest request) {
        return toFuture(client -> client.listBucketMetricsConfigurations(request));
    }

    public CompletableFuture<UpdateBucketMetadataInventoryTableConfigurationResponse> updateBucketMetadataInventoryTableConfiguration(UpdateBucketMetadataInventoryTableConfigurationRequest request) {
        return toFuture(client -> client.updateBucketMetadataInventoryTableConfiguration(request));
    }

    public CompletableFuture<UpdateBucketMetadataJournalTableConfigurationResponse> updateBucketMetadataJournalTableConfiguration(UpdateBucketMetadataJournalTableConfigurationRequest request) {
        return toFuture(client -> client.updateBucketMetadataJournalTableConfiguration(request));
    }

    public CompletableFuture<CreateBucketMetadataConfigurationResponse> createBucketMetadataConfiguration(CreateBucketMetadataConfigurationRequest request) {
        return toFuture(client -> client.createBucketMetadataConfiguration(request));
    }

    public CompletableFuture<CreateBucketMetadataTableConfigurationResponse> createBucketMetadataTableConfiguration(CreateBucketMetadataTableConfigurationRequest request) {
        return toFuture(client -> client.createBucketMetadataTableConfiguration(request));
    }
}
