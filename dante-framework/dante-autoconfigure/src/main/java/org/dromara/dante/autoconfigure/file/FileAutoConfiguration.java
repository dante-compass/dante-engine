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

package org.dromara.dante.autoconfigure.file;

import jakarta.annotation.PostConstruct;
import org.dromara.dante.core.support.file.FileTemplate;
import org.dromara.dante.core.support.file.JsonSchemaFileManager;
import org.dromara.dante.core.support.file.OssTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * <p>Description: 服务本地文件统一配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/10/12 15:00
 */
@AutoConfiguration
@EnableConfigurationProperties({FileProperties.class})
public class FileAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(FileAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [File] Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    public FileTemplate fileTemplate(FileProperties fileProperties) {
        DefaultFileTemplate template = new DefaultFileTemplate(fileProperties);
        log.trace("[Herodotus] |- Bean [Default File Template] Configure.");
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    public OssTransformer ossTransformer() {
        DefaultOssTransformer transformer = new DefaultOssTransformer();
        log.trace("[Herodotus] |- Bean [Default File Transformer] Configure.");
        return transformer;
    }

    @Bean
    @ConditionalOnMissingBean
    public JsonSchemaFileManager jsonSchemaFileManager(FileProperties fileProperties, FileTemplate fileTemplate, OssTransformer ossTransformer) {
        DefaultJsonSchemaFileManager manager = new DefaultJsonSchemaFileManager(fileProperties, fileTemplate, ossTransformer);
        log.trace("[Herodotus] |- Bean [Default JsonSchema File Manager] Configure.");
        return manager;
    }
}
