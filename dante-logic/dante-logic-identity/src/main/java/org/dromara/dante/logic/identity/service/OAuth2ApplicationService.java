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

package org.dromara.dante.logic.identity.service;

import org.apache.commons.lang3.ObjectUtils;
import org.dromara.dante.data.jpa.repository.BaseJpaRepository;
import org.dromara.dante.data.jpa.service.AbstractJpaService;
import org.dromara.dante.logic.identity.converter.OAuth2ApplicationToRegisteredClientConverter;
import org.dromara.dante.logic.identity.entity.OAuth2Application;
import org.dromara.dante.logic.identity.entity.OAuth2Scope;
import org.dromara.dante.logic.identity.repository.OAuth2ApplicationRepository;
import org.dromara.dante.persistence.commons.definition.EnhanceAuthenticationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * <p>Description: OAuth2ApplicationService </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/3/1 18:06
 */
@Service
public class OAuth2ApplicationService extends AbstractJpaService<OAuth2Application, String> {

    private static final Logger log = LoggerFactory.getLogger(OAuth2ApplicationService.class);

    private final OAuth2ApplicationRepository oauth2ApplicationRepository;
    private final EnhanceAuthenticationManager enhanceAuthenticationManager;
    private final Converter<OAuth2Application, RegisteredClient> toRegisteredClient;

    public OAuth2ApplicationService(OAuth2ApplicationRepository oauth2ApplicationRepository, EnhanceAuthenticationManager enhanceAuthenticationManager) {
        this.oauth2ApplicationRepository = oauth2ApplicationRepository;
        this.enhanceAuthenticationManager = enhanceAuthenticationManager;
        this.toRegisteredClient = new OAuth2ApplicationToRegisteredClientConverter();
    }

    @Override
    public BaseJpaRepository<OAuth2Application, String> getRepository() {
        return this.oauth2ApplicationRepository;
    }

    /**
     * 覆盖基础 {@link AbstractJpaService} 中的 save 方法，实现 save 功能的扩展。
     * <p>
     * 扩展 save 方法，增加对 SAS 数据表 oauth2_registered_client 的数据同步添加、修改功能。
     * <p>
     * 之所以这样实现：
     * 一方面：可以不需要破坏 SAS 数据表 oauth2_registered_client 原有设计，让其始终保持独立与“原汁原味”，变相减少对 SAS 本身逻辑的影响
     * 另一方面：{@link OAuth2Application} 是对 SAS 数据表 oauth2_registered_client 的业务扩展，扩展与核心各自独立，扩展就可以更灵活
     * <p>
     * 之所以扩展 save 方法，是因为当前系统通用的保存和修改 RestController 都是调用的 Service 层的 save 方法。
     *
     * @param entity 数据对应实体
     * @return 保存后的数据对应实体
     */
    @Override
    public OAuth2Application save(OAuth2Application entity) {
        OAuth2Application application = super.save(entity);
        return synchronize(application);
    }

    /**
     * 向 SAS 数据表 oauth2_registered_client 同步增加 OAuth2 Client 数据，实现 OAuth2 支持（任何 OAuth2 应用首先必须要有 Client，即 oauth2_registered_client 中的数据）。
     * <p>
     * 注意：
     * 该同步方法，底层核心逻辑是“异步操作”，因此极端情况下就会存在“数据一致性”问题。
     * 正常情况下，因为添加应用操作并不是频繁性操作，所以异步操作可以满足需要。所以，当前默认采用的就是“异步”方式，这样也可以降低逻辑复杂度
     * <p>
     * 如果实际应用对此有强烈的一致性要求，那么需要自己扩展数据操作确认操作以及失败后的补偿操作，来确保一致性。
     * 数据确认操作：即可以在 {@link OAuth2Application} 中，增加一个状态，{@link OAuth2Application} 添加成功后设定为一个 例如：pending 的状态，待 oauth2_registered_client 数据同步成功之后，再返回一个 success 状态。这样来确保一致性。
     * 补偿操作：假设 {@link OAuth2Application} 数据一直是pending 的状态，那么就认为 oauth2_registered_client 未添加成功，可以增加例如手动或者其他方式
     * <p>
     * 注意事项：如果要做强一致性处理，除了当前的 {@link OAuth2Application} 以外，ThingsBrain 物联网平台中的 Product 和 Device 也是采用相同的逻辑，所以也需要做一致性处理。
     *
     * @param entity 数据对应实体
     * @return 数据对应实体
     */
    public OAuth2Application synchronize(OAuth2Application entity) {
        if (ObjectUtils.isNotEmpty(entity)) {
            enhanceAuthenticationManager.enable(Objects.requireNonNull(toRegisteredClient.convert(entity)));
            return entity;
        } else {
            log.error("[Herodotus] |- OAuth2ApplicationService saveOrUpdate error!");
            throw new NullPointerException("save or update OAuth2Application failed");
        }
    }

    /**
     * 覆盖基础 {@link AbstractJpaService} 中的 save 方法，实现 save 功能的扩展。
     * <p>
     * 扩展 save 方法，增加对 SAS 数据表 oauth2_registered_client 的数据同步删除功能。
     *
     * @param id ID
     */
    @Override
    public void deleteById(String id) {
        super.deleteById(id);
        enhanceAuthenticationManager.disable(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public OAuth2Application authorize(String applicationId, String[] scopeIds) {

        Set<OAuth2Scope> scopes = new HashSet<>();
        for (String scopeId : scopeIds) {
            OAuth2Scope scope = new OAuth2Scope();
            scope.setScopeId(scopeId);
            scopes.add(scope);
        }

        Optional<OAuth2Application> oldApplication = findById(applicationId);
        return oldApplication.map(entity -> {
                    entity.setScopes(scopes);
                    return entity;
                })
                .map(this::saveAndFlush)
                .orElse(null);
    }

    public OAuth2Application findByClientId(String clientId) {
        return oauth2ApplicationRepository.findByClientId(clientId);
    }
}
