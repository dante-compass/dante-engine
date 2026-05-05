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

package org.dromara.dante.security.definition;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.dante.security.domain.OAuth2AuthorizationResource;
import org.dromara.dante.security.domain.RegisteredClientTransmitter;

import java.util.Optional;

/**
 * <p>Description: OAuth2 认证资源定义 </p>
 * <p>
 * 主要用于提供 OAuth2 确认页面中的资源。定义该接口是为了统一处理不同用途的自定义 RegisteredClient，例如：OAuth2Application 和 IotDevice。
 * 如果没有该统一定义，确认页面中的信息将无法进行兼容处理。支持 OAuth2Application 就无法支持 IotDevice
 *
 * @author : gengwei.zheng
 * @date : 2025/2/25 18:01
 */
public interface OAuth2AuthorizationResourceService {

    /**
     * 保存 {@link OAuth2AuthorizationResource}
     *
     * @param transmitter {@link RegisteredClientTransmitter}
     */
    void save(RegisteredClientTransmitter transmitter);

    /**
     * 根据 ClientId 查询 {@link OAuth2AuthorizationResource}
     *
     * @param clientId OAuth2 Client ID
     * @return {@link OAuth2AuthorizationResource}
     */
    OAuth2AuthorizationResource findByClientId(String clientId);

    /**
     * 根据 ID 删除资源
     *
     * @param id ID
     */
    void deleteById(String id);

    /**
     * 通用逻辑模版化定义
     *
     * @param transmitter {@link OAuth2AuthorizationResource}
     */
    default void process(RegisteredClientTransmitter transmitter) {
        RegisteredClientTransmitter clone = SerializationUtils.clone(transmitter);
        if (transmitter.isRegistrationClient()) {
            // 查询上级客户端，使用上级客户端的资源作为动态注册客户端的资源
            OAuth2AuthorizationResource parentResource = findByClientId(transmitter.getParentClientId());

            Optional.ofNullable(parentResource)
                    .filter(item -> StringUtils.isNotBlank(item.getLogo()))
                    .ifPresent(item -> clone.setLogo(item.getLogo()));
        }

        save(clone);
    }
}
