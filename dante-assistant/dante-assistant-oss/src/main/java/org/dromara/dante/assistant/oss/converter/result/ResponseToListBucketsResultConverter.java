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

import org.dromara.dante.assistant.oss.converter.domain.BucketsToDomainsConverter;
import org.dromara.dante.assistant.oss.converter.domain.OwnerToDomainConverter;
import org.dromara.dante.assistant.oss.definition.converter.ResponseConverter;
import org.dromara.dante.assistant.oss.entity.domain.BucketDomain;
import org.dromara.dante.assistant.oss.entity.domain.OwnerDomain;
import org.dromara.dante.assistant.oss.entity.result.ListBucketsResult;
import org.dromara.dante.spring.founction.ListConverter;
import org.springframework.core.convert.converter.Converter;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.Owner;

/**
 * <p>Description: {@link ListBucketsResponse} 转 {@link ListBucketsResult} 转换器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/22 0:21
 */
public class ResponseToListBucketsResultConverter implements ResponseConverter<ListBucketsResponse, ListBucketsResult> {

    private final ListConverter<Bucket, BucketDomain> toBucketDomain;
    private final Converter<Owner, OwnerDomain> toOwnerDomain;

    public ResponseToListBucketsResultConverter() {
        this.toBucketDomain = new BucketsToDomainsConverter();
        this.toOwnerDomain = new OwnerToDomainConverter();
    }

    @Override
    public void prepare(ListBucketsResponse source, ListBucketsResult target) {
        target.setBuckets(toBucketDomain.convert(source.buckets()));
        target.setOwner(toOwnerDomain.convert(source.owner()));
        target.setContinuationToken(source.continuationToken());
        target.setPrefix(source.prefix());
        ResponseConverter.super.prepare(source, target);
    }

    @Override
    public ListBucketsResult getInstance(ListBucketsResponse source) {
        return new ListBucketsResult();
    }

}
