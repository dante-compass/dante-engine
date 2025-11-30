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

package cn.herodotus.dante.oauth2.authorization.autoconfigure;

import cn.herodotus.dante.oauth2.authorization.autoconfigure.listener.*;
import cn.herodotus.dante.spring.condition.ConditionalOnArchitecture;
import cn.herodotus.dante.spring.enums.Architecture;
import cn.herodotus.dante.logic.upms.annotation.EnableHerodotusLogicUpms;
import cn.herodotus.dante.logic.upms.service.security.SysUserService;
import cn.herodotus.dante.oauth2.authorization.autoconfigure.condition.ConditionalOnUpmsService;
import cn.herodotus.dante.oauth2.authorization.autoconfigure.listener.*;
import cn.herodotus.dante.oauth2.authorization.autoconfigure.processor.AttributeTransmitterDistributeProcessor;
import cn.herodotus.dante.oauth2.authorization.autoconfigure.processor.EnumDictionaryGatherProcessor;
import jakarta.annotation.PostConstruct;
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
        "cn.herodotus.dante.oauth2.authorization.autoconfigure.processor",
})
public class OAuth2UpmsServiceAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OAuth2UpmsServiceAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [OAuth2 Upms Service] Configure.");
    }

    @Configuration(proxyBeanMethods = false)
    public static class UpmsLocalListenerConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public LocalAccountStatusChangedListener localAccountStatusChangedListener(SysUserService sysUserService) {
            LocalAccountStatusChangedListener listener = new LocalAccountStatusChangedListener(sysUserService);
            log.trace("[Herodotus] |- Bean [Local Account Status Changed Listener] Configure.");
            return listener;
        }

        @Bean
        @ConditionalOnMissingBean
        public LocalEnumDictionaryGatherListener localEnumDictionaryGatherListener(EnumDictionaryGatherProcessor enumDictionaryGatherProcessor) {
            LocalEnumDictionaryGatherListener listener = new LocalEnumDictionaryGatherListener(enumDictionaryGatherProcessor);
            log.trace("[Herodotus] |- Bean [Local Enum Dictionary Gather Listener] Configure.");
            return listener;
        }

        @Bean
        @ConditionalOnMissingBean
        public LocalRestMappingGatherListener localRequestMappingGatherListener(AttributeTransmitterDistributeProcessor attributeTransmitterDistributeProcessor) {
            LocalRestMappingGatherListener listener = new LocalRestMappingGatherListener(attributeTransmitterDistributeProcessor);
            log.trace("[Herodotus] |- Bean [Local Request Mapping Gather Listener] Configure.");
            return listener;
        }

        @Bean
        @ConditionalOnMissingBean
        public SysAttributeChangeListener sysAttributeChangeListener(AttributeTransmitterDistributeProcessor attributeTransmitterDistributeProcessor) {
            SysAttributeChangeListener listener = new SysAttributeChangeListener(attributeTransmitterDistributeProcessor);
            log.trace("[Herodotus] |- Bean [SysAttribute Change Listener] Configure.");
            return listener;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnArchitecture(Architecture.DISTRIBUTED)
    public static class UpmsRemoteListenerConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public RemoteAccountStatusChangedListener remoteAccountStatusChangedListener(SysUserService sysUserService) {
            RemoteAccountStatusChangedListener listener = new RemoteAccountStatusChangedListener(sysUserService);
            log.trace("[Herodotus] |- Bean [Remote Account Status Changed Listener] Configure.");
            return listener;
        }

        @Bean
        @ConditionalOnMissingBean
        public RemoteEnumDictionaryGatherListener remoteEnumDictionaryGatherListener(EnumDictionaryGatherProcessor enumDictionaryGatherProcessor) {
            RemoteEnumDictionaryGatherListener listener = new RemoteEnumDictionaryGatherListener(enumDictionaryGatherProcessor);
            log.trace("[Herodotus] |- Bean [Remote Enum Dictionary Gather Listener] Configure.");
            return listener;
        }

        @Bean
        @ConditionalOnMissingBean
        public RemoteRestMappingGatherListener remoteRequestMappingGatherListener(AttributeTransmitterDistributeProcessor attributeTransmitterDistributeProcessor) {
            RemoteRestMappingGatherListener listener = new RemoteRestMappingGatherListener(attributeTransmitterDistributeProcessor);
            log.trace("[Herodotus] |- Bean [Remote Request Mapping Gather Listener] Configure.");
            return listener;
        }
    }
}
