/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2020-2030 郑庚伟 ZHENGGENGWEI (码匠君), <herodotus@aliyun.com> Licensed under the AGPL License
 *
 * This file is part of Herodotus Stirrup.
 *
 * Herodotus Stirrup is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Herodotus Stirrup is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.herodotus.vip>.
 */

package cn.herodotus.engine.oauth2.authentication.customizer;

import cn.herodotus.engine.oauth2.core.properties.OAuth2AuthenticationProperties;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

/**
 * <p>Description: 授权服务器错误处理自定义器 </p>
 * <p>
 * 注意：与资源服务错误处理自定义器有区别。所以单独进行定义。
 * <p>
 * 主要区别在于，授权服务器需要定义 defaultAuthenticationEntryPointFor，来解决匿名登录跳转到 /login 页面的问题
 * <p>
 * 同时，defaultAuthenticationEntryPointFor 不能与 authenticationEntryPoint、accessDeniedHandler 同时定义。如果定义了后者，defaultAuthenticationEntryPointFor 将不会生效。
 *
 * @author : gengwei.zheng
 * @date : 2025/3/7 22:27
 */
public class OAuth2ExceptionHandlingConfigurerCustomizer implements Customizer<ExceptionHandlingConfigurer<HttpSecurity>> {

    private final OAuth2AuthenticationProperties authenticationProperties;

    public OAuth2ExceptionHandlingConfigurerCustomizer(OAuth2AuthenticationProperties authenticationProperties) {
        this.authenticationProperties = authenticationProperties;
    }

    @Override
    public void customize(ExceptionHandlingConfigurer<HttpSecurity> configurer) {
        configurer
                .defaultAuthenticationEntryPointFor(
                        new LoginUrlAuthenticationEntryPoint(authenticationProperties.getFormLogin().getLoginPageUrl()),
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                );
    }
}

