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

package cn.herodotus.engine.oauth2.core.utils;

import cn.herodotus.engine.core.definition.constant.SymbolConstants;
import cn.herodotus.engine.core.definition.utils.WellFormedUtils;
import cn.herodotus.engine.core.identity.domain.UserPrincipal;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;


/**
 * @author gengwei.zheng
 * @date 2018-3-8
 **/
public class OAuth2Utils {

    public static final String PREFIX_ROLE = "ROLE_";

    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    /**
     * 密码验证
     *
     * @param rawPassword     原始密码
     * @param encodedPassword 加密后的密码
     * @return 密码是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 判断是否为已经是加密过的密码
     *
     * @param password 明文密码
     * @return 是否已经加密
     */
    public static boolean isEncrypted(String password) {
        return Strings.CS.containsAny(password, SymbolConstants.OPEN_CURLY_BRACE, SymbolConstants.CLOSE_CURLY_BRACE);
    }

    /**
     * 密码加密
     *
     * @param password 明文密码
     * @return 已加密密码
     */
    public static String encrypt(String password) {
        return encrypt(password, passwordEncoder);
    }

    public static String encrypt(String password, PasswordEncoder passwordEncoder) {
        if (StringUtils.isNotBlank(password)) {
            if (isEncrypted(password)) {
                return password;
            } else {
                return passwordEncoder.encode(password);
            }
        } else {
            return null;
        }
    }

    public static String wellFormRolePrefix(String content) {
        return WellFormedUtils.robustness(content, PREFIX_ROLE, true, true);
    }

    /**
     * 从 {@link Authentication} 读取用户信息 {@link UserPrincipal}
     *
     * @param authentication {@link Authentication}
     * @return 用户信息 {@link UserPrincipal}
     */
    public static UserPrincipal getUserPrincipal(Authentication authentication) {
        Object details = authentication.getDetails();
        if (ObjectUtils.isNotEmpty(details) && details instanceof UserPrincipal userPrincipal) {
            return userPrincipal;
        }
        return null;
    }

    /**
     * 从 {@link Authentication} 读取用户标识信息
     *
     * @param authentication {@link Authentication}
     * @return 用户名
     */
    public static String getUsername(Authentication authentication) {
        UserPrincipal principal = getUserPrincipal(authentication);
        if (ObjectUtils.isNotEmpty(principal)) {
            return principal.getName();
        }
        return null;
    }

    /**
     * 从 {@link Authentication} 读取用户Id
     *
     * @param authentication {@link Authentication}
     * @return 用户ID
     */
    public static String getUserId(Authentication authentication) {
        UserPrincipal principal = getUserPrincipal(authentication);
        if (ObjectUtils.isNotEmpty(principal)) {
            return principal.getId();
        }
        return null;
    }

    /**
     * 从 {@link Authentication} 读取用户Email
     *
     * @param authentication {@link Authentication}
     * @return 用户Email
     */
    public static String getEmail(Authentication authentication) {
        UserPrincipal principal = getUserPrincipal(authentication);
        if (ObjectUtils.isNotEmpty(principal)) {
            return principal.getEmail();
        }
        return null;
    }

    public static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.AUTHORIZATION_CODE;
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS;
        } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.REFRESH_TOKEN;
        } else if (AuthorizationGrantType.DEVICE_CODE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.DEVICE_CODE;
        } else if (AuthorizationGrantType.JWT_BEARER.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.JWT_BEARER;
        } else if (AuthorizationGrantType.TOKEN_EXCHANGE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.TOKEN_EXCHANGE;
        }

        return new AuthorizationGrantType(authorizationGrantType);        // Custom authorization grant type
    }

    public static ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
        } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_POST;
        } else if (ClientAuthenticationMethod.CLIENT_SECRET_JWT.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_JWT;
        } else if (ClientAuthenticationMethod.PRIVATE_KEY_JWT.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.PRIVATE_KEY_JWT;
        } else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.NONE;
        } else if (ClientAuthenticationMethod.TLS_CLIENT_AUTH.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.TLS_CLIENT_AUTH;
        } else if (ClientAuthenticationMethod.SELF_SIGNED_TLS_CLIENT_AUTH.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.SELF_SIGNED_TLS_CLIENT_AUTH;
        }
        return new ClientAuthenticationMethod(clientAuthenticationMethod);
    }

    public static AuthenticationMethod resolveAuthenticationMethod(String authenticationMethod) {
        if (AuthenticationMethod.HEADER.getValue().equals(authenticationMethod)) {
            return AuthenticationMethod.HEADER;
        } else if (AuthenticationMethod.FORM.getValue().equals(authenticationMethod)) {
            return AuthenticationMethod.FORM;
        } else if (AuthenticationMethod.QUERY.getValue().equals(authenticationMethod)) {
            return AuthenticationMethod.QUERY;
        }
        return new AuthenticationMethod(authenticationMethod);
    }

}
