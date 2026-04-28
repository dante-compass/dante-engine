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

package org.dromara.dante.oauth2.authentication.response;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dromara.dante.oauth2.commons.strategy.OAuth2DeviceVerificationSuccessEventManager;
import org.dromara.dante.security.domain.DeviceVerificationTransmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2DeviceVerificationAuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

/**
 * <p>Description: 设备校验成功处理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/2/28 21:52
 */
public class OAuth2DeviceVerificationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuth2DeviceVerificationSuccessHandler.class);

    private final OAuth2DeviceVerificationSuccessEventManager deviceVerificationSuccessEventManager;

    public OAuth2DeviceVerificationSuccessHandler(OAuth2DeviceVerificationSuccessEventManager deviceVerificationSuccessEventManager) {
        this.deviceVerificationSuccessEventManager = deviceVerificationSuccessEventManager;
    }

    public OAuth2DeviceVerificationSuccessHandler(String defaultTargetUrl, OAuth2DeviceVerificationSuccessEventManager deviceVerificationSuccessEventManager) {
        super(defaultTargetUrl);
        setAlwaysUseDefaultTargetUrl(true);
        this.deviceVerificationSuccessEventManager = deviceVerificationSuccessEventManager;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2DeviceVerificationAuthenticationToken deviceVerificationAuthenticationToken = (OAuth2DeviceVerificationAuthenticationToken) authentication;

        if (deviceVerificationAuthenticationToken.isAuthenticated()) {
            log.debug("[Herodotus] |- [OAUTH2-DEVICE-VERIFICATION] Sync verification status to business entity!");
            DeviceVerificationTransmitter transmitter = new DeviceVerificationTransmitter();
            transmitter.setClientId(deviceVerificationAuthenticationToken.getClientId());
            deviceVerificationSuccessEventManager.postProcess(transmitter);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
