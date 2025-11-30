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

package cn.herodotus.dante.oauth2.persistence.sas.jpa.jackson;

import cn.herodotus.dante.security.jackson.JsonNodeUtils;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;

import java.time.Instant;
import java.util.Set;

/**
 * <p>Description: RegisteredClientDeserializer </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/10/24 15:11
 */
public class RegisteredClientDeserializer extends ValueDeserializer<RegisteredClient> {

    private static final TypeReference<Set<ClientAuthenticationMethod>> CLIENT_AUTHENTICATION_METHOD_SET = new TypeReference<>() {
    };
    private static final TypeReference<Set<AuthorizationGrantType>> AUTHORIZATION_GRANT_TYPE_SET = new TypeReference<>() {
    };

    @Override
    public RegisteredClient deserialize(JsonParser parser, DeserializationContext context) throws JacksonException {

        JsonNode root = context.readTree(parser);
        return deserialize(context, root);
    }

    private RegisteredClient deserialize(DeserializationContext context, JsonNode root) throws JacksonException {

        String id = JsonNodeUtils.findStringValue(root, "id");
        String clientId = JsonNodeUtils.findStringValue(root, "clientId");
        Instant clientIdIssuedAt = JsonNodeUtils.findValue(root, "clientIdIssuedAt", JsonNodeUtils.INSTANT, context);
        String clientSecret = JsonNodeUtils.findStringValue(root, "clientSecret");
        Instant clientSecretExpiresAt = JsonNodeUtils.findValue(root, "clientSecretExpiresAt", JsonNodeUtils.INSTANT, context);
        String clientName = JsonNodeUtils.findStringValue(root, "clientName");

        Set<ClientAuthenticationMethod> clientAuthenticationMethods = JsonNodeUtils.findValue(root, "clientAuthenticationMethods", CLIENT_AUTHENTICATION_METHOD_SET, context);
        Set<AuthorizationGrantType> authorizationGrantTypes = JsonNodeUtils.findValue(root, "authorizationGrantTypes", AUTHORIZATION_GRANT_TYPE_SET, context);
        Set<String> redirectUris = JsonNodeUtils.findValue(root, "redirectUris", JsonNodeUtils.STRING_SET, context);
        Set<String> postLogoutRedirectUris = JsonNodeUtils.findValue(root, "postLogoutRedirectUris", JsonNodeUtils.STRING_SET, context);
        Set<String> scopes = JsonNodeUtils.findValue(root, "scopes", JsonNodeUtils.STRING_SET, context);
        ClientSettings clientSettings = JsonNodeUtils.findValue(root, "clientSettings", new TypeReference<>() {
        }, context);
        TokenSettings tokenSettings = JsonNodeUtils.findValue(root, "tokenSettings", new TypeReference<>() {
        }, context);

        return RegisteredClient.withId(id)
                .clientId(clientId)
                .clientIdIssuedAt(clientIdIssuedAt)
                .clientSecret(clientSecret)
                .clientSecretExpiresAt(clientSecretExpiresAt)
                .clientName(clientName)
                .clientAuthenticationMethods(methods -> methods.addAll(clientAuthenticationMethods))
                .authorizationGrantTypes(types -> types.addAll(authorizationGrantTypes))
                .redirectUris(uris -> uris.addAll(redirectUris))
                .postLogoutRedirectUris(uris -> uris.addAll(postLogoutRedirectUris))
                .scopes(s -> s.addAll(scopes))
                .clientSettings(clientSettings)
                .tokenSettings(tokenSettings)
                .build();
    }
}
