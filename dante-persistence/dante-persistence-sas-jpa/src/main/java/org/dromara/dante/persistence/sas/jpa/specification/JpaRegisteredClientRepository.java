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

import org.apache.commons.lang3.ObjectUtils;
import org.dromara.dante.oauth2.commons.exception.RegisteredClientExistsException;
import org.dromara.dante.persistence.commons.jackson.OAuth2JacksonProcessor;
import org.dromara.dante.persistence.sas.jpa.converter.HerodotusToOAuth2RegisteredClientConverter;
import org.dromara.dante.persistence.sas.jpa.converter.OAuth2ToHerodotusRegisteredClientConverter;
import org.dromara.dante.persistence.sas.jpa.entity.HerodotusRegisteredClient;
import org.dromara.dante.persistence.sas.jpa.service.HerodotusRegisteredClientService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.Optional;

/**
 * <p>Description: 基于Jpa 的 RegisteredClient服务 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/2/25 21:27
 */
public class JpaRegisteredClientRepository implements RegisteredClientRepository {

    private final HerodotusRegisteredClientService herodotusRegisteredClientService;
    private final Converter<HerodotusRegisteredClient, RegisteredClient> herodotusToOAuth2Converter;
    private final Converter<RegisteredClient, HerodotusRegisteredClient> oauth2ToHerodotusConverter;

    public JpaRegisteredClientRepository(HerodotusRegisteredClientService herodotusRegisteredClientService) {
        this.herodotusRegisteredClientService = herodotusRegisteredClientService;
        OAuth2JacksonProcessor jacksonProcessor = OAuth2JacksonProcessor.builder().build();
        this.herodotusToOAuth2Converter = new HerodotusToOAuth2RegisteredClientConverter(jacksonProcessor);
        this.oauth2ToHerodotusConverter = new OAuth2ToHerodotusRegisteredClientConverter(jacksonProcessor);
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        // findByClientId 是 SAS 关键操作，如果允许 clientId 重复，会导致查询失败。
        // 客户端动态注册，默认会随机设置一个 RegisteredClient ID。这可能出现相同 clientId 的数据。
        RegisteredClient old = findByClientId(registeredClient.getClientId());
        if (ObjectUtils.isNotEmpty(old) && !old.getId().equals(registeredClient.getId())) {
            throw new RegisteredClientExistsException("Registered client with id " + registeredClient.getClientId() + " already exists");
        }
        this.herodotusRegisteredClientService.save(toEntity(registeredClient));
    }

    @Override
    public RegisteredClient findById(String id) {
        Optional<HerodotusRegisteredClient> herodotusRegisteredClient = this.herodotusRegisteredClientService.findById(id);
        return herodotusRegisteredClient.map(this::toObject).orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return this.herodotusRegisteredClientService.findByClientId(clientId).map(this::toObject).orElse(null);
    }

    private RegisteredClient toObject(HerodotusRegisteredClient herodotusRegisteredClient) {
        return herodotusToOAuth2Converter.convert(herodotusRegisteredClient);
    }

    private HerodotusRegisteredClient toEntity(RegisteredClient registeredClient) {
        return oauth2ToHerodotusConverter.convert(registeredClient);
    }
}
