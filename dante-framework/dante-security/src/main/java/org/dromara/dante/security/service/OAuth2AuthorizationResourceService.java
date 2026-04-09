/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2020-2030 郑庚伟 ZHENGGENGWEI (码匠君), <herodotus@aliyun.com> Licensed under the AGPL License
 *
 * This file is part of Herodotus Stirrup.
 *
 * Herodotus Stirrup is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Herodotus Stirrup is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.herodotus.cn>.
 */

package org.dromara.dante.security.service;

import org.dromara.dante.security.domain.OAuth2AuthorizationResource;
import org.dromara.dante.security.domain.RegisteredClientTransmitter;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

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
