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

package org.dromara.dante.oauth2.authentication.autoconfigure;

import jakarta.annotation.PostConstruct;
import org.dromara.dante.oauth2.authentication.autoconfigure.listener.LocalDisableAuthenticationListener;
import org.dromara.dante.oauth2.authentication.autoconfigure.listener.LocalEnableAuthenticationListener;
import org.dromara.dante.oauth2.authentication.autoconfigure.listener.RemoteDisableAuthenticationListener;
import org.dromara.dante.oauth2.authentication.autoconfigure.listener.RemoteEnableAuthenticationListener;
import org.dromara.dante.oauth2.authentication.autoconfigure.strategy.DefaultAccountStatusChangeEventManager;
import org.dromara.dante.oauth2.authentication.autoconfigure.strategy.DefaultOAuth2DeviceVerificationSuccessEventManager;
import org.dromara.dante.oauth2.authentication.autoconfigure.strategy.DefaultOidcClientRegistrationSuccessEventManager;
import org.dromara.dante.oauth2.commons.strategy.AccountStatusChangedEventManager;
import org.dromara.dante.oauth2.commons.strategy.OAuth2DeviceVerificationSuccessEventManager;
import org.dromara.dante.oauth2.commons.strategy.OidcClientRegistrationSuccessEventManager;
import org.dromara.dante.persistence.commons.definition.EnhanceAuthenticationManager;
import org.dromara.dante.spring.condition.ConditionalOnArchitecture;
import org.dromara.dante.spring.enums.Architecture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: 认证服务器 OAuth2 消息配置 </p>
 * <p>
 * 本配置类中，仅配置认证服务器 UAA 所需要的相关信息内容
 *
 * @author : gengwei.zheng
 * @date : 2024/8/21 17:54
 */
@AutoConfiguration
public class UaaServiceMessageAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(UaaServiceMessageAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- Auto [Uaa Service Message] Configure.");
    }

    @Bean
    public AccountStatusChangedEventManager accountStatusChangedEventManager() {
        DefaultAccountStatusChangeEventManager manager = new DefaultAccountStatusChangeEventManager();
        log.trace("[Herodotus] |- Bean [Herodotus Account Status Event Manager] Configure.");
        return manager;
    }

    @Bean
    public OAuth2DeviceVerificationSuccessEventManager oauth2DeviceVerificationSuccessEventManager() {
        DefaultOAuth2DeviceVerificationSuccessEventManager manager = new DefaultOAuth2DeviceVerificationSuccessEventManager();
        log.trace("[Herodotus] |- Bean [OAuth2 Device Verification Success Event Manager] Configure.");
        return manager;
    }

    @Bean
    public OidcClientRegistrationSuccessEventManager oidcClientRegistrationSuccessEventManager() {
        DefaultOidcClientRegistrationSuccessEventManager manager = new DefaultOidcClientRegistrationSuccessEventManager();
        log.trace("[Herodotus] |- Bean [Oidc Client Registration Success Event Manager] Configure.");
        return manager;
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnArchitecture(Architecture.MONOLITH)
    static class LocalAuthenticationConfiguration {

        @Bean
        public LocalDisableAuthenticationListener localDisableAuthenticationListener(EnhanceAuthenticationManager enhanceAuthenticationManager) {
            LocalDisableAuthenticationListener listener = new LocalDisableAuthenticationListener(enhanceAuthenticationManager);
            log.trace("[Herodotus] |- Bean [Local Disable Authentication Listener] Configure.");
            return listener;
        }

        @Bean
        public LocalEnableAuthenticationListener localEnableAuthenticationListener(EnhanceAuthenticationManager enhanceAuthenticationManager) {
            LocalEnableAuthenticationListener listener = new LocalEnableAuthenticationListener(enhanceAuthenticationManager);
            log.trace("[Herodotus] |- Bean [Local Enable Authentication Listener] Configure.");
            return listener;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnArchitecture(Architecture.DISTRIBUTED)
    static class RemoteAuthenticationConfiguration {

        @Bean
        public RemoteDisableAuthenticationListener remoteDisableAuthenticationListener(EnhanceAuthenticationManager enhanceAuthenticationManager) {
            RemoteDisableAuthenticationListener listener = new RemoteDisableAuthenticationListener(enhanceAuthenticationManager);
            log.trace("[Herodotus] |- Bean [Remote Disable Authentication Listener] Configure.");
            return listener;
        }

        @Bean
        public RemoteEnableAuthenticationListener remoteEnableAuthenticationListener(EnhanceAuthenticationManager enhanceAuthenticationManager) {
            RemoteEnableAuthenticationListener listener = new RemoteEnableAuthenticationListener(enhanceAuthenticationManager);
            log.trace("[Herodotus] |- Bean [Remote Enable Authentication Listener] Configure.");
            return listener;
        }
    }
}
