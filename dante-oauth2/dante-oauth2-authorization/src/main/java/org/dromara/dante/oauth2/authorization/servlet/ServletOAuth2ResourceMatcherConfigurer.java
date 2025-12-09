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

package org.dromara.dante.oauth2.authorization.servlet;

import org.dromara.dante.security.definition.AbstractSecurityMatcherConfigurer;
import org.dromara.dante.security.properties.OAuth2AuthorizationProperties;
import org.dromara.dante.security.utils.WebPathUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

/**
 * <p>Description: 安全过滤配置处理器 </p>
 * <p>
 * 对静态资源、开放接口等静态配置进行处理。整合默认配置和配置文件中的配置
 *
 * @author : gengwei.zheng
 * @date : 2022/3/8 22:57
 */
public class ServletOAuth2ResourceMatcherConfigurer extends AbstractSecurityMatcherConfigurer {

    private final ResourceUrlProvider resourceUrlProvider;

    private final RequestMatcher[] staticRequestMatchers;
    private final RequestMatcher[] permitAllRequestMatchers;
    private final RequestMatcher[] hasAuthenticatedRequestMatchers;

    public ServletOAuth2ResourceMatcherConfigurer(OAuth2AuthorizationProperties authorizationProperties, ResourceUrlProvider resourceUrlProvider) {
        super(authorizationProperties);
        this.resourceUrlProvider = resourceUrlProvider;
        this.staticRequestMatchers = WebPathUtils.toRequestMatchers(getStaticResources());
        this.permitAllRequestMatchers = WebPathUtils.toRequestMatchers(getPermitAllResources());
        this.hasAuthenticatedRequestMatchers = WebPathUtils.toRequestMatchers(getHasAuthenticatedResources());
    }

    public RequestMatcher[] getStaticRequestMatchers() {
        return staticRequestMatchers;
    }

    public RequestMatcher[] getPermitAllRequestMatchers() {
        return permitAllRequestMatchers;
    }

    public RequestMatcher[] getHasAuthenticatedRequestMatchers() {
        return hasAuthenticatedRequestMatchers;
    }

    /**
     * 判断是否为静态资源
     *
     * @param uri 请求 URL
     * @return 是否为静态资源
     */
    public boolean isStaticRequest(String uri) {
        String staticUri = resourceUrlProvider.getForLookupPath(uri);
        return StringUtils.isNotBlank(staticUri);
    }

    public boolean isPermitAllRequest(HttpServletRequest request) {
        return WebPathUtils.isRequestMatched(getPermitAllRequestMatchers(), request);
    }

    public boolean isHasAuthenticatedRequest(HttpServletRequest request) {
        return WebPathUtils.isRequestMatched(getHasAuthenticatedRequestMatchers(), request);
    }
}
