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

package org.dromara.dante.oauth2.authentication.customizer;

import org.dromara.dante.core.constant.SystemConstants;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.*;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>Description: 自定义 Spring Security 请求缓存 RequestMatcher </p>
 * <p>
 * 通过该 {@link RequestMatcher} 生成器，指定可以缓存的具体请求，以实现登录成功后后续流程的跳转。
 * <p>
 * 默认 HttpSessionRequestCache 不支持 POST 类型请求，会导致 OAuth2 Device Flow 跳转到登录页面登录成功后，无法在重定向至设备验证页面，只会跳转到 '/'
 * 指定 OAUTH2_DEVICE_VERIFICATION_ENDPOINT 对应路径可以实现请求缓存，以实现请求的重定向
 * <p>
 * 默认配置逻辑，主要参考 {@link org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer} 中逻辑实现。
 *
 * @author : gengwei_zheng
 * @date : 2026/5/26 19:05
 */
class HerodotusSavedRequestMatcher {

    public RequestMatcher create(HttpSecurity http) {
        // Spring Security 默认 SavedRequest 规则
        RequestMatcher defaultRequestMatcher = createDefaultSavedRequestMatcher(http);

        // OAuth2 设备码授权模式 POST 请求缓存。
        RequestMatcher deviceVerificationMatcher = PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, SystemConstants.OAUTH2_DEVICE_VERIFICATION_ENDPOINT);

        return new OrRequestMatcher(defaultRequestMatcher, deviceVerificationMatcher);
    }

    private RequestMatcher createDefaultSavedRequestMatcher(HttpSecurity http) {
        RequestMatcher notFavIcon = new NegatedRequestMatcher(PathPatternRequestMatcher.withDefaults().matcher("/favicon.*"));
        RequestMatcher notXRequestedWith = new NegatedRequestMatcher(
                new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"));
        RequestMatcher notWebSocket = new NegatedRequestMatcher(
                new RequestHeaderRequestMatcher("Upgrade", "websocket"));

        boolean isCsrfEnabled = http.getConfigurer(CsrfConfigurer.class) != null;
        List<RequestMatcher> matchers = new ArrayList<>();
        if (isCsrfEnabled) {
            RequestMatcher getRequests = PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/**");
            matchers.add(0, getRequests);
        }
        matchers.add(notFavIcon);
        matchers.add(notMatchingMediaType(http, MediaType.APPLICATION_JSON));
        matchers.add(notXRequestedWith);
        matchers.add(notMatchingMediaType(http, MediaType.MULTIPART_FORM_DATA));
        matchers.add(notMatchingMediaType(http, MediaType.TEXT_EVENT_STREAM));
        matchers.add(notWebSocket);
        return new AndRequestMatcher(matchers);
    }

    private RequestMatcher notMatchingMediaType(HttpSecurity http, MediaType mediaType) {
        ContentNegotiationStrategy contentNegotiationStrategy = http.getSharedObject(ContentNegotiationStrategy.class);
        if (contentNegotiationStrategy == null) {
            contentNegotiationStrategy = new HeaderContentNegotiationStrategy();
        }
        MediaTypeRequestMatcher mediaRequest = new MediaTypeRequestMatcher(contentNegotiationStrategy, mediaType);
        mediaRequest.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
        return new NegatedRequestMatcher(mediaRequest);
    }
}
