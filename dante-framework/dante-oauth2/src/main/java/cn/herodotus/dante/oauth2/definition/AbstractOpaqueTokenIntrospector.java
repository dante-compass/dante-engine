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

package cn.herodotus.dante.oauth2.definition;

import cn.herodotus.dante.core.constant.SystemConstants;
import cn.herodotus.engine.core.identity.domain.HerodotusGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;

import java.time.Instant;
import java.util.*;

/**
 * <p>Description: OpaqueTokenIntrospector 通用代码抽象 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/4/8 11:16
 */
public abstract class AbstractOpaqueTokenIntrospector {

    protected OAuth2AuthenticatedPrincipal convertClaimsSet(Map<String, Object> claims) {
        claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.AUD, (k, v) -> {
            if (v instanceof String) {
                return Collections.singletonList(v);
            }
            return v;
        });
        claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.CLIENT_ID, (k, v) -> v.toString());
        claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.EXP,
                (k, v) -> Instant.ofEpochSecond(((Number) v).longValue()));
        claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.IAT,
                (k, v) -> Instant.ofEpochSecond(((Number) v).longValue()));
        // RFC-7662 page 7 directs users to RFC-7519 for defining the values of these
        // issuer fields.
        // https://datatracker.ietf.org/doc/html/rfc7662#page-7
        //
        // RFC-7519 page 9 defines issuer fields as being 'case-sensitive' strings
        // containing
        // a 'StringOrURI', which is defined on page 5 as being any string, but strings
        // containing ':'
        // should be treated as valid URIs.
        // https://datatracker.ietf.org/doc/html/rfc7519#section-2
        //
        // It is not defined however as to whether-or-not normalized URIs should be
        // treated as the same literal
        // value. It only defines validation itself, so to avoid potential ambiguity or
        // unwanted side effects that
        // may be awkward to debug, we do not want to manipulate this value. Previous
        // versions of Spring Security
        // would *only* allow valid URLs, which is not what we wish to achieve here.
        claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.ISS, (k, v) -> v.toString());
        claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.NBF,
                (k, v) -> Instant.ofEpochSecond(((Number) v).longValue()));
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.SCOPE, (k, v) -> v.toString());
        claims.computeIfPresent(SystemConstants.AUTHORITIES, (k, v) -> {
            if (v instanceof ArrayList) {
                List<String> values = (List<String>) v;
                values.forEach(value -> authorities.add(new HerodotusGrantedAuthority(value)));
            }
            return v;
        });
        return new OAuth2IntrospectionAuthenticatedPrincipal(claims, authorities);
    }
}
