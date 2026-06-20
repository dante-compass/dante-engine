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

package cn.herodotus.dante.servlet.container.autoconfigure.oauth2;

import cn.herodotus.dante.security.definition.BearerTokenResolver;
import cn.herodotus.dante.security.domain.UserPrincipal;
import cn.herodotus.dante.security.utils.SecurityUtils;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

/**
 * <p>Description: 阻塞式 Bearer Token 处理器 </p>
 * <p>
 * 根据 AccessToken 动态判断是否为 JWT Token，然后动态选择解析器
 *
 * @author : gengwei_zheng
 * @date : 2026/5/15 22:33
 */
public class HerodotusServletBearerTokenResolver implements BearerTokenResolver {

    private final HerodotusServletJwtTokenResolver jwtTokenResolver;
    private final HerodotusServletOpaqueTokenResolver opaqueTokenResolver;

    public HerodotusServletBearerTokenResolver(JwtDecoder jwtDecoder, OpaqueTokenIntrospector opaqueTokenIntrospector) {
        this.jwtTokenResolver = new HerodotusServletJwtTokenResolver(jwtDecoder);
        this.opaqueTokenResolver = new HerodotusServletOpaqueTokenResolver(opaqueTokenIntrospector);
    }

    @Override
    public UserPrincipal resolve(String accessToken) {
        return SecurityUtils.isJwtToken(accessToken) ? jwtTokenResolver.resolve(accessToken) : opaqueTokenResolver.resolve(accessToken);
    }
}
