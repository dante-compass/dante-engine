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

package cn.herodotus.engine.rest.servlet.identity.controller;

import cn.herodotus.engine.core.definition.domain.SecretKey;
import cn.herodotus.engine.core.definition.enums.Protocol;
import cn.herodotus.engine.oauth2.core.properties.OAuth2AuthenticationProperties;
import cn.herodotus.engine.web.core.servlet.utils.SessionUtils;
import cn.herodotus.engine.web.servlet.crypto.HttpCryptoProcessor;
import cn.hutool.v7.core.codec.binary.Base64;
import com.google.common.net.HttpHeaders;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.boot.session.autoconfigure.SessionProperties;
import org.springframework.http.ResponseCookie;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import java.util.Map;

/**
 * <p>Description: Security 登录控制器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/3/21 19:52
 * @see org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer
 * @see org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter
 */
@Controller
public class OAuth2SignInController {

    private final OAuth2AuthenticationProperties authenticationProperties;
    private final SessionProperties sessionProperties;
    private final HttpCryptoProcessor httpCryptoProcessor;

    public OAuth2SignInController(OAuth2AuthenticationProperties authenticationProperties, SessionProperties sessionProperties, HttpCryptoProcessor httpCryptoProcessor) {
        this.authenticationProperties = authenticationProperties;
        this.sessionProperties = sessionProperties;
        this.httpCryptoProcessor = httpCryptoProcessor;
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {

        ModelAndView modelAndView = new ModelAndView("login");

        boolean loginError = isErrorPage(request);
        boolean logoutSuccess = isLogoutSuccess(request);
        String errorMessage = getErrorMessage(request);

        // 登录可配置密码参数
        modelAndView.addObject("vulgar_tycoon", getFormLogin().getUsernameParameter());
        modelAndView.addObject("beast", getFormLogin().getPasswordParameter());
        modelAndView.addObject("anubis", getFormLogin().getRememberMeParameter());
        modelAndView.addObject("graphic", getFormLogin().getCaptchaParameter());
        modelAndView.addObject("show_verification_code", getFormLogin().getCaptchaEnabled());
        modelAndView.addObject("forgot_password_url", getFormLogin().getForgotPasswordUrl());
        modelAndView.addObject("registration_ur", getFormLogin().getRegistrationUrl());
        modelAndView.addObject("show_registration_url", getFormLogin().isRegistrationEnabled());
        modelAndView.addObject("show_forgot_password_url", getFormLogin().isForgotPasswordEnabled());
        // 验证码类别
        modelAndView.addObject("verification_category", getFormLogin().getCategory());
        modelAndView.addObject("login_error", loginError);
        modelAndView.addObject("logout_success", logoutSuccess);
        modelAndView.addObject("message", StringUtils.isNotBlank(errorMessage) ? HtmlUtils.htmlEscape(errorMessage) : null);
        modelAndView.addObject("contextPath", request.getContextPath());

        // AES加密key
        processSecretKey(modelAndView, request, response);

        return modelAndView;
    }

    public void processSecretKey(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {
        String sessionId = SessionUtils.analyseSessionId(request);
        SecretKey secretKey;
        if (StringUtils.isBlank(sessionId)) {
            secretKey = httpCryptoProcessor.createSecretKey(null, sessionProperties.getTimeout());
            sessionId = secretKey.getIdentity();

            ResponseCookie cookie = ResponseCookie.from("SESSION", Base64.encode(secretKey.getIdentity()))
                    .path(request.getContextPath() + "/")
                    .httpOnly(true)
                    .secure(Strings.CI.equals(Protocol.HTTPS.getPrefix(), request.getScheme()))
                    .sameSite("Lax")
                    .maxAge(authenticationProperties.getFormLogin().getCookieMaxAge())
                    .build();

            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        } else {
            secretKey = httpCryptoProcessor.createSecretKey(sessionId, sessionProperties.getTimeout());
            sessionId = secretKey.getIdentity();
        }

        // AES加密key
        modelAndView.addObject("soup_spoon", secretKey.getPublicKey());
        modelAndView.addObject("sessionId", sessionId);
    }

    private OAuth2AuthenticationProperties.FormLogin getFormLogin() {
        return authenticationProperties.getFormLogin();
    }

    private boolean isLogoutSuccess(HttpServletRequest request) {
        String logoutSuccessUrl = getFormLogin().getLogoutSuccessUrl();
        return StringUtils.isNotBlank(logoutSuccessUrl) && matches(request, logoutSuccessUrl);
    }

    private boolean isErrorPage(HttpServletRequest request) {
        return matches(request, getFormLogin().getFailureUrl());
    }

    private String getErrorMessage(HttpServletRequest request) {
        HttpSession session = SessionUtils.getSession(request);
        if (ObjectUtils.isNotEmpty(session)) {
            String message = (String) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ObjectUtils.isNotEmpty(message)) {
                return message;
            }
        }

        return null;
    }

    private boolean matches(HttpServletRequest request, String url) {
        if (!"GET".equals(request.getMethod()) || url == null) {
            return false;
        }
        String uri = request.getRequestURI();
        int pathParamIndex = uri.indexOf(';');
        if (pathParamIndex > 0) {
            // strip everything after the first semi-colon
            uri = uri.substring(0, pathParamIndex);
        }
        if (request.getQueryString() != null) {
            uri += "?" + request.getQueryString();
        }
        if ("".equals(request.getContextPath())) {
            return uri.equals(url);
        }
        return uri.equals(request.getContextPath() + url);
    }
}
