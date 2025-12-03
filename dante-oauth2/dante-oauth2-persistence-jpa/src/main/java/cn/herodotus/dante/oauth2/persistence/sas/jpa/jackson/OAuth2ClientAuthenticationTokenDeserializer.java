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

import cn.herodotus.dante.security.domain.HerodotusGrantedAuthority;
import cn.herodotus.dante.core.jackson.JsonNodeUtils;
import cn.hutool.v7.core.reflect.FieldUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;

import java.util.Set;

/**
 * <p>Description: OAuth2ClientAuthenticationTokenDeserializer </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/10/24 14:43
 */
public class OAuth2ClientAuthenticationTokenDeserializer extends ValueDeserializer<OAuth2ClientAuthenticationToken> {

    private static final TypeReference<Set<HerodotusGrantedAuthority>> HERODOTUS_GRANTED_AUTHORITY_SET = new TypeReference<Set<HerodotusGrantedAuthority>>() {
    };

    @Override
    public OAuth2ClientAuthenticationToken deserialize(JsonParser parser, DeserializationContext context) throws JacksonException {

        JsonNode jsonNode = context.readTree(parser);
        return deserialize(context, jsonNode);
    }

    private OAuth2ClientAuthenticationToken deserialize(DeserializationContext context, JsonNode root) throws JacksonException {
        Set<HerodotusGrantedAuthority> authorities = JsonNodeUtils.findValue(root, "authorities", HERODOTUS_GRANTED_AUTHORITY_SET, context);
        RegisteredClient registeredClient = JsonNodeUtils.findValue(root, "registeredClient", new TypeReference<RegisteredClient>() {
        }, context);
        String credentials = JsonNodeUtils.findStringValue(root, "credentials");
        ClientAuthenticationMethod clientAuthenticationMethod = JsonNodeUtils.findValue(root, "clientAuthenticationMethod", new TypeReference<ClientAuthenticationMethod>() {
        }, context);

        OAuth2ClientAuthenticationToken clientAuthenticationToken = new OAuth2ClientAuthenticationToken(registeredClient, clientAuthenticationMethod, credentials);
        if (CollectionUtils.isNotEmpty(authorities)) {
            FieldUtil.setFieldValue(clientAuthenticationToken, "authorities", authorities);
        }
        return clientAuthenticationToken;
    }
}
