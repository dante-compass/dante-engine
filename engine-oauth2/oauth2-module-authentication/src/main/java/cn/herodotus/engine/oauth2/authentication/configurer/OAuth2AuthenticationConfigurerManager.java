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

package cn.herodotus.engine.oauth2.authentication.configurer;

import cn.herodotus.engine.oauth2.authentication.customizer.OAuth2FormLoginConfigurerCustomizer;
import cn.herodotus.engine.oauth2.authentication.response.OAuth2AccessTokenResponseHandler;
import cn.herodotus.engine.oauth2.authentication.response.OAuth2AuthenticationFailureHandler;
import cn.herodotus.engine.oauth2.core.properties.OAuth2AuthenticationProperties;
import cn.herodotus.engine.web.core.servlet.template.ThymeleafTemplateHandler;
import cn.herodotus.engine.web.servlet.crypto.HttpCryptoProcessor;

/**
 * <p>Description: 授权服务器通用 Bean 配置器 </p>
 * <p>
 * 重新嵌套一层 Bean，将授权服务器中通用的 Bean 进行封装。减少使用时重复注入相同的内容，方便使用。
 *
 * @author : gengwei.zheng
 * @date : 2025/3/8 0:25
 */
public class OAuth2AuthenticationConfigurerManager {

    private final HttpCryptoProcessor httpCryptoProcessor;

    private final OAuth2AuthenticationProperties oauth2AuthenticationProperties;
    private final OAuth2FormLoginConfigurerCustomizer oauth2FormLoginConfigurerCustomizer;
    private final OAuth2AccessTokenResponseHandler oauth2AccessTokenResponseHandler;
    private final OAuth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;

    public OAuth2AuthenticationConfigurerManager(
            ThymeleafTemplateHandler thymeleafTemplateHandler,
            HttpCryptoProcessor httpCryptoProcessor,
            OAuth2AuthenticationProperties oauth2AuthenticationProperties) {
        this.httpCryptoProcessor = httpCryptoProcessor;
        this.oauth2AuthenticationProperties = oauth2AuthenticationProperties;
        this.oauth2FormLoginConfigurerCustomizer = new OAuth2FormLoginConfigurerCustomizer(oauth2AuthenticationProperties);
        this.oauth2AccessTokenResponseHandler = new OAuth2AccessTokenResponseHandler(httpCryptoProcessor);
        this.oauth2AuthenticationFailureHandler = new OAuth2AuthenticationFailureHandler(thymeleafTemplateHandler);
    }

    public HttpCryptoProcessor getHttpCryptoProcessor() {
        return httpCryptoProcessor;
    }

    public OAuth2AuthenticationProperties getOAuth2AuthenticationProperties() {
        return oauth2AuthenticationProperties;
    }

    public OAuth2FormLoginConfigurerCustomizer getOAuth2FormLoginConfigurerCustomizer() {
        return oauth2FormLoginConfigurerCustomizer;
    }

    public OAuth2AccessTokenResponseHandler getOAuth2AccessTokenResponseHandler() {
        return oauth2AccessTokenResponseHandler;
    }

    public OAuth2AuthenticationFailureHandler getOAuth2AuthenticationFailureHandler() {
        return oauth2AuthenticationFailureHandler;
    }
}
