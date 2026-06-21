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

package cn.herodotus.dante.oauth2.authorization.servlet;

import cn.herodotus.dante.oauth2.authorization.converter.HerodotusJwtAuthenticationConverter;
import cn.herodotus.dante.security.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;

/**
 * <p>Description: OAuth2ResourceServerConfigurer 扩展配置</p>
 *
 * @author : gengwei.zheng
 * @date : 2023/8/31 23:27
 */
public class OAuth2ResourceServerConfigurerCustomer implements Customizer<OAuth2ResourceServerConfigurer<HttpSecurity>> {

    private final JwtDecoder jwtDecoder;
    private final OpaqueTokenIntrospector opaqueTokenIntrospector;
    private final BearerTokenResolver bearerTokenResolver;

    public OAuth2ResourceServerConfigurerCustomer(JwtDecoder jwtDecoder, OpaqueTokenIntrospector opaqueTokenIntrospector) {
        this.jwtDecoder = jwtDecoder;
        this.opaqueTokenIntrospector = opaqueTokenIntrospector;
        this.bearerTokenResolver = new DefaultBearerTokenResolver();
    }

    @Override
    public void customize(OAuth2ResourceServerConfigurer<HttpSecurity> configurer) {
        configurer.authenticationManagerResolver(getAuthenticationManagerResolver(this.jwtDecoder, this.opaqueTokenIntrospector));
    }

    private AuthenticationManagerResolver<HttpServletRequest> getAuthenticationManagerResolver(JwtDecoder jwtDecoder, OpaqueTokenIntrospector opaqueTokenIntrospector) {
        return (request) -> isJwt(request) ? getJwtTokenAuthenticationManager(jwtDecoder) : getOpaqueTokenAuthenticationManager(opaqueTokenIntrospector);
    }

    private AuthenticationManager getOpaqueTokenAuthenticationManager(OpaqueTokenIntrospector opaqueTokenIntrospector) {
        OpaqueTokenAuthenticationProvider opaqueTokenAuthenticationProvider = new OpaqueTokenAuthenticationProvider(opaqueTokenIntrospector);
        return new ProviderManager(opaqueTokenAuthenticationProvider);
    }

    private AuthenticationManager getJwtTokenAuthenticationManager(JwtDecoder jwtDecoder) {
        JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
        jwtAuthenticationProvider.setJwtAuthenticationConverter(new HerodotusJwtAuthenticationConverter());
        return new ProviderManager(jwtAuthenticationProvider);
    }

    private boolean isJwt(HttpServletRequest request) {
        String accessToken = bearerTokenResolver.resolve(request);
        return SecurityUtils.isJwtToken(accessToken);
    }
}
