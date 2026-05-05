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

package org.dromara.dante.oauth2.authentication.autoconfigure.strategy;

import org.dromara.dante.oauth2.authorization.autoconfigure.bus.RemoteOidcClientRegistrationSuccessEvent;
import org.dromara.dante.oauth2.commons.event.OidcClientRegistrationSuccessEvent;
import org.dromara.dante.oauth2.commons.strategy.ClientRegistrationSuccessEventManager;
import org.dromara.dante.security.domain.RegisteredClientTransmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: 默认的客户端注册事件管理器 </p>
 * <p>
 * 目前主要用于物联网服务，实现一型一密类型认证
 *
 * @author : gengwei.zheng
 * @date : 2024/8/21 21:55
 */
public class DefaultClientRegistrationSuccessEventManager implements ClientRegistrationSuccessEventManager {

    private static final Logger log = LoggerFactory.getLogger(DefaultClientRegistrationSuccessEventManager.class);

    @Override
    public String getDestinationServiceName() {
        return "";
    }

    @Override
    public void postLocalProcess(RegisteredClientTransmitter data) {
        log.debug("[Herodotus] |- [OIDC-CLIENT-REGISTRATION] Start sync OidcClientRegistration process from local!");
        publishEvent(new OidcClientRegistrationSuccessEvent(data));
    }

    @Override
    public void postRemoteProcess(String data, String originService, String destinationService) {
        log.debug("[Herodotus] |- [OIDC-CLIENT-REGISTRATION] Start sync OidcClientRegistration process from remote!");
        publishEvent(new RemoteOidcClientRegistrationSuccessEvent(data, originService, destinationService));
    }
}
