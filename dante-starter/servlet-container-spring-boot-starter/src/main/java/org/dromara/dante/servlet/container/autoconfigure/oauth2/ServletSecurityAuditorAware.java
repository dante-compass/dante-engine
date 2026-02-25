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

import org.dromara.dante.security.definition.AbstractAuditorAware;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;

import java.util.Optional;

/**
 * <p>Description: 基于 Security 的数据库审计用户信息获取 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/4/7 15:56
 */
public class ServletSecurityAuditorAware extends AbstractAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        SecurityContext context = SecurityContextHolder.getContext();

        return Optional.ofNullable(context)
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .filter(authentication -> authentication instanceof BearerTokenAuthentication)
                .map(BearerTokenAuthentication.class::cast)
                .map(AbstractOAuth2TokenAuthenticationToken::getPrincipal)
                .filter(object -> object instanceof OAuth2IntrospectionAuthenticatedPrincipal)
                .map(OAuth2IntrospectionAuthenticatedPrincipal.class::cast)
                .map(this::getName)
                .or(Optional::empty);
    }
}
