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

package org.dromara.dante.message.servlet.websocket.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.dromara.dante.core.constant.SymbolConstants;
import org.dromara.dante.core.constant.SystemConstants;
import org.dromara.dante.message.servlet.websocket.utils.WebSocketUtils;
import org.dromara.dante.security.domain.UserPrincipal;
import org.dromara.dante.security.definition.BearerTokenResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.session.web.socket.server.SessionRepositoryMessageInterceptor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * <p>Description: WebSocketSessionHandshakeInterceptor </p>
 * <p>
 * 不是开启websocket的必要步骤，根据自身的业务逻辑决定是否添加拦截器
 * <p>
 * 当前主要处理 Token 获取，以及 Token 的验证。如果验证成功，使用返回的用户名进行下一步，如果验证失败返回 false 终止握手。
 *
 * @author : gengwei.zheng
 * @date : 2022/12/4 21:34
 */
public class WebSocketAuthenticationHandshakeInterceptor implements HandshakeInterceptor {

    private static final Logger log = LoggerFactory.getLogger(WebSocketAuthenticationHandshakeInterceptor.class);

    private static final String SEC_WEBSOCKET_PROTOCOL = com.google.common.net.HttpHeaders.SEC_WEBSOCKET_PROTOCOL;

    private final BearerTokenResolver bearerTokenResolver;

    public WebSocketAuthenticationHandshakeInterceptor(BearerTokenResolver bearerTokenResolver) {
        this.bearerTokenResolver = bearerTokenResolver;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        HttpServletRequest httpServletRequest = WebSocketUtils.getHttpServletRequest(request);
        if (ObjectUtils.isNotEmpty(httpServletRequest)) {
            // 在本地调试时经常会出现，停止后端看代码，但是前端没有停止的情况。这时再启动后端，后端就会直接链接后端。
            // 这时后端的 Session 是空的，但是前端 Token 还是有效的。如果不加 Session != null 的判断，就会出现 attributes 还是会获取到 PRINCIPAL
            // 就会导致WebSocketRegistryListener#gafterConnectionEstablished 方法能够获取到 Principal，继续处理 Session，出现 getSessionId(null) 导致抛错问题
            HttpSession session = httpServletRequest.getSession(false);
            if (session != null) {
                SessionRepositoryMessageInterceptor.setSessionId(attributes, session.getId());

                String protocol = httpServletRequest.getHeader(SEC_WEBSOCKET_PROTOCOL);
                String token = determineToken(protocol);
                if (StringUtils.isNotBlank(token)) {
                    UserPrincipal details = bearerTokenResolver.resolve(token);
                    attributes.put(SystemConstants.PRINCIPAL, details);
                    log.debug("[Herodotus] |- WebSocket fetch the token is [{}].", token);
                } else {
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    log.info("[Herodotus] |- Token is invalid for WebSocket, stop handshake.");
                    return false;
                }
            }
        }

        return true;
    }

    private String determineToken(String protocol) {
        if (Strings.CS.contains(protocol, SymbolConstants.COMMA)) {
            String[] protocols = StringUtils.split(protocol, SymbolConstants.COMMA);
            for (String item : protocols) {
                if (!Strings.CS.endsWith(item, ".stomp")) {
                    return item;
                }
            }
        }
        return null;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {

        HttpServletRequest httpServletRequest = WebSocketUtils.getHttpServletRequest(request);
        HttpServletResponse httpServletResponse = WebSocketUtils.getHttpServletResponse(response);

        if (ObjectUtils.isNotEmpty(httpServletRequest) && ObjectUtils.isNotEmpty(httpServletResponse)) {
            httpServletResponse.setHeader(SEC_WEBSOCKET_PROTOCOL, "v10.stomp");
        }

        log.info("[Herodotus] |- WebSocket handshake success!");
    }
}
