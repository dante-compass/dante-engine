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
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.model.*;

import java.util.concurrent.CompletableFuture;

/**
 * <p>Description: 对象存储 Object 基本操作Service </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/21 17:38
 */
@Service
public class S3AsyncClientObjectService extends AbstractS3AsyncClientService {

    public S3AsyncClientObjectService(S3AsyncClientObjectPool objectPool) {
        super(objectPool);
    }

    public CompletableFuture<HeadObjectResponse> headObject(HeadObjectRequest headObjectRequest) {
        return toFuture(client -> client.headObject(headObjectRequest));
    }

    public CompletableFuture<DeleteObjectResponse> deleteObject(DeleteObjectRequest deleteObjectRequest) {
        return toFuture(client -> client.deleteObject(deleteObjectRequest));
    }

    public CompletableFuture<DeleteObjectsResponse> deleteObjects(DeleteObjectsRequest deleteObjectsRequest) {
        return toFuture(client -> client.deleteObjects(deleteObjectsRequest));
    }

    public <ReturnT> CompletableFuture<ReturnT> getObject(GetObjectRequest getObjectRequest, AsyncResponseTransformer<GetObjectResponse, ReturnT> asyncResponseTransformer) {
        return toFuture(client -> client.getObject(getObjectRequest, asyncResponseTransformer));
    }

    public CompletableFuture<ListObjectsV2Response> listObjectsV2(ListObjectsV2Request listObjectsV2Request) {
        return toFuture(client -> client.listObjectsV2(listObjectsV2Request));
    }

    public CompletableFuture<PutObjectResponse> putObject(PutObjectRequest putObjectRequest, AsyncRequestBody requestBody) {
        return toFuture(client -> client.putObject(putObjectRequest, requestBody));
    }

    public CompletableFuture<WriteGetObjectResponseResponse> writeGetObjectResponse(WriteGetObjectResponseRequest writeGetObjectResponseRequest, AsyncRequestBody requestBody) {
        return toFuture(client -> client.writeGetObjectResponse(writeGetObjectResponseRequest, requestBody));
    }
}
