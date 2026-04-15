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

package org.dromara.dante.persistence.sas.jpa.specification;

import jakarta.transaction.Transactional;
import org.dromara.dante.persistence.commons.definition.AbstractEnhanceAuthenticationManager;
import org.dromara.dante.persistence.sas.jpa.repository.HerodotusRegisteredClientRepository;
import org.dromara.dante.security.service.OAuth2AuthorizationResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * <p>Description: 默认的认证管理器 </p>
 * <p>
 * 用于 Spring Authorization Server 外部业务实现开启和关闭 OAuth2 认证的相关功能。
 *
 * @author : gengwei.zheng
 * @date : 2024/10/9 15:21
 */
public class JpaEnhanceAuthenticationManager extends AbstractEnhanceAuthenticationManager {

    private static final Logger log = LoggerFactory.getLogger(JpaEnhanceAuthenticationManager.class);

    private final HerodotusRegisteredClientRepository herodotusRegisteredClientRepository;

    public JpaEnhanceAuthenticationManager(HerodotusRegisteredClientRepository herodotusRegisteredClientRepository, RegisteredClientRepository registeredClientRepository, OAuth2AuthorizationResourceService authorizationResourceService) {
        super(registeredClientRepository, authorizationResourceService);
        this.herodotusRegisteredClientRepository = herodotusRegisteredClientRepository;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void disable(String id) {
        log.debug("[Herodotus] |- [AUTHENTICATION-SWITCH] Authentication disable FINISHED for [{}].", id);
        herodotusRegisteredClientRepository.deleteById(id);
        deleteResource(id);
    }
}
