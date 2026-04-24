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

import org.dromara.dante.oauth2.authorization.autoconfigure.bus.RemoteOAuth2DeviceVerificationSuccessEvent;
import org.dromara.dante.oauth2.commons.event.OAuth2DeviceVerificationSuccessEvent;
import org.dromara.dante.oauth2.commons.strategy.OAuth2DeviceVerificationSuccessEventManager;
import org.dromara.dante.security.domain.DeviceVerificationTransmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: 默认的设备验证成功事件管理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/2/28 21:48
 */
public class DefaultOAuth2DeviceVerificationSuccessEventManager implements OAuth2DeviceVerificationSuccessEventManager {

    private static final Logger log = LoggerFactory.getLogger(DefaultOAuth2DeviceVerificationSuccessEventManager.class);

    @Override
    public String getDestinationServiceName() {
        return "";
    }

    @Override
    public void postLocalProcess(DeviceVerificationTransmitter data) {
        log.debug("[Herodotus] |- [OAUTH2-DEVICE-VERIFICATION] Start sync DeviceVerification process from local!");
        publishEvent(new OAuth2DeviceVerificationSuccessEvent(data));
    }

    @Override
    public void postRemoteProcess(String data, String originService, String destinationService) {
        log.debug("[Herodotus] |- [OAUTH2-DEVICE-VERIFICATION] Start sync DeviceVerification process from remote!");
        publishEvent(new RemoteOAuth2DeviceVerificationSuccessEvent(data, originService, destinationService));
    }
}
