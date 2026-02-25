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

package org.dromara.dante.servlet.container.autoconfigure.oauth2;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.dante.security.converter.UserPrincipalConverter;
import org.dromara.dante.security.definition.BearerTokenResolver;
import org.dromara.dante.security.domain.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

/**
 * <p>Description: Servlet 环境 JWT Token 手动解析器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/4/8 16:48
 */
public class HerodotusServletJwtTokenResolver implements BearerTokenResolver {

    private final JwtDecoder jwtDecoder;

    private static final Logger log = LoggerFactory.getLogger(HerodotusServletJwtTokenResolver.class);

    public HerodotusServletJwtTokenResolver(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public UserPrincipal resolve(String token) {

        if (StringUtils.isBlank(token)) {
            throw new IllegalArgumentException("token can not be null");
        }

        Jwt jwt = getJwt(token);
        if (ObjectUtils.isNotEmpty(jwt)) {
            UserPrincipal userPrincipal = UserPrincipalConverter.toUserPrincipal(jwt);
            log.debug("[Herodotus] |- Resolve JWT token to principal details [{}]", userPrincipal);
            return userPrincipal;
        }

        return null;
    }

    private Jwt getJwt(String token) {
        try {
            return this.jwtDecoder.decode(token);
        } catch (BadJwtException failed) {
            log.error("[Herodotus] |- Failed to decode since the JWT was invalid");
        } catch (JwtException failed) {
            log.error("[Herodotus] |- Failed to decode JWT, catch exception", failed);
        }

        return null;
    }
}
