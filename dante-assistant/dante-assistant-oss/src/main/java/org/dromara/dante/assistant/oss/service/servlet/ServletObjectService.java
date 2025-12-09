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

import cn.herodotus.dante.assistant.oss.converter.argument.*;
import cn.herodotus.dante.assistant.oss.converter.result.*;
import org.dromara.dante.assistant.oss.converter.argument.*;
import org.dromara.dante.assistant.oss.converter.result.*;
import org.dromara.dante.assistant.oss.definition.service.AbstractServletService;
import cn.herodotus.dante.assistant.oss.entity.argument.*;
import cn.herodotus.dante.assistant.oss.entity.result.*;
import org.dromara.dante.assistant.oss.entity.argument.*;
import org.dromara.dante.assistant.oss.entity.result.*;
import org.dromara.dante.assistant.oss.service.base.S3AsyncClientObjectService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;

/**
 * <p>Description: 阻塞式对象存储 Object 操作 Service </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/26 16:19
 */
@Service
public class ServletObjectService extends AbstractServletService {

    private final S3AsyncClientObjectService s3AsyncClientObjectService;

    public ServletObjectService(S3AsyncClientObjectService s3AsyncClientObjectService) {
        this.s3AsyncClientObjectService = s3AsyncClientObjectService;
    }

    public HeadObjectResult headObject(HeadObjectArgument argument) {
        return process(argument, new ArgumentToHeadObjectRequestConverter(), new ResponseToHeadObjectResultConverter(), s3AsyncClientObjectService::headObject);
    }

    public DeleteObjectResult deleteObject(DeleteObjectArgument argument) {
        return process(argument, new ArgumentToDeleteObjectRequestConverter(), new ResponseToDeleteObjectResultConverter(), s3AsyncClientObjectService::deleteObject);
    }

    public DeleteObjectsResult deleteObjects(DeleteObjectsArgument argument) {
        return process(argument, new ArgumentToDeleteObjectsRequestConverter(), new ResponseToDeleteObjectsResultConverter(), s3AsyncClientObjectService::deleteObjects);
    }

    public GetObjectResult getObject(GetObjectArgument argument) {
        return process(argument, new ArgumentToGetObjectRequestConverter(), new InputStreamToGetObjectResultConverter(), getObjectRequest -> s3AsyncClientObjectService.getObject(getObjectRequest, AsyncResponseTransformer.toBlockingInputStream()));
    }

    public ListObjectsV2Result listObjectsV2(ListObjectsV2Argument argument) {
        return process(argument, new ArgumentToListObjectsV2RequestConverter(), new ResponseToListObjectsV2ResultConverter(), s3AsyncClientObjectService::listObjectsV2);
    }

    public PutObjectResult putObject(PutObjectArgument argument, AsyncRequestBody requestBody) {
        return process(argument, new ArgumentToPutObjectRequestConverter(), new ResponseToPutObjectResultConverter(), request -> s3AsyncClientObjectService.putObject(request, requestBody));
    }

    public WriteGetObjectResponseResult writeGetObjectResponse(WriteGetObjectResponseArgument argument, AsyncRequestBody requestBody) {
        return process(argument, new ArgumentToWriteGetObjectResponseRequestConverter(), new ResponseToWriteGetObjectResponseResultConverter(), request -> s3AsyncClientObjectService.writeGetObjectResponse(request, requestBody));
    }
}
