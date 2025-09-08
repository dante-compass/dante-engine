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

import cn.herodotus.engine.core.autoconfigure.oauth2.OAuth2AuthorizationProperties;
import cn.herodotus.engine.core.autoconfigure.oauth2.servlet.ServletOAuth2ResourceMatcherConfigurer;
import cn.herodotus.engine.web.core.servlet.template.ThymeleafTemplateHandler;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

/**
 * <p>Description: 资源服务器通用 Bean 配置器 </p>
 * <p>
 * 重新嵌套一层 Bean，将资源服务器中通用的 Bean 进行封装。减少使用时重复注入相同的内容，方便使用。
 *
 * @author : gengwei.zheng
 * @date : 2025/3/8 0:50
 */
public class ServletOAuth2AuthorizationConfigurerManager {

    private final OAuth2ResourceServerConfigurerCustomer oauth2ResourceServerConfigurerCustomer;
    private final OAuth2SessionManagementConfigurerCustomer oauth2SessionManagementConfigurerCustomer;
    private final OAuth2AuthorizeHttpRequestsConfigurerCustomer oauth2AuthorizeHttpRequestsConfigurerCustomer;
    private final OAuth2ExceptionHandlingConfigurerCustomizer oauth2ExceptionHandlingConfigurerCustomizer;

    public ServletOAuth2AuthorizationConfigurerManager(
            ThymeleafTemplateHandler thymeleafTemplateHandler,
            OAuth2AuthorizationProperties oauth2AuthorizationProperties,
            JwtDecoder jwtDecoder,
            OpaqueTokenIntrospector opaqueTokenIntrospector,
            OAuth2SessionManagementConfigurerCustomer oauth2SessionManagementConfigurerCustomer,
            ServletOAuth2ResourceMatcherConfigurer servletOAuth2ResourceMatcherConfigurer,
            ServletSecurityAuthorizationManager servletSecurityAuthorizationManager) {
        this.oauth2ResourceServerConfigurerCustomer = new OAuth2ResourceServerConfigurerCustomer(oauth2AuthorizationProperties, jwtDecoder, opaqueTokenIntrospector);
        this.oauth2SessionManagementConfigurerCustomer = oauth2SessionManagementConfigurerCustomer;
        this.oauth2AuthorizeHttpRequestsConfigurerCustomer = new OAuth2AuthorizeHttpRequestsConfigurerCustomer(servletOAuth2ResourceMatcherConfigurer, servletSecurityAuthorizationManager);
        this.oauth2ExceptionHandlingConfigurerCustomizer = new OAuth2ExceptionHandlingConfigurerCustomizer(thymeleafTemplateHandler);
    }

    public OAuth2ResourceServerConfigurerCustomer getOAuth2ResourceServerConfigurerCustomer() {
        return oauth2ResourceServerConfigurerCustomer;
    }

    public OAuth2SessionManagementConfigurerCustomer getOAuth2SessionManagementConfigurerCustomer() {
        return oauth2SessionManagementConfigurerCustomer;
    }

    public OAuth2AuthorizeHttpRequestsConfigurerCustomer getOAuth2AuthorizeHttpRequestsConfigurerCustomer() {
        return oauth2AuthorizeHttpRequestsConfigurerCustomer;
    }

    public OAuth2ExceptionHandlingConfigurerCustomizer getOAuth2ExceptionHandlingConfigurerCustomizer() {
        return oauth2ExceptionHandlingConfigurerCustomizer;
    }
}
