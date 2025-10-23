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

package cn.herodotus.engine.oauth2.extension.converter;

import cn.herodotus.engine.core.identity.utils.SecurityUtils;
import cn.herodotus.engine.oauth2.extension.entity.OAuth2UserLogging;
import cn.herodotus.engine.web.core.servlet.utils.HeaderUtils;
import cn.hutool.v7.http.useragent.UserAgent;
import cn.hutool.v7.http.useragent.UserAgentUtil;
import com.google.common.net.HttpHeaders;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;

/**
 * <p>Description: 请求转成 {@link OAuth2UserLogging} </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/12/12 17:18
 */
public class RequestToUserLoggingConverter implements Converter<HttpServletRequest, OAuth2UserLogging> {

    private final String principal;
    private final String clientId;
    private final String operation;

    public RequestToUserLoggingConverter(OAuth2AccessTokenAuthenticationToken token) {
        this(SecurityUtils.getUsername(token), token.getRegisteredClient().getId(), "登录系统");
    }

    public RequestToUserLoggingConverter(OAuth2Authorization authorization) {
        this(authorization.getPrincipalName(), authorization.getRegisteredClientId(), "退出系统");
    }

    private RequestToUserLoggingConverter(String principal, String clientId, String operation) {
        this.principal = principal;
        this.clientId = clientId;
        this.operation = operation;
    }

    @Override
    public OAuth2UserLogging convert(HttpServletRequest source) {

        OAuth2UserLogging target = new OAuth2UserLogging();
        target.setPrincipalName(principal);
        target.setClientId(clientId);
        target.setIp(HeaderUtils.getIp(source));
        target.setOperation(operation);

        withUserAgent(target, source);

        return target;
    }

    private void withUserAgent(OAuth2UserLogging target, HttpServletRequest source) {
        UserAgent userAgent = UserAgentUtil.parse(source.getHeader(HttpHeaders.USER_AGENT));
        if (ObjectUtils.isNotEmpty(userAgent)) {
            target.setMobile(userAgent.isMobile());
            target.setOsName(userAgent.getOs().getName());
            target.setBrowserName(userAgent.getBrowser().getName());
            target.setMobileBrowser(userAgent.getBrowser().isMobile());
            target.setEngineName(userAgent.getEngine().getName());
            target.setMobilePlatform(userAgent.getPlatform().isMobile());
            target.setIphoneOrIpod(userAgent.getPlatform().isIPhoneOrIPod());
            target.setIpad(userAgent.getPlatform().isIPad());
            target.setIos(userAgent.getPlatform().isIos());
            target.setAndroid(userAgent.getPlatform().isAndroid());
        }
    }
}
