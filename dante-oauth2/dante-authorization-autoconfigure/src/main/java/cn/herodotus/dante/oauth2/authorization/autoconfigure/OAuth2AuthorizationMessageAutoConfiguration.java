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

import cn.herodotus.dante.spring.condition.ConditionalOnArchitecture;
import cn.herodotus.dante.spring.condition.ConditionalOnServletApplication;
import cn.herodotus.dante.spring.enums.Architecture;
import cn.herodotus.dante.message.core.definition.strategy.EnumDictionaryGatherEventManager;
import cn.herodotus.dante.message.core.definition.strategy.RestMappingScanEventManager;
import cn.herodotus.dante.oauth2.authorization.autoconfigure.listener.RemoteAttributeTransmitterSyncListener;
import cn.herodotus.dante.oauth2.authorization.autoconfigure.strategy.DefaultEnumDictionaryGatherEventManager;
import cn.herodotus.dante.oauth2.authorization.autoconfigure.strategy.DefaultRestMappingScanEventManager;
import cn.herodotus.dante.oauth2.authorization.processor.SecurityAttributeAnalyzer;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.bus.ServiceMatcher;
import org.springframework.cloud.bus.StreamBusBridge;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: 资源型服务消息配置  </p>
 * <p>
 * 本配置类配置所有服务均需要用到的消息相关内容配置。
 *
 * @author : gengwei.zheng
 * @date : 2024/8/21 17:54
 */
@AutoConfiguration
public class OAuth2AuthorizationMessageAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthorizationMessageAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [OAuth2 Authorization Message] Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnServletApplication
    public RestMappingScanEventManager servletRequestMappingScanEventManager(SecurityAttributeAnalyzer analyzer) {
        DefaultRestMappingScanEventManager manager = new DefaultRestMappingScanEventManager(analyzer);
        log.trace("[Herodotus] |- Bean [Servlet Request Mapping Scan Manager] Configure.");
        return manager;
    }

    @Bean
    @ConditionalOnMissingBean
    public EnumDictionaryGatherEventManager enumDictionaryGatherEventManager() {
        DefaultEnumDictionaryGatherEventManager manager = new DefaultEnumDictionaryGatherEventManager();
        log.trace("[Herodotus] |- Bean [Enum Dictionary Gather Manager] Configure.");
        return manager;
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(StreamBusBridge.class)
    @ConditionalOnArchitecture(Architecture.DISTRIBUTED)
    @RemoteApplicationEventScan({
            "cn.herodotus.dante.oauth2.authorization.autoconfigure.bus"
    })
    static class BusMessageConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public RemoteAttributeTransmitterSyncListener remoteSecurityMetadataSyncListener(SecurityAttributeAnalyzer analyzer, ServiceMatcher serviceMatcher) {
            RemoteAttributeTransmitterSyncListener listener = new RemoteAttributeTransmitterSyncListener(analyzer, serviceMatcher);
            log.trace("[Herodotus] |- Bean [Security Metadata Refresh Listener] Configure.");
            return listener;
        }
    }
}
