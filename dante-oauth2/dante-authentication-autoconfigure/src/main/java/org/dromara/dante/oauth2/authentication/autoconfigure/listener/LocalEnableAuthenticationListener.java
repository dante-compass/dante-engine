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

package org.dromara.dante.oauth2.authentication.autoconfigure.listener;

import org.dromara.dante.oauth2.commons.event.EnableAuthenticationEvent;
import org.dromara.dante.persistence.commons.definition.EnhanceAuthenticationManager;
import org.dromara.dante.security.domain.RegisteredClientTransmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import java.util.Optional;

/**
 * <p>Description: 本地（单体）手动开启认证监听 </p>
 * <p>
 * 主要用于物联网系统，一机一密和一型一密类型认证。
 * · 提供一机一密的设备认证机制，降低设备被攻破的安全风险。适合有能力批量预分配设备证书（ProductKey、DeviceName和DeviceSecret），将设备证书信息烧录到每个设备的芯片。安全级别高。
 * · 提供一型一密的设备认证机制。设备预烧产品证书（ProductKey和ProductSecret），认证时动态获取设备证书（ProductKey、DeviceName和DeviceSecret）。适合批量生产时无法将设备证书烧录每个设备的情况。安全级别普通。
 *
 * @author : gengwei.zheng
 * @date : 2024/8/18 20:55
 */
public class LocalEnableAuthenticationListener implements ApplicationListener<EnableAuthenticationEvent> {

    private static final Logger log = LoggerFactory.getLogger(LocalEnableAuthenticationListener.class);

    private final EnhanceAuthenticationManager enhanceAuthenticationManager;

    public LocalEnableAuthenticationListener(EnhanceAuthenticationManager enhanceAuthenticationManager) {
        this.enhanceAuthenticationManager = enhanceAuthenticationManager;
    }

    @Override
    public void onApplicationEvent(EnableAuthenticationEvent event) {

        log.info("[Herodotus] |- Enable authentication LOCAL listener, response event!");

        RegisteredClientTransmitter registeredClientTransmitter = event.getData();

        log.debug("[Herodotus] |- [AUTHENTICATION-SWITCH] Enable authentication process BEGIN!");

        Optional.ofNullable(registeredClientTransmitter).ifPresent(enhanceAuthenticationManager::enable);
    }
}
