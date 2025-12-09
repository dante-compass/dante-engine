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

package org.dromara.dante.oauth2.persistence.sas.jpa.config;

import jakarta.annotation.PostConstruct;
import org.dromara.dante.oauth2.persistence.sas.jpa.service.HerodotusAuthorizationConsentService;
import org.dromara.dante.oauth2.persistence.sas.jpa.service.HerodotusAuthorizationService;
import org.dromara.dante.oauth2.persistence.sas.jpa.service.HerodotusRegisteredClientService;
import org.dromara.dante.oauth2.persistence.sas.jpa.storage.JpaOAuth2AuthorizationConsentService;
import org.dromara.dante.oauth2.persistence.sas.jpa.storage.JpaOAuth2AuthorizationService;
import org.dromara.dante.oauth2.persistence.sas.jpa.storage.JpaRegisteredClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * <p>Description: OAuth2 Manager 模块配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/3/1 18:25
 */
@Configuration(proxyBeanMethods = false)
@EntityScan(basePackages = {
        "org.dromara.dante.oauth2.persistence.sas.jpa.entity"
})
@EnableJpaRepositories(basePackages = {
        "org.dromara.dante.oauth2.persistence.sas.jpa.repository",
})
@ComponentScan(basePackages = {
        "org.dromara.dante.oauth2.persistence.sas.jpa.service",
})
public class OAuth2PersistenceSasJpaConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OAuth2PersistenceSasJpaConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- Module [OAuth2 Data JPA] Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    public RegisteredClientRepository registeredClientRepository(HerodotusRegisteredClientService herodotusRegisteredClientService, PasswordEncoder passwordEncoder) {
        JpaRegisteredClientRepository jpaRegisteredClientRepository = new JpaRegisteredClientRepository(herodotusRegisteredClientService, passwordEncoder);
        log.trace("[Herodotus] |- Bean [Jpa Registered Client Repository] Configure.");
        return jpaRegisteredClientRepository;
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2AuthorizationService authorizationService(HerodotusAuthorizationService herodotusAuthorizationService, RegisteredClientRepository registeredClientRepository) {
        JpaOAuth2AuthorizationService jpaOAuth2AuthorizationService = new JpaOAuth2AuthorizationService(herodotusAuthorizationService, registeredClientRepository);
        log.trace("[Herodotus] |- Bean [Jpa OAuth2 Authorization Service] Configure.");
        return jpaOAuth2AuthorizationService;
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2AuthorizationConsentService authorizationConsentService(HerodotusAuthorizationConsentService herodotusAuthorizationConsentService, RegisteredClientRepository registeredClientRepository) {
        JpaOAuth2AuthorizationConsentService jpaOAuth2AuthorizationConsentService = new JpaOAuth2AuthorizationConsentService(herodotusAuthorizationConsentService, registeredClientRepository);
        log.trace("[Herodotus] |- Bean [Jpa OAuth2 Authorization Consent Service] Configure.");
        return jpaOAuth2AuthorizationConsentService;
    }
}
