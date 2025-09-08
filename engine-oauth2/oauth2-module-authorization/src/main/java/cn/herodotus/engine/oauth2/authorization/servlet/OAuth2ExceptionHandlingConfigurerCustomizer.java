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

package cn.herodotus.engine.oauth2.authorization.servlet;

import cn.herodotus.engine.web.core.servlet.template.ThymeleafTemplateHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;

/**
 * <p>Description: 授权服务器错误处理自定义器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/3/7 22:27
 */
public class OAuth2ExceptionHandlingConfigurerCustomizer implements Customizer<ExceptionHandlingConfigurer<HttpSecurity>> {

    private final ThymeleafTemplateHandler templateHandler;

    public OAuth2ExceptionHandlingConfigurerCustomizer(ThymeleafTemplateHandler templateHandler) {
        this.templateHandler = templateHandler;
    }

    @Override
    public void customize(ExceptionHandlingConfigurer<HttpSecurity> configurer) {
        configurer
                .authenticationEntryPoint(new HerodotusAuthenticationEntryPoint(templateHandler))
                .accessDeniedHandler(new HerodotusAccessDeniedHandler(templateHandler));
    }
}

