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

import org.apache.commons.collections4.CollectionUtils;
import org.dromara.dante.assistant.oss.converter.domain.CommonPrefixesToDomainConverter;
import org.dromara.dante.assistant.oss.converter.domain.S3ObjectsToDomainConverter;
import org.dromara.dante.assistant.oss.definition.converter.ResponseConverter;
import org.dromara.dante.assistant.oss.entity.domain.ObjectDomain;
import org.dromara.dante.assistant.oss.entity.result.ListObjectsV2Result;
import org.dromara.dante.spring.founction.ListConverter;
import software.amazon.awssdk.services.s3.model.CommonPrefix;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: AWS OSS API 响应转换为 ListObjectsV2Result 转换器</p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/15 16:10
 */
public class ResponseToListObjectsV2ResultConverter implements ResponseConverter<ListObjectsV2Response, ListObjectsV2Result> {

    @Override
    public void prepare(ListObjectsV2Response source, ListObjectsV2Result target) {
        target.setBucketName(source.name());
        target.setContents(getContents(source));
        target.setContinuationToken(source.continuationToken());
        target.setDelimiter(source.delimiter());
        target.setEncodingType(source.encodingTypeAsString());
        target.setTruncated(source.isTruncated());
        target.setKeyCount(source.keyCount());
        target.setMaxKeys(source.maxKeys());
        target.setNextContinuationToken(source.nextContinuationToken());
        target.setPrefix(source.prefix());
        target.setStartAfter(source.startAfter());
        ResponseConverter.super.prepare(source, target);
    }

    @Override
    public ListObjectsV2Result getInstance(ListObjectsV2Response source) {
        return new ListObjectsV2Result();
    }

    private List<ObjectDomain> getContents(ListObjectsV2Response source) {
        ListConverter<CommonPrefix, ObjectDomain> toCommonPrefixesResult = new CommonPrefixesToDomainConverter();
        ListConverter<S3Object, ObjectDomain> toS3ObjectsResult = new S3ObjectsToDomainConverter(source.delimiter());

        List<ObjectDomain> results = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(source.commonPrefixes())) {
            List<ObjectDomain> commonPrefixes = toCommonPrefixesResult.convert(source.commonPrefixes());
            if (CollectionUtils.isNotEmpty(commonPrefixes)) {
                results.addAll(commonPrefixes);
            }
        }

        if (CollectionUtils.isNotEmpty(source.contents())) {
            List<ObjectDomain> contents = toS3ObjectsResult.convert(source.contents());
            if (CollectionUtils.isNotEmpty(contents)) {
                results.addAll(contents);
            }
        }

        return results;
    }
}
