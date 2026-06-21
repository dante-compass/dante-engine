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

package cn.herodotus.dante.assistant.oss.converter.result;

import cn.herodotus.dante.assistant.oss.converter.domain.DeleteMarkerEntryToConverter;
import cn.herodotus.dante.assistant.oss.converter.domain.ObjectVersionToDomainConverter;
import cn.herodotus.dante.assistant.oss.definition.converter.ResponseConverter;
import cn.herodotus.dante.assistant.oss.entity.domain.ObjectVersionDomain;
import cn.herodotus.dante.assistant.oss.entity.result.ListObjectVersionsResult;
import cn.herodotus.dante.assistant.oss.entity.result.ListObjectsV2Result;
import cn.herodotus.dante.spring.founction.ListConverter;
import software.amazon.awssdk.services.s3.model.DeleteMarkerEntry;
import software.amazon.awssdk.services.s3.model.ListObjectVersionsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.ObjectVersion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * <p>Description: {@link ListObjectsV2Response} 转 {@link ListObjectsV2Result} 转换器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2026/2/15 13:00
 */
public class ResponseToListObjectVersionsConverter implements ResponseConverter<ListObjectVersionsResponse, ListObjectVersionsResult> {

    @Override
    public void prepare(ListObjectVersionsResponse source, ListObjectVersionsResult target) {
        target.setBucketName(source.name());
        target.setDelimiter(source.delimiter());
        target.setEncodingType(source.encodingTypeAsString());
        target.setTruncated(source.isTruncated());
        target.setMaxKeys(source.maxKeys());
        target.setPrefix(source.prefix());

        target.setVersions(getVersions(source));
        target.setKeyMarker(source.keyMarker());
        target.setVersionIdMarker(source.versionIdMarker());
        target.setNextKeyMarker(source.nextKeyMarker());
        target.setNextVersionIdMarker(source.nextVersionIdMarker());

        ResponseConverter.super.prepare(source, target);
    }

    @Override
    public ListObjectVersionsResult getInstance(ListObjectVersionsResponse source) {
        return new ListObjectVersionsResult();
    }

    private List<ObjectVersionDomain> getVersions(ListObjectVersionsResponse source) {
        ListConverter<ObjectVersion, ObjectVersionDomain> fromObjectVersion = new ObjectVersionToDomainConverter();
        ListConverter<DeleteMarkerEntry, ObjectVersionDomain> fromDeleteMarkerEntry = new DeleteMarkerEntryToConverter();

        List<ObjectVersionDomain> results = new ArrayList<>();

        Optional.ofNullable(source.versions())
                .map(fromObjectVersion::convert)
                .ifPresent(results::addAll);

        Optional.ofNullable(source.deleteMarkers())
                .map(fromDeleteMarkerEntry::convert)
                .ifPresent(results::addAll);

        if (results.size() > 2) {
            return results.stream()
                    .sorted(Comparator.comparing(ObjectVersionDomain::getLastModified).reversed())
                    .toList();
        }

        return results;
    }
}
