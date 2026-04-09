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

package org.dromara.dante.persistence.commons.definition;

import org.dromara.dante.core.constant.SystemConstants;
import org.dromara.dante.security.definition.AuthenticationManager;
import org.dromara.dante.security.domain.RegisteredClientTransmitter;
import org.dromara.dante.spring.context.ServiceContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>Description: OAuth2 认证管理 </p>
 * <p>
 * 增强的认证管理器定义。主要用于添加或删除 Spring Authorization Server 标准数据表 oauth2_registered_client 中的数据。
 * 通过这种方式，变相实现业务功能可以开启和关闭认证的功能
 *
 * @author : gengwei.zheng
 * @date : 2024/10/9 15:04
 */
public interface EnhanceAuthenticationManager extends AuthenticationManager {

    /**
     * 保存认证资源信息
     *
     * @param transmitter 手动创建 oauth2_registered_client 数据的必要信息 {@link RegisteredClientTransmitter}
     */

    void resource(RegisteredClientTransmitter transmitter);

    /**
     * 开启认证
     * <p>
     * 开启认证，即在 oauth2_registered_client 添加对应的数据
     *
     * @param transmitter 手动创建 oauth2_registered_client 数据的必要信息 {@link RegisteredClientTransmitter}
     */
    @Override
    default void enable(RegisteredClientTransmitter transmitter) {

        ClientSettings.Builder clientSettings = ClientSettings.builder();
        clientSettings.requireProofKey(false);
        clientSettings.requireAuthorizationConsent(true);
        clientSettings.tokenEndpointAuthenticationSigningAlgorithm(SignatureAlgorithm.RS256);

        Set<AuthorizationGrantType> authorizationGrantTypes = new HashSet<>(Set.of(AuthorizationGrantType.CLIENT_CREDENTIALS));

        if (transmitter.isRegistrationClient()) {
            clientSettings.setting(SystemConstants.PARAMETER__PRODUCT_KEY, transmitter.getParentClientId());
            authorizationGrantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
            authorizationGrantTypes.add(AuthorizationGrantType.DEVICE_CODE);
        }

        RegisteredClient registeredClient = RegisteredClient.withId(transmitter.getId())
                .clientId(transmitter.getClientId())
                .clientName(transmitter.getClientName())
                .clientSecret(transmitter.getClientSecret())
                .scope(StringUtils.collectionToCommaDelimitedString(List.of(SystemConstants.SCOPE_CLIENT_CREATE, SystemConstants.SCOPE_CLIENT_READ)))
                .authorizationGrantTypes((types) -> types.addAll(authorizationGrantTypes))
                .clientAuthenticationMethods((methods -> methods.addAll(Set.of(ClientAuthenticationMethod.CLIENT_SECRET_POST, ClientAuthenticationMethod.CLIENT_SECRET_BASIC))))
                .redirectUri(ServiceContextHolder.getIotServiceUri())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                        .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
                        .authorizationCodeTimeToLive(Duration.ofMinutes(5))
                        .accessTokenTimeToLive(Duration.ofMinutes(10))
                        .refreshTokenTimeToLive(Duration.ofHours(1))
                        .build())
                .clientSettings(clientSettings.build())
                .build();
        enable(registeredClient);
        resource(transmitter);
    }

    /**
     * 开启认证
     * <p>
     * 开启认证，即在 oauth2_registered_client 添加对应的数据
     *
     * @param registeredClient {@link RegisteredClient}
     */
    void enable(RegisteredClient registeredClient);
}
