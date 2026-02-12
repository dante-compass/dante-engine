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

package org.dromara.dante.assistant.oss.config;

import jakarta.annotation.PostConstruct;
import org.dromara.dante.assistant.oss.customizer.OssEnumDictionaryBuilderCustomizer;
import org.dromara.dante.assistant.oss.customizer.OssErrorCodeMapperBuilderCustomizer;
import org.dromara.dante.assistant.oss.pool.AwsConfigurer;
import org.dromara.dante.assistant.oss.pool.S3AsyncClientObjectPool;
import org.dromara.dante.assistant.oss.pool.S3PresignerObjectPool;
import org.dromara.dante.assistant.oss.properties.OssProperties;
import org.dromara.dante.core.function.EnumDictionaryBuilderCustomizer;
import org.dromara.dante.core.function.ErrorCodeMapperBuilderCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: OSS 业务模块配置 </p>
 * <p>
 * 在 Herodotus Cloud Reactive 版本中，将不再使用原有 Dialect 模式适配所有 OSS，工作量大实现繁琐，而改用统一使用 AWS S3 V2 版本进行实现。
 * AWS S3 V2 版本除了 API 变化，对于OSS厂商所谓兼容 S3 协议的部分操作没有变化。比如当前代码，实现 Minio 的基本操作是完全可以的。
 * <p>
 * 理想情况下，未来各个厂商的新版本会逐步适配或像 AWS S3 V2。即使不改动，现在代码也会兼容。
 * <p>
 * 自2024年7月31日起， AWS SDK for Java 1.x已进入维护模式，并将于2025年12月31日end-of-support上线。我们建议您迁移到AWS SDK for Java 2.x以继续接收新功能、可用性改进和安全更新。
 *
 * @author : gengwei.zheng
 * @date : 2024/7/21 9:55
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(OssProperties.class)
public class AssistantOssConfiguration {

    private static final Logger log = LoggerFactory.getLogger(AssistantOssConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- Module [Assistant Oss] Configure.");
    }

    @Bean
    public EnumDictionaryBuilderCustomizer ossEnumDictionaryBuilder() {
        OssEnumDictionaryBuilderCustomizer customizer = new OssEnumDictionaryBuilderCustomizer();
        log.debug("[Herodotus] |- Strategy [OSS EnumDictionary Builder Customizer] Configure.");
        return customizer;
    }

    @Bean
    public ErrorCodeMapperBuilderCustomizer ossErrorCodeMapperBuilderCustomizer() {
        OssErrorCodeMapperBuilderCustomizer customizer = new OssErrorCodeMapperBuilderCustomizer();
        log.debug("[Herodotus] |- Strategy [Oss ErrorCodeMapper Builder Customizer] Configure.");
        return customizer;
    }

    @Bean
    @ConditionalOnMissingBean
    public AwsConfigurer awsConfigurer(OssProperties ossProperties) {
        AwsConfigurer configurer = new AwsConfigurer(ossProperties);
        log.trace("[Herodotus] |- Bean [Aws Configurer] Configure.");
        return configurer;
    }

    @Bean
    @ConditionalOnMissingBean
    public S3AsyncClientObjectPool s3AsyncClientObjectPool(AwsConfigurer awsConfigurer) {
        S3AsyncClientObjectPool pool = new S3AsyncClientObjectPool(awsConfigurer);
        log.trace("[Herodotus] |- Bean [Aws S3 Async Client Pool] Configure.");
        return pool;
    }

    @Bean
    @ConditionalOnMissingBean
    public S3PresignerObjectPool s3PresignerObjectPool(AwsConfigurer awsConfigurer) {
        S3PresignerObjectPool pool = new S3PresignerObjectPool(awsConfigurer);
        log.trace("[Herodotus] |- Bean [Aws S3 Presigner Pool] Configure.");
        return pool;
    }

    @Configuration(proxyBeanMethods = false)
    @ComponentScan(basePackages = {
            "org.dromara.dante.assistant.oss.service.base",
            "org.dromara.dante.assistant.oss.service.manager"
    })
    static class AwsServiceConfiguration {

    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ComponentScan(basePackages = {
            "org.dromara.dante.assistant.oss.service.servlet"
    })
    static class ServletOssConfiguration {

    }
}
