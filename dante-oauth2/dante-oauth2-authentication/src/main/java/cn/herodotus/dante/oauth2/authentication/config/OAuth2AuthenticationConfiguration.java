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

package cn.herodotus.dante.oauth2.authentication.config;

import cn.herodotus.dante.core.support.crypto.DigitalEnvelopeProcessor;
import cn.herodotus.dante.web.servlet.template.ThymeleafTemplateHandler;
import cn.herodotus.dante.oauth2.authentication.configurer.OAuth2AuthenticationConfigurerManager;
import cn.herodotus.dante.oauth2.authentication.customizer.HerodotusJwtTokenCustomizer;
import cn.herodotus.dante.oauth2.authentication.customizer.HerodotusOpaqueTokenCustomizer;
import cn.herodotus.dante.oauth2.authentication.response.DefaultOAuth2AuthenticationEventPublisher;
import cn.herodotus.dante.oauth2.commons.properties.OAuth2AuthenticationProperties;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * <p>Description: OAuth2 认证基础模块配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/5/13 15:40
 */
@Configuration(proxyBeanMethods = false)
public class OAuth2AuthenticationConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthenticationConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- Module [OAuth2 Authentication] Configure.");
    }

    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher(ApplicationContext applicationContext) {
        DefaultOAuth2AuthenticationEventPublisher publisher = new DefaultOAuth2AuthenticationEventPublisher(applicationContext);
        // 设置默认的错误 Event 类型。在遇到默认字典中没有的错误时，默认抛出。
        publisher.setDefaultAuthenticationFailureEvent(AuthenticationFailureBadCredentialsEvent.class);
        log.trace("[Herodotus] |- Bean [Authentication Event Publisher] Configure.");
        return publisher;
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        HerodotusJwtTokenCustomizer customizer = new HerodotusJwtTokenCustomizer();
        log.trace("[Herodotus] |- Bean [OAuth2 Jwt Token Customizer] Configure.");
        return customizer;
    }

    @Bean
    public OAuth2TokenCustomizer<OAuth2TokenClaimsContext> opaqueTokenCustomizer() {
        HerodotusOpaqueTokenCustomizer customizer = new HerodotusOpaqueTokenCustomizer();
        log.trace("[Herodotus] |- Bean [OAuth2 Opaque Token Customizer] Configure.");
        return customizer;
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2AuthenticationConfigurerManager oauth2AuthenticationConfigurerManager(
            ThymeleafTemplateHandler thymeleafTemplateHandler,
            DigitalEnvelopeProcessor digitalEnvelopeProcessor,
            OAuth2AuthenticationProperties authenticationProperties) {

        OAuth2AuthenticationConfigurerManager configurer = new OAuth2AuthenticationConfigurerManager(
                thymeleafTemplateHandler,
                digitalEnvelopeProcessor,
                authenticationProperties);
        log.trace("[Herodotus] |- Bean [Servlet OAuth2 Authorization Server Configurer] Configure.");
        return configurer;
    }
}
