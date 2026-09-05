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

package cn.herodotus.dante.persistence.commons.metadata;

import cn.herodotus.dante.oauth2.commons.properties.OAuth2AuthenticationProperties;
import cn.herodotus.dante.persistence.commons.definition.ClientIdMetadataDocumentResolver;
import cn.herodotus.dante.persistence.commons.definition.ClientMetadataValidator;
import org.apache.commons.lang3.ObjectUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.server.authorization.OAuth2ClientRegistration;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.converter.OAuth2ClientRegistrationRegisteredClientConverter;
import org.springframework.util.Assert;

/**
 * <p>Description: Client Id Metadata Document {@link RegisteredClientRepository} </p>
 *
 * @author : gengwei_zheng
 * @date : 2026/9/4 13:13
 */
class ClientIdMetadataDocumentRegisteredClientRepository implements RegisteredClientRepository {

    private final ClientIdMetadataDocumentCache cache;
    private final Converter<OAuth2ClientRegistration, RegisteredClient> registeredClientConverter;
    private final ClientIdMetadataDocumentResolver clientIdMetadataDocumentResolver;
    private final ClientMetadataValidator clientMetadataValidator;

    public ClientIdMetadataDocumentRegisteredClientRepository(OAuth2AuthenticationProperties properties) {
        this.cache = new ClientIdMetadataDocumentCache(properties.getClientIdMetadataDocument().getCacheMaxAge());
        this.registeredClientConverter = new OAuth2ClientRegistrationRegisteredClientConverter();
        this.clientIdMetadataDocumentResolver = new DefaultClientIdMetadataDocumentResolver(this.cache,
                properties.getClientIdMetadataDocument().getAllowHttpUrlForClientIdentifier(),
                properties.getClientIdMetadataDocument().getAllowLoopbackHostForClientIdentifier());
        this.clientMetadataValidator = new DefaultClientMetadataValidator();
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        // No-op
    }

    @Override
    public @Nullable RegisteredClient findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return this.cache.getById(id);
    }

    @Override
    public @Nullable RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");

        // 根据 clientId 从缓存中读取 RegisteredClient，如果存在则直接返回缓存内容
        RegisteredClient cachedRegisteredClient = this.cache.getByClientId(clientId);
        if (ObjectUtils.isNotEmpty(cachedRegisteredClient)) {
            return cachedRegisteredClient;
        }

        // 如果缓存中不存在，则直接读取网络内容。如果网络内容读取失败则返回 null
        ClientIdMetadataDocumentResolver.Result result = this.clientIdMetadataDocumentResolver.resolve(clientId);
        if (ObjectUtils.isEmpty(result)) {
            return null;
        }

        // 校验网络内容
        OAuth2ClientRegistration clientRegistration = result.clientRegistration();
        if (!this.clientMetadataValidator.validate(clientId, clientRegistration)) {
            return null;
        }

        // 校验成功后，将客户端注册信息转换为 RegisteredClient
        RegisteredClient registeredClient = this.registeredClientConverter.convert(clientRegistration);
        registeredClient = RegisteredClient.from(registeredClient)
                .id(clientId)
                .clientId(clientId)
                .build();

        if (result.responseAttributes().cacheMaxAgeSeconds() >= 0) {
            long cacheMaxAgeSeconds = result.responseAttributes().cacheMaxAgeSeconds();
            this.cache.put(registeredClient, System.currentTimeMillis() + cacheMaxAgeSeconds * 1000);
        }
        return registeredClient;
    }
}
