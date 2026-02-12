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

package org.dromara.dante.assistant.oss.definition.service;

import org.dromara.dante.assistant.oss.exception.*;
import org.dromara.dante.assistant.oss.pool.S3AsyncClientObjectPool;
import org.dromara.dante.core.support.pool.AbstractPooledObjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.*;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * <p>Description: AsyncClient 基础操作定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/21 17:26
 */
public abstract class AbstractS3AsyncClientService extends AbstractPooledObjectService<S3AsyncClient> {

    private static final Logger log = LoggerFactory.getLogger(AbstractS3AsyncClientService.class);

    public AbstractS3AsyncClientService(S3AsyncClientObjectPool objectPool) {
        super(objectPool);
    }

    protected <T> CompletableFuture<T> toFuture(Function<S3AsyncClient, CompletableFuture<T>> operate) {
        S3AsyncClient client = getClient();
        CompletableFuture<T> future = operate.apply(client);
        close(client);
        return future.exceptionally(throwable -> {
            Throwable cause = throwable.getCause();

            log.error("[Herodotus] |- AWS s3 error: {}", cause.getMessage(), cause);

            if (cause instanceof S3Exception s3Exception) {
                switch (s3Exception) {
                    case AccessDeniedException accessDeniedException ->
                            throw new OssAccessDeniedException(accessDeniedException.getMessage(), accessDeniedException);
                    case BucketAlreadyOwnedByYouException bucketAlreadyOwnedByYouException ->
                            throw new OssBucketAlreadyOwnedByYouException(bucketAlreadyOwnedByYouException.getMessage(), bucketAlreadyOwnedByYouException);
                    case BucketAlreadyExistsException bucketAlreadyExistsException ->
                            throw new OssBucketAlreadyExistsException(bucketAlreadyExistsException.getMessage(), bucketAlreadyExistsException);
                    case InvalidObjectStateException invalidObjectStateException ->
                            throw new OssInvalidObjectStateException(invalidObjectStateException.getMessage(), invalidObjectStateException);
                    case InvalidRequestException invalidRequestException ->
                            throw new OssInvalidRequestException(invalidRequestException.getMessage(), invalidRequestException);
                    case NoSuchBucketException noSuchBucketException ->
                            throw new OssNoSuchBucketException(noSuchBucketException.getMessage(), noSuchBucketException);
                    case NoSuchKeyException noSuchKeyException ->
                            throw new OssNoSuchKeyException(noSuchKeyException.getMessage(), noSuchKeyException);
                    case NoSuchUploadException noSuchUploadException ->
                            throw new OssNoSuchUploadException(noSuchUploadException.getMessage(), noSuchUploadException);
                    default -> throw new RuntimeException(cause);
                }
            }

            throw new RuntimeException(throwable);
        });
    }
}
