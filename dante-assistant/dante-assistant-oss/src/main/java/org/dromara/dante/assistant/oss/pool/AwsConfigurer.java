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

package org.dromara.dante.assistant.oss.pool;

import org.apache.commons.lang3.StringUtils;
import org.dromara.dante.assistant.oss.constant.OssConstants;
import org.dromara.dante.assistant.oss.properties.OssProperties;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

/**
 * <p>Description: Aws 配置器 </p>
 * <p>
 * 说明：
 * AWS CRT基于的 S3 客户端（建立在AWS 公共运行时 (CRT) 之上）是替代的 S3 异步客户端。它通过自动使用 Amazon S3 的分段API上传和字节范围提取功能，在亚马逊简单存储服务 (Amazon S3) Simple Ser vice (Amazon S3) 之间传输对象，从而提高了性能和可靠性。
 * AWS CRT基于的 S3 客户端可提高网络故障时的传输可靠性。通过重试文件传输中失败的各个分段，而无需从头开始重新启动传输，从而提高可靠性。
 * 此外， AWS CRT基于的 S3 客户端还提供了增强的连接池和域名系统 (DNS) 负载平衡，这也提高了吞吐量。
 * 您可以使用 AWS CRT基于的 S3 客户端代替SDK的标准 S3 异步客户端，并立即利用其提高的吞吐量。
 * <p>
 * AWS CRT基于的 S3 客户端是 S 3 AsyncClient 接口的实现，用于使用 Amazon S3 服务。它是基于 Java 的 S3AsyncClient 接口实现的替代方案，具有多种优势。
 * AWS CRT基于的HTTP客户端是SdkAsyncHttpClient接口的实现，用于一般HTTP通信。它是 Netty SdkAsyncHttpClient 接口实现的替代方案，具有多种优势。
 * 尽管两个组件都使用AWS 公共运行时中的库，但 AWS CRT基于的 S3 客户端使用 aws-c-s3 库并支持 S3 分段上传API功能。由于 AWS CRT基于的HTTP客户端仅供一般用途，因此它不支持 S3 分段上传API功能。
 *
 * @author : gengwei.zheng
 * @date : 2024/7/21 19:08
 */
public record AwsConfigurer(OssProperties ossProperties) {

    /**
     * 创建 AWS 认证信息
     *
     * @return {@link StaticCredentialsProvider}
     */
    private StaticCredentialsProvider createCredentialsProvider() {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(ossProperties.getAccessKey(), ossProperties.getSecretKey()));
    }

    /**
     * 创建 AWS基于 CRT 的 S3 异步客户端
     *
     * @return {@link S3AsyncClient}
     */
    public S3AsyncClient createS3AsyncClient() {
        return S3AsyncClient.crtBuilder()
                .credentialsProvider(createCredentialsProvider())
                .endpointOverride(URI.create(ossProperties.getEndpoint()))
                .region(of())
                .targetThroughputInGbps(20.0)
                .minimumPartSizeInBytes(OssConstants.MIN_PART_SIZE)
                .checksumValidationEnabled(false)
                .build();
    }

    /**
     * AWS基于 CRT 的 S3 AsyncClient 实例用作 S3 传输管理器的底层客户端
     * <p>
     * 预签名 URL 提供对私有 S3 对象的临时访问权限，无需用户拥有 AWS 凭证或权限。
     *
     * @return {@link S3Presigner}
     */
    public S3Presigner createPresigner() {
        // 创建 S3 配置对象
        S3Configuration config = S3Configuration.builder()
                .chunkedEncodingEnabled(false)
                .pathStyleAccessEnabled(false)
                .build();

        return S3Presigner.builder()
                .region(of())
                .endpointOverride(URI.create(ossProperties.getEndpoint()))
                .credentialsProvider(createCredentialsProvider())
                .serviceConfiguration(config)
                .build();
    }

    /**
     * 根据传入的 region 参数返回相应的 AWS 区域
     * 如果 region 参数非空，使用 Region.of 方法创建并返回对应的 AWS 区域对象
     * 如果 region 参数为空，返回一个默认的 AWS 区域（例如，us-east-1），作为广泛支持的区域
     *
     * @return 对应的 AWS 区域对象，或者默认的广泛支持的区域（us-east-1）
     */
    public Region of() {
        // AWS 区域字符串
        String region = ossProperties.getRegion();
        // 如果 region 参数非空，使用 Region.of 方法创建对应的 AWS 区域对象，否则返回默认区域
        return StringUtils.isNotEmpty(region) ? Region.of(region) : Region.US_EAST_1;
    }
}
