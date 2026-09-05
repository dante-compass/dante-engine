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

import cn.herodotus.dante.persistence.commons.definition.ClientMetadataValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.OAuth2ClientRegistration;

import java.util.Set;

/**
 * <p>Description: OAuth2 CIMD 信息验证 </p>
 *
 * @author : gengwei_zheng
 * @date : 2026/9/4 13:15
 */
class DefaultClientMetadataValidator implements ClientMetadataValidator {

    private static final Set<String> ALLOWED_TOKEN_ENDPOINT_AUTH_METHODS = Set.of(
            ClientAuthenticationMethod.NONE.getValue(),
            ClientAuthenticationMethod.PRIVATE_KEY_JWT.getValue(),
            ClientAuthenticationMethod.TLS_CLIENT_AUTH.getValue(),
            ClientAuthenticationMethod.SELF_SIGNED_TLS_CLIENT_AUTH.getValue());

    @Override
    public boolean validate(String clientIdUrl, OAuth2ClientRegistration clientRegistration) {
        String clientId = clientRegistration.getClientId();
        if (StringUtils.isBlank(clientId)) {
            return false;
        }
        if (!Strings.CS.equals(clientIdUrl, clientId)) {
            return false;
        }
        if (StringUtils.isNotBlank(clientRegistration.getClientSecret())) {
            return false;
        }
        if (ObjectUtils.isNotEmpty(clientRegistration.getClientSecretExpiresAt())) {
            return false;
        }
        if (CollectionUtils.isEmpty(clientRegistration.getRedirectUris())) {
            return false;
        }
        if (StringUtils.isBlank(clientRegistration.getClientName())) {
            return false;
        }

        String tokenEndpointAuthenticationMethod = clientRegistration.getTokenEndpointAuthenticationMethod();
        return !StringUtils.isNotBlank(tokenEndpointAuthenticationMethod) || ALLOWED_TOKEN_ENDPOINT_AUTH_METHODS.contains(tokenEndpointAuthenticationMethod);
    }

}
