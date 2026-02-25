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

package org.dromara.dante.servlet.container.autoconfigure.oauth2;

import jakarta.annotation.PostConstruct;
import org.dromara.dante.security.condition.ConditionalOnTokenFormat;
import org.dromara.dante.security.condition.TokenFormat;
import org.dromara.dante.security.definition.BearerTokenResolver;
import org.dromara.dante.servlet.container.autoconfigure.context.ServletServiceContextAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

@AutoConfiguration(after = ServletServiceContextAutoConfiguration.class)
@ConditionalOnClass(BearerTokenAuthenticationToken.class)
@Import({
        ServletRestControllerAdvice.class
})
public class ServletOAuth2AuthorizationAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ServletOAuth2AuthorizationAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [Servlet OAuth2 Authorization] Configure.");
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(OpaqueTokenIntrospector.class)
    @ConditionalOnTokenFormat(TokenFormat.OPAQUE)
    static class OAuth2OpaqueTokenConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public OpaqueTokenIntrospector herodotusServletOpaqueTokenIntrospector(OAuth2ResourceServerProperties resourceServerProperties) {
            HerodotusServletOpaqueTokenIntrospector introspector = new HerodotusServletOpaqueTokenIntrospector(resourceServerProperties);
            log.trace("[Herodotus] |- Bean [Herodotus Servlet Opaque Token Introspector] Configure.");
            return introspector;
        }

        @Bean
        @ConditionalOnMissingBean
        public BearerTokenResolver opaqueBearerTokenResolver(OpaqueTokenIntrospector OpaqueTokenIntrospector) {
            HerodotusServletOpaqueTokenResolver resolver = new HerodotusServletOpaqueTokenResolver(OpaqueTokenIntrospector);
            log.trace("[Herodotus] |- Bean [Herodotus Servlet Opaque Token Resolver] Configure.");
            return resolver;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(JwtDecoder.class)
    @ConditionalOnTokenFormat(TokenFormat.JWT)
    static class OAuth2JwtTokenConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public BearerTokenResolver jwtBearerTokenResolver(JwtDecoder jwtDecoder) {
            HerodotusServletJwtTokenResolver resolver = new HerodotusServletJwtTokenResolver(jwtDecoder);
            log.trace("[Herodotus] |- Bean [Herodotus Servlet JWT Token Resolver] Configure.");
            return resolver;
        }
    }

    @Bean
    @ConditionalOnClass(AuditorAware.class)
    @ConditionalOnMissingBean
    public AuditorAware<String> auditorAware() {
        ServletSecurityAuditorAware aware = new ServletSecurityAuditorAware();
        log.trace("[Herodotus] |- Bean [Servlet Security Auditor Aware] Configure.");
        return aware;
    }
}
