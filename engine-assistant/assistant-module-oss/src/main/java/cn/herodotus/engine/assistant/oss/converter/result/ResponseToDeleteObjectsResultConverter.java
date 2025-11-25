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

import cn.herodotus.engine.assistant.oss.converter.domain.DeletedObjectsToDomainConverter;
import cn.herodotus.engine.assistant.oss.converter.domain.S3ErrorToDomainConverter;
import cn.herodotus.engine.assistant.oss.definition.converter.ResponseConverter;
import cn.herodotus.engine.assistant.oss.definition.domain.DeletedObjectDomain;
import cn.herodotus.engine.assistant.oss.definition.domain.S3ErrorDomain;
import cn.herodotus.engine.assistant.oss.entity.result.DeleteObjectsResult;
import cn.herodotus.engine.core.foundation.founction.ListConverter;
import org.apache.commons.collections4.CollectionUtils;
import software.amazon.awssdk.services.s3.model.DeleteObjectsResponse;
import software.amazon.awssdk.services.s3.model.DeletedObject;
import software.amazon.awssdk.services.s3.model.S3Error;

import java.util.List;

/**
 * <p>Description: Response 转 Result 转换器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/26 0:53
 */
public class ResponseToDeleteObjectsResultConverter implements ResponseConverter<DeleteObjectsResponse, DeleteObjectsResult> {

    private final ListConverter<DeletedObject, DeletedObjectDomain> toDeletedObject;
    private final ListConverter<S3Error, S3ErrorDomain> toS3Error;

    public ResponseToDeleteObjectsResultConverter() {
        this.toDeletedObject = new DeletedObjectsToDomainConverter();
        this.toS3Error = new S3ErrorToDomainConverter();
    }

    @Override
    public DeleteObjectsResult getInstance(DeleteObjectsResponse source) {
        return new DeleteObjectsResult();
    }

    @Override
    public void prepare(DeleteObjectsResponse source, DeleteObjectsResult target) {
        target.setDeleted(CollectionUtils.isNotEmpty(source.deleted()) ? toDeletedObject.convert(source.deleted()) : List.of());
        target.setErrors(CollectionUtils.isNotEmpty(source.errors()) ? toS3Error.convert(source.errors()) : List.of());
        target.setRequestCharged(source.requestChargedAsString());
        ResponseConverter.super.prepare(source, target);
    }
}
