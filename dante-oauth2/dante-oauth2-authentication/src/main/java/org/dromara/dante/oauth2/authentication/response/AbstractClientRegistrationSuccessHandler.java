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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.dromara.dante.oauth2.commons.strategy.ClientRegistrationSuccessEventManager;
import org.dromara.dante.security.domain.OAuth2AuthorizationResource;
import org.dromara.dante.security.domain.RegisteredClientTransmitter;
import org.dromara.dante.security.service.OAuth2AuthorizationResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.server.authorization.AbstractOAuth2ClientRegistration;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.function.BiFunction;

/**
 * <p>Description: 客户端注册成功处理抽象实现 </p>
 * <p>
 * 自 Spring Authorization Server 合并至 Spring Security 7.0 后，新增了一个 OAuth2 方式的客户端动态注册。至此 SAS 中就包含 OAuth2 和 OIDC 两种方式的客户端动态注册。
 * 因为，扩展方式相同，所以提取一个抽象类处理公共逻辑。
 *
 * @author : gengwei_zheng
 * @date : 2026/4/27 21:47
 */
abstract class AbstractClientRegistrationSuccessHandler<T extends AbstractOAuth2ClientRegistration> implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(AbstractClientRegistrationSuccessHandler.class);

    private final RegisteredClientRepository registeredClientRepository;
    private final OAuth2AuthorizationResourceService authorizationResourceService;
    private final ClientRegistrationSuccessEventManager clientRegistrationSuccessEventManager;

    AbstractClientRegistrationSuccessHandler(RegisteredClientRepository registeredClientRepository, OAuth2AuthorizationResourceService authorizationResourceService, ClientRegistrationSuccessEventManager clientRegistrationSuccessEventManager) {
        this.registeredClientRepository = registeredClientRepository;
        this.authorizationResourceService = authorizationResourceService;
        this.clientRegistrationSuccessEventManager = clientRegistrationSuccessEventManager;
    }

    protected void process(T clientRegistration, BiFunction<T, String, RegisteredClientTransmitter> transmit) {
        // 1. 客户端注册成功之后，才会进入到此处。因此此处如果判断 oauth2_registered_client 中是否存在新注册的 RegisteredClient 没有意义。而且可能会间接导致 oauth2_authorization_resource 表中，出现多条相同 clientId 信息存在，导致查询出错。
        // 2. 同时检测 oauth2_authorization_resource 中是否有新注册的 RegisteredClient。可以规避相同客户端的重复注册
        OAuth2AuthorizationResource authorizationResource = authorizationResourceService.findByClientId(clientRegistration.getClientId());
        if (ObjectUtils.isEmpty(authorizationResource)) {
            RegisteredClient registeredClient = registeredClientRepository.findByClientId(clientRegistration.getClientId());
            if (ObjectUtils.isNotEmpty(registeredClient)) {
                RegisteredClientTransmitter transmitter = transmit.apply(clientRegistration, registeredClient.getId());
                authorizationResourceService.process(transmitter);
                log.debug("[Herodotus] |- Synchronize client dynamic registration information!");
                clientRegistrationSuccessEventManager.postProcess(transmitter);
            }
        }
    }

    protected HttpOutputMessage writeOutputMessage(HttpServletRequest request, HttpServletResponse response) {
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        if (HttpMethod.POST.name().equals(request.getMethod())) {
            httpResponse.setStatusCode(HttpStatus.CREATED);
        } else {
            httpResponse.setStatusCode(HttpStatus.OK);
        }
        return httpResponse;
    }

}
