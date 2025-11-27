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

package cn.herodotus.engine.core.identity.jackson;

import cn.herodotus.engine.core.identity.domain.HerodotusGrantedAuthority;
import cn.herodotus.engine.core.identity.domain.HerodotusUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;

import java.util.Set;

/**
 * <p>Description: 自定义 UserDetails 序列化 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/2/17 21:01
 */
public class HerodotusUserDeserializer extends ValueDeserializer<HerodotusUser> {

    private static final TypeReference<Set<HerodotusGrantedAuthority>> HERODOTUS_GRANTED_AUTHORITY_SET = new TypeReference<>() {
    };
    private static final TypeReference<Set<String>> HERODOTUS_ROLE_SET = new TypeReference<>() {
    };

    /**
     * This method will create {@link User} object. It will ensure successful object
     * creation even if password key is null in serialized json, because credentials may
     * be removed from the {@link User} by invoking {@link User#eraseCredentials()}. In
     * that case there won't be any password key in serialized json.
     *
     * @param parser   the JsonParser
     * @param context the DeserializationContext
     * @return the user
     * @throws JacksonException if an error during JSON processing occurs
     */
    @Override
    public HerodotusUser deserialize(JsonParser parser, DeserializationContext context) throws JacksonException {
        JsonNode jsonNode = context.readTree(parser);
        Set<? extends GrantedAuthority> authorities = JsonNodeUtils.findObject(jsonNode,"authorities", HERODOTUS_GRANTED_AUTHORITY_SET, context);
        Set<String> roles = JsonNodeUtils.findObject(jsonNode, "roles", HERODOTUS_ROLE_SET, context);
        JsonNode passwordNode = JsonNodeUtils.readJsonNode(jsonNode, "password");
        String userId = JsonNodeUtils.findStringValue(jsonNode, "userId");
        String username = JsonNodeUtils.findStringValue(jsonNode, "username");
        String password = passwordNode.asString("");
        boolean enabled = JsonNodeUtils.findBooleanValue(jsonNode, "enabled");
        boolean accountNonExpired = JsonNodeUtils.findBooleanValue(jsonNode, "accountNonExpired");
        boolean credentialsNonExpired = JsonNodeUtils.findBooleanValue(jsonNode, "credentialsNonExpired");
        boolean accountNonLocked = JsonNodeUtils.findBooleanValue(jsonNode, "accountNonLocked");
        String employeeId = JsonNodeUtils.findStringValue(jsonNode, "employeeId");
        String avatar = JsonNodeUtils.findStringValue(jsonNode, "avatar");
        HerodotusUser result = new HerodotusUser(userId, username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, roles, employeeId, avatar);
        if (passwordNode.asText(null) == null) {
            result.eraseCredentials();
        }
        return result;
    }
}
