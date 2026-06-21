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

package cn.herodotus.dante.oauth2.authorization.autoconfigure.strategy;

import cn.herodotus.dante.oauth2.commons.strategy.DisableAuthenticationEventManager;
import cn.herodotus.dante.oauth2.commons.strategy.EnableAuthenticationEventManager;
import cn.herodotus.dante.security.definition.AuthenticationManager;
import cn.herodotus.dante.security.domain.RegisteredClientTransmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: 认证管理器 </p>
 * <p>
 * 将动态开启认证管理器 {@link EnableAuthenticationEventManager} 和 将动态关闭认证管理器 {@link DisableAuthenticationEventManager} 封装在一起。
 * 一方面方便 Bean 的注入和配置，另一方面便于其它组件远程控制认证
 *
 * @author : gengwei.zheng
 * @date : 2024/10/9 17:54
 */
public class DefaultAuthenticationManager implements AuthenticationManager {

    private static final Logger log = LoggerFactory.getLogger(DefaultAuthenticationManager.class);

    private final DisableAuthenticationEventManager disableAuthenticationEventManager;
    private final EnableAuthenticationEventManager enableAuthenticationEventManager;

    public DefaultAuthenticationManager(DisableAuthenticationEventManager disableAuthenticationEventManager, EnableAuthenticationEventManager enableAuthenticationEventManager) {
        this.disableAuthenticationEventManager = disableAuthenticationEventManager;
        this.enableAuthenticationEventManager = enableAuthenticationEventManager;
    }

    @Override
    public void disable(String id) {
        log.debug("[Herodotus] |- [AUTHENTICATION-SWITCH] Start Authentication disable process!");
        disableAuthenticationEventManager.postProcess(id);
    }

    @Override
    public void enable(RegisteredClientTransmitter registeredClientTransmitter) {
        log.debug("[Herodotus] |- [AUTHENTICATION-SWITCH] Start Authentication enable process!");
        enableAuthenticationEventManager.postProcess(registeredClientTransmitter);
    }
}
