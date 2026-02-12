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

package org.dromara.dante.assistant.oss.converter.result;

import cn.hutool.v7.core.date.DateUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.dromara.dante.assistant.oss.entity.result.GetObjectAttributesResult;
import org.dromara.dante.assistant.oss.enums.ObjectRetentionMode;
import org.springframework.core.convert.converter.Converter;
import software.amazon.awssdk.services.s3.model.*;

/**
 * <p>Description: {@link GetObjectAttributesResponse} 转 {@link GetObjectAttributesResult} 转换器</p>
 *
 * @author : gengwei.zheng
 * @date : 2026/2/6 21:33
 */
public class ResponseToGetObjectAttributesResultConverter implements Converter<GetObjectAttributesResponse, GetObjectAttributesResult> {

    private final String bucketName;
    private final String objectName;
    private final GetObjectLockConfigurationResponse objectLockResponse;
    private final GetObjectLegalHoldResponse objectLegalHoldResponse;
    private final GetObjectRetentionResponse objectRetentionResponse;

    public ResponseToGetObjectAttributesResultConverter(String bucketName, String objectName, GetObjectLockConfigurationResponse objectLockResponse, GetObjectLegalHoldResponse objectLegalHoldResponse, GetObjectRetentionResponse objectRetentionResponse) {
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.objectLockResponse = objectLockResponse;
        this.objectLegalHoldResponse = objectLegalHoldResponse;
        this.objectRetentionResponse = objectRetentionResponse;
    }


    @Override
    public GetObjectAttributesResult convert(GetObjectAttributesResponse source) {

        GetObjectAttributesResult details = new GetObjectAttributesResult();
        details.setBucketName(bucketName);
        details.setObjectName(objectName);
        details.setLockEnabled(ObjectUtils.isNotEmpty(this.objectLockResponse.objectLockConfiguration()));

        if (ObjectUtils.isNotEmpty(objectRetentionResponse.retention())) {
            if (ObjectUtils.isNotEmpty(objectRetentionResponse.retention().mode())) {
                details.setRetentionMode(ObjectRetentionMode.get(objectRetentionResponse.retention().mode().name()));
            }
            details.setRetainUntilDate(DateUtil.toLocalDateTime(objectRetentionResponse.retention().retainUntilDate()));
        }

        if (ObjectUtils.isNotEmpty(objectLegalHoldResponse.legalHold()) && ObjectUtils.isNotEmpty(objectLegalHoldResponse.legalHold().status())) {
            details.setLockLegalHold(objectLegalHoldResponse.legalHold().status() == ObjectLockLegalHoldStatus.ON);
        }

        details.setDeleteMarker(source.deleteMarker());
        details.setLastModified(DateUtil.toLocalDateTime(source.lastModified()));
        details.setVersionId(source.versionId());
        details.setTag(source.eTag());
        details.setSize(source.objectSize());

        return details;
    }
}
