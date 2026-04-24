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

package org.dromara.dante.oauth2.authorization.autoconfigure;

import jakarta.annotation.PostConstruct;
import org.dromara.dante.logic.upms.annotation.EnableHerodotusLogicUpms;
import org.dromara.dante.logic.upms.service.security.SysUserService;
import org.dromara.dante.oauth2.authorization.autoconfigure.condition.ConditionalOnUpmsService;
import org.dromara.dante.oauth2.authorization.autoconfigure.listener.*;
import org.dromara.dante.oauth2.authorization.autoconfigure.processor.EnumDictionaryCollectProcessor;
import org.dromara.dante.oauth2.authorization.autoconfigure.processor.SecurityAttributeDistributionProcessor;
import org.dromara.dante.spring.condition.ConditionalOnArchitecture;
import org.dromara.dante.spring.enums.Architecture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: Upms 服务消息配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/10/27 23:46
 */
@AutoConfiguration
@ConditionalOnUpmsService
@EnableHerodotusLogicUpms
@ComponentScan(basePackages = {
        "org.dromara.dante.oauth2.authorization.autoconfigure.processor",
})
public class UpmsServiceMessageAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(UpmsServiceMessageAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [OAuth2 Upms Service] Configure.");
    }

    @Configuration(proxyBeanMethods = false)
    public static class UpmsLocalListenerConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public LocalAccountStatusChangeListener localAccountStatusChangeListener(SysUserService sysUserService) {
            LocalAccountStatusChangeListener listener = new LocalAccountStatusChangeListener(sysUserService);
            log.trace("[Herodotus] |- Bean [Local Account Status Change Listener] Configure.");
            return listener;
        }

        @Bean
        @ConditionalOnMissingBean
        public LocalEnumDictionaryCollectListener localEnumDictionaryCollectListener(EnumDictionaryCollectProcessor enumDictionaryCollectProcessor) {
            LocalEnumDictionaryCollectListener listener = new LocalEnumDictionaryCollectListener(enumDictionaryCollectProcessor);
            log.trace("[Herodotus] |- Bean [Local Enum Dictionary Collect Listener] Configure.");
            return listener;
        }

        @Bean
        @ConditionalOnMissingBean
        public LocalRestMappingCollectListener localRestMappingCollectListener(SecurityAttributeDistributionProcessor securityAttributeDistributionProcessor) {
            LocalRestMappingCollectListener listener = new LocalRestMappingCollectListener(securityAttributeDistributionProcessor);
            log.trace("[Herodotus] |- Bean [Local Request Mapping Collect Listener] Configure.");
            return listener;
        }

        @Bean
        @ConditionalOnMissingBean
        public SysAttributeChangeListener sysAttributeChangeListener(SecurityAttributeDistributionProcessor securityAttributeDistributionProcessor) {
            SysAttributeChangeListener listener = new SysAttributeChangeListener(securityAttributeDistributionProcessor);
            log.trace("[Herodotus] |- Bean [SysAttribute Change Listener] Configure.");
            return listener;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnArchitecture(Architecture.DISTRIBUTED)
    public static class UpmsRemoteListenerConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public RemoteAccountStatusChangeListener remoteAccountStatusChangeListener(SysUserService sysUserService) {
            RemoteAccountStatusChangeListener listener = new RemoteAccountStatusChangeListener(sysUserService);
            log.trace("[Herodotus] |- Bean [Remote Account Status Change Listener] Configure.");
            return listener;
        }

        @Bean
        @ConditionalOnMissingBean
        public RemoteEnumDictionaryCollectListener remoteEnumDictionaryCollectListener(EnumDictionaryCollectProcessor enumDictionaryCollectProcessor) {
            RemoteEnumDictionaryCollectListener listener = new RemoteEnumDictionaryCollectListener(enumDictionaryCollectProcessor);
            log.trace("[Herodotus] |- Bean [Remote Enum Dictionary Collect Listener] Configure.");
            return listener;
        }

        @Bean
        @ConditionalOnMissingBean
        public RemoteRestMappingGatherListener remoteRestMappingGatherListener(SecurityAttributeDistributionProcessor securityAttributeDistributionProcessor) {
            RemoteRestMappingGatherListener listener = new RemoteRestMappingGatherListener(securityAttributeDistributionProcessor);
            log.trace("[Herodotus] |- Bean [Remote Request Mapping Collect Listener] Configure.");
            return listener;
        }
    }
}
