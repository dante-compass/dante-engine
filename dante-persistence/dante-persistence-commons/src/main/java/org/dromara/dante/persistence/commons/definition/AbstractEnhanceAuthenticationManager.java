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

package org.dromara.dante.persistence.commons.definition;

import org.apache.commons.lang3.ObjectUtils;
import org.dromara.dante.security.domain.RegisteredClientTransmitter;
import org.dromara.dante.security.service.OAuth2AuthorizationResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * <p>Description: {@link EnhanceAuthenticationManager} 抽象定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/2/25 23:20
 */
public abstract class AbstractEnhanceAuthenticationManager implements EnhanceAuthenticationManager {

    private static final Logger log = LoggerFactory.getLogger(AbstractEnhanceAuthenticationManager.class);

    private final RegisteredClientRepository registeredClientRepository;
    private final OAuth2AuthorizationResourceService authorizationResourceService;

    protected AbstractEnhanceAuthenticationManager(RegisteredClientRepository registeredClientRepository, OAuth2AuthorizationResourceService authorizationResourceService) {
        this.registeredClientRepository = registeredClientRepository;
        this.authorizationResourceService = authorizationResourceService;
    }

    protected void deleteResource(String id) {
        authorizationResourceService.deleteById(id);
    }

    @Override
    public void enable(RegisteredClient registeredClient) {
        if (ObjectUtils.isNotEmpty(registeredClient)) {
            log.debug("[Herodotus] |- [AUTHENTICATION-SWITCH] Authentication enable FINISHED for  [{}].", registeredClient.getClientId());
            registeredClientRepository.save(registeredClient);
        }
    }

    @Override
    public void addResource(RegisteredClientTransmitter transmitter) {
        authorizationResourceService.process(transmitter);
    }
}
