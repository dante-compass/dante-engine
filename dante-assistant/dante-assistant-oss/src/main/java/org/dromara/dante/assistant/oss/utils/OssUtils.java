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

package org.dromara.dante.assistant.oss.utils;

import cn.hutool.v7.http.html.HtmlUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.dromara.dante.assistant.oss.entity.domain.PolicyDomain;
import org.dromara.dante.assistant.oss.entity.domain.StatementDomain;
import org.dromara.dante.assistant.oss.exception.*;
import org.dromara.dante.core.constant.SymbolConstants;
import org.dromara.dante.core.jackson.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.*;

import java.util.List;
import java.util.Optional;

/**
 * <p>Description: 对象存储工具类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/27 18:53
 */
public class OssUtils {

    private static final Logger log = LoggerFactory.getLogger(OssUtils.class);

    private static final String DEFAULT_RESOURCE_PREFIX = "arn:aws:s3:::";
    private static final List<String> DEFAULT_ACTION_FOR_BUCKET = Lists.newArrayList("s3:GetBucketLocation", "s3:ListBucket", "s3:ListBucketMultipartUploads");
    private static final List<String> DEFAULT_ACTION_FOR_OBJECT = Lists.newArrayList("s3:DeleteObject", "s3:GetObject", "s3:ListMultipartUploadParts", "s3:PutObject", "s3:AbortMultipartUpload");

    public static RuntimeException convertException(Throwable throwable) {
        Throwable cause = throwable.getCause();

        log.error("[Herodotus] |- AWS s3 error: {}", cause.getMessage(), cause);

        if (cause instanceof S3Exception s3Exception) {
            return switch (s3Exception) {
                case AccessDeniedException accessDeniedException ->
                        new OssAccessDeniedException(accessDeniedException.getMessage(), accessDeniedException);
                case BucketAlreadyOwnedByYouException bucketAlreadyOwnedByYouException ->
                        new OssBucketAlreadyOwnedByYouException(bucketAlreadyOwnedByYouException.getMessage(), bucketAlreadyOwnedByYouException);
                case BucketAlreadyExistsException bucketAlreadyExistsException ->
                        new OssBucketAlreadyExistsException(bucketAlreadyExistsException.getMessage(), bucketAlreadyExistsException);
                case InvalidObjectStateException invalidObjectStateException ->
                        new OssInvalidObjectStateException(invalidObjectStateException.getMessage(), invalidObjectStateException);
                case InvalidRequestException invalidRequestException ->
                        new OssInvalidRequestException(invalidRequestException.getMessage(), invalidRequestException);
                case NoSuchBucketException noSuchBucketException ->
                        new OssNoSuchBucketException(noSuchBucketException.getMessage(), noSuchBucketException);
                case NoSuchKeyException noSuchKeyException ->
                        new OssNoSuchKeyException(noSuchKeyException.getMessage(), noSuchKeyException);
                case NoSuchUploadException noSuchUploadException ->
                        new OssNoSuchUploadException(noSuchUploadException.getMessage(), noSuchUploadException);
                default -> new RuntimeException(cause);
            };
        }

        return new RuntimeException(cause);
    }

    public static String unwrapETag(String source) {

        String target = source;

        if (StringUtils.isEmpty(target)) {
            return null;
        }

        if (Strings.CS.contains(target, SymbolConstants.AMPERSAND)) {
            target = HtmlUtil.unescape(source);
        }

        if (Strings.CS.contains(target, SymbolConstants.QUOTE)) {
            target = StringUtils.unwrap(target, SymbolConstants.QUOTE);
        }

        return target;
    }

    private static PolicyDomain getPublicPolicy(String bucketName) {
        StatementDomain bucketStatement = new StatementDomain();
        bucketStatement.setActions(DEFAULT_ACTION_FOR_BUCKET);
        bucketStatement.setResources(getDefaultResource(bucketName, true));

        StatementDomain objectStatement = new StatementDomain();
        objectStatement.setActions(DEFAULT_ACTION_FOR_OBJECT);
        objectStatement.setResources(getDefaultResource(bucketName, false));

        PolicyDomain policy = new PolicyDomain();
        policy.setStatements(Lists.newArrayList(bucketStatement, objectStatement));
        return policy;
    }

    private static List<String> getDefaultResource(String bucketName, boolean isForBucket) {
        String suffix = isForBucket ? "" : "/*";
        return Lists.newArrayList(DEFAULT_RESOURCE_PREFIX + bucketName + suffix);
    }

    /**
     * 生成适配 Minio 的默认 Public Policy
     * <p>
     * 注意：使用 {@link S3AsyncClient#deleteBucketPolicy(DeleteBucketPolicyRequest)} 操作也可以将
     *
     * @param bucketName 存储桶名称
     * @param isPublic   是否为公开的存储桶
     * @return 策略 JSON
     */
    public static String getBucketPolicyJson(String bucketName, boolean isPublic) {
        PolicyDomain policyDomain = isPublic ? getPublicPolicy(bucketName) : new PolicyDomain();
        return JacksonUtils.toJson(policyDomain);
    }

    /**
     * 判断存储桶是否为公开访问存储桶
     * <p>
     * 注意：该方法逻辑基于 AWS S3 V2 sdk 和 minio 验证并实现，其它厂商是否可用尚未验证。
     * <p>
     * 在 Minio 中判断 Bucket 访问权限为 Private 的条件有两个：
     * 1. 调用 <>code>getBucketPolicyResponse</code> 方法抛出异常，即没有设置过任何 policy
     * 2. 返回的 Policy 中 Statement 为空。
     * 除了以上两种情况外，Minio Bucket 存储桶的访问 Access 权限即为 Public
     *
     * @param response 获取存储桶策略响应 {@link GetBucketPolicyResponse}
     * @return true 为 public，false 为 private
     */
    public static boolean isBucketPublic(GetBucketPolicyResponse response) {
        return Optional.ofNullable(response)
                .map(GetBucketPolicyResponse::policy)
                .map(policy -> JacksonUtils.toObject(policy, PolicyDomain.class))
                .map(domain -> ObjectUtils.isNotEmpty(domain) && CollectionUtils.isNotEmpty(domain.getStatements()))
                .orElse(false);
    }
}
