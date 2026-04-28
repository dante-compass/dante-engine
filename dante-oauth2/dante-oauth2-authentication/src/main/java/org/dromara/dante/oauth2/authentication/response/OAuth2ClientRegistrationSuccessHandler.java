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

package org.dromara.dante.oauth2.authentication.response;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dromara.dante.oauth2.authentication.converter.AbstractToRegisteredClientTransmitterConverter;
import org.dromara.dante.oauth2.commons.strategy.ClientRegistrationSuccessEventManager;
import org.dromara.dante.security.domain.RegisteredClientTransmitter;
import org.dromara.dante.security.service.OAuth2AuthorizationResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.OAuth2ClientRegistration;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientRegistrationAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.http.converter.OAuth2ClientRegistrationHttpMessageConverter;

import java.io.IOException;

/**
 * <p>Description: OAuth2 客户端自动注册成功后续逻辑处理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/5/23 17:37
 */
public class OAuth2ClientRegistrationSuccessHandler extends AbstractClientRegistrationSuccessHandler<OAuth2ClientRegistration> {

    private static final Logger log = LoggerFactory.getLogger(OAuth2ClientRegistrationSuccessHandler.class);

    private final HttpMessageConverter<OAuth2ClientRegistration> clientRegistrationHttpMessageConverter =
            new OAuth2ClientRegistrationHttpMessageConverter();

    public OAuth2ClientRegistrationSuccessHandler(RegisteredClientRepository registeredClientRepository, OAuth2AuthorizationResourceService authorizationResourceService, ClientRegistrationSuccessEventManager clientRegistrationSuccessEventManager) {
        super(registeredClientRepository, authorizationResourceService, clientRegistrationSuccessEventManager);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("[Herodotus] |- OAuth2 client dynamic registration successful!");

        OAuth2ClientRegistrationAuthenticationToken clientRegistrationAuthenticationToken =
                (OAuth2ClientRegistrationAuthenticationToken) authentication;

        OAuth2ClientRegistration clientRegistration = clientRegistrationAuthenticationToken.getClientRegistration();

        process(clientRegistration, (registration, id) -> {
            Converter<OAuth2ClientRegistration, RegisteredClientTransmitter> toTransmitter = new OAuth2ClientRegistrationToRegisteredClientTransmitterConverter(id);
            return toTransmitter.convert(registration);
        });

        this.clientRegistrationHttpMessageConverter.write(clientRegistration, null, writeOutputMessage(request, response));
    }

    static class OAuth2ClientRegistrationToRegisteredClientTransmitterConverter extends AbstractToRegisteredClientTransmitterConverter<OAuth2ClientRegistration> {
        public OAuth2ClientRegistrationToRegisteredClientTransmitterConverter(String id) {
            super(id);
        }
    }
}
