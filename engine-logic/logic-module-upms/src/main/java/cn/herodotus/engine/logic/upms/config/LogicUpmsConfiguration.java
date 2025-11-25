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

package cn.herodotus.engine.logic.upms.config;

import cn.herodotus.engine.assistant.access.config.AssistantAccessConfiguration;
import cn.herodotus.engine.assistant.access.factory.AccessHandlerStrategyFactory;
import cn.herodotus.engine.core.definition.function.EnumDictionaryBuilderCustomizer;
import cn.herodotus.engine.logic.upms.customizer.UpmsEnumDictionaryBuilderCustomizer;
import cn.herodotus.engine.logic.upms.definition.SocialAuthenticationHandler;
import cn.herodotus.engine.logic.upms.handler.DefaultSocialAuthenticationHandler;
import cn.herodotus.engine.logic.upms.service.security.SysSocialUserService;
import cn.herodotus.engine.logic.upms.service.security.SysUserService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * <p>Description: UPMS SDK 模块配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/2/16 19:00
 */
@Configuration(proxyBeanMethods = false)
@EntityScan(basePackages = {
        "cn.herodotus.engine.logic.upms.entity.security",
        "cn.herodotus.engine.logic.upms.entity.hr",
})
@EnableJpaRepositories(basePackages = {
        "cn.herodotus.engine.logic.upms.repository.security",
        "cn.herodotus.engine.logic.upms.repository.hr",
})
@ComponentScan(basePackages = {
        "cn.herodotus.engine.logic.upms.service.assistant",
        "cn.herodotus.engine.logic.upms.service.security",
        "cn.herodotus.engine.logic.upms.service.hr",
})
@Import({AssistantAccessConfiguration.class})
public class LogicUpmsConfiguration {

    private static final Logger log = LoggerFactory.getLogger(LogicUpmsConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- Module [Logic Upms] Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    public SocialAuthenticationHandler socialAuthenticationHandler(SysUserService sysUserService, SysSocialUserService sysSocialUserService, AccessHandlerStrategyFactory accessHandlerStrategyFactory) {
        DefaultSocialAuthenticationHandler defaultSocialAuthenticationHandler = new DefaultSocialAuthenticationHandler(sysUserService, sysSocialUserService, accessHandlerStrategyFactory);
        log.trace("[Herodotus] |- Bean [Default Social Authentication Handler] Configure.");
        return defaultSocialAuthenticationHandler;
    }

    @Bean
    public EnumDictionaryBuilderCustomizer upmsEnumDictionaryBuilderCustomizer() {
        UpmsEnumDictionaryBuilderCustomizer customizer = new UpmsEnumDictionaryBuilderCustomizer();
        log.debug("[Herodotus] |- Strategy [Upms EnumDictionary Builder Customizer] Configure.");
        return customizer;
    }
}
