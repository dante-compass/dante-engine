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

package org.dromara.dante.assistant.oss.service.servlet;

import org.dromara.dante.assistant.oss.converter.argument.ArgumentToCreateBucketRequestConverter;
import org.dromara.dante.assistant.oss.converter.argument.ArgumentToDeleteBucketRequestConverter;
import org.dromara.dante.assistant.oss.converter.argument.ArgumentToHeadBucketRequestConverter;
import org.dromara.dante.assistant.oss.converter.argument.ArgumentToListBucketsRequestConverter;
import org.dromara.dante.assistant.oss.converter.result.ResponseToCreateBucketResultConverter;
import org.dromara.dante.assistant.oss.converter.result.ResponseToDeleteBucketResultConverter;
import org.dromara.dante.assistant.oss.converter.result.ResponseToHeadBucketResultConverter;
import org.dromara.dante.assistant.oss.converter.result.ResponseToListBucketsResultConverter;
import org.dromara.dante.assistant.oss.definition.service.AbstractServletService;
import org.dromara.dante.assistant.oss.entity.argument.CreateBucketArgument;
import org.dromara.dante.assistant.oss.entity.argument.DeleteBucketArgument;
import org.dromara.dante.assistant.oss.entity.argument.HeadBucketArgument;
import org.dromara.dante.assistant.oss.entity.argument.ListBucketsArgument;
import org.dromara.dante.assistant.oss.entity.result.CreateBucketResult;
import org.dromara.dante.assistant.oss.entity.result.DeleteBucketResult;
import org.dromara.dante.assistant.oss.entity.result.HeadBucketResult;
import org.dromara.dante.assistant.oss.entity.result.ListBucketsResult;
import org.dromara.dante.assistant.oss.service.base.S3AsyncClientBucketService;
import org.springframework.stereotype.Service;

/**
 * <p>Description: 阻塞式 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/22 18:03
 */
@Service
public class ServletBucketService extends AbstractServletService {

    private final S3AsyncClientBucketService bucketService;

    public ServletBucketService(S3AsyncClientBucketService bucketService) {
        this.bucketService = bucketService;
    }

    public CreateBucketResult createBucket(CreateBucketArgument argument) {
        return process(argument, new ArgumentToCreateBucketRequestConverter(), new ResponseToCreateBucketResultConverter(), bucketService::createBucket);
    }

    public DeleteBucketResult deleteBucket(DeleteBucketArgument argument) {
        return process(argument, new ArgumentToDeleteBucketRequestConverter(), new ResponseToDeleteBucketResultConverter(), bucketService::deleteBucket);
    }

    public HeadBucketResult headBucket(HeadBucketArgument argument) {
        return process(argument, new ArgumentToHeadBucketRequestConverter(), new ResponseToHeadBucketResultConverter(), bucketService::headBucket);
    }

    public ListBucketsResult listBuckets(ListBucketsArgument argument) {
        return process(argument, new ArgumentToListBucketsRequestConverter(), new ResponseToListBucketsResultConverter(), bucketService::listBuckets);
    }
}
