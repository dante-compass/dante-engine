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

package cn.herodotus.dante.assistant.access.config;

import cn.herodotus.dante.assistant.access.condition.ConditionalOnAnduinEnabled;
import cn.herodotus.dante.assistant.access.processor.AnduinAccessHandler;
import cn.herodotus.dante.assistant.access.processor.AnduinRequestProcessor;
import cn.herodotus.dante.assistant.access.properties.AnduinProperties;
import cn.herodotus.dante.assistant.access.stamp.AnduinStateStampManager;
import cn.herodotus.dante.security.enums.AccountCategory;
import jakarta.annotation.PostConstruct;
import org.dromara.anduin.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

/**
 * <p>Description: Anduin配置 </p>
 * <p>
 * 仅在存在herodotus.platform.social.justauth.configs配置的情况下才注入
 *
 * @author : gengwei.zheng
 * @date : 2021/5/22 11:25
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnAnduinEnabled
@EnableConfigurationProperties(AnduinProperties.class)
public class AssistantAccessAnduinConfiguration {

    private static final Logger log = LoggerFactory.getLogger(AssistantAccessAnduinConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- Module [Assistant Access Anduin] Configure.");
    }

    @Bean
    public AnduinStateStampManager anduinStateStampManager(AnduinProperties anduinProperties) {
        AnduinStateStampManager anduinStateStampManager = new AnduinStateStampManager(anduinProperties);
        log.trace("[Herodotus] |- Bean [Anduin State Redis Cache] Configure.");
        return anduinStateStampManager;
    }

    @Bean
    public AnduinRequestProcessor anduinRequestProcessor(AnduinProperties anduinProperties, AnduinStateStampManager anduinStateStampManager) {
        AnduinRequestProcessor processor = new AnduinRequestProcessor(anduinProperties, anduinStateStampManager);
        log.trace("[Herodotus] |- Bean [Anduin Request Processor] Configure.");
        return processor;
    }


    @Bean(AccountCategory.JUST_AUTH_HANDLER)
    @ConditionalOnBean(AnduinRequestProcessor.class)
    public AnduinAccessHandler anduinAccessHandler(AnduinRequestProcessor anduinRequestProcessor) {
        AnduinAccessHandler anduinAccessHandler = new AnduinAccessHandler(anduinRequestProcessor);
        log.trace("[Herodotus] |- Bean [Anduin Access Handler] Configure.");
        return anduinAccessHandler;
    }

    @Bean
    @ConditionalOnBean(ObjectMapper.class)
    public JsonUtils jsonUtils(ObjectMapper objectMapper) {
        JsonUtils.setObjectMapper(objectMapper);
        return JsonUtils.getInstance();
    }
}
