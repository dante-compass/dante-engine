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

package org.dromara.dante.oauth2.authentication.converter;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.server.authorization.AbstractOAuth2ClientRegistration;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>Description:  {@link RegisteredClient} 转 SAS 客户端注册实体转换器抽象定义 </p>
 * <p>
 * 客户端动态注册时，增加自定义属性的扩展处理。
 * <p>
 * 自 Spring Authorization Server 合并至 Spring Security 7.0 后，新增了一个 OAuth2 方式的客户端动态注册。至此 SAS 中就包含 OAuth2 和 OIDC 两种方式的客户端动态注册。
 * 因为，扩展方式相同，所以提取一个抽象类处理公共逻辑。
 *
 * @author : gengwei_zheng
 * @date : 2026/4/27 18:26
 */
abstract class AbstractFromRegisteredClientConverter<T extends AbstractOAuth2ClientRegistration> implements Converter<RegisteredClient, T> {

    private final List<String> clientMetadata;

    protected AbstractFromRegisteredClientConverter(List<String> clientMetadata) {
        this.clientMetadata = clientMetadata;
    }

    protected Map<String, Object> updateClaims(RegisteredClient registeredClient, T clientRegistration) {
        Map<String, Object> claims = new HashMap<>(clientRegistration.getClaims());
        if (CollectionUtils.isNotEmpty(this.clientMetadata)) {
            ClientSettings clientSettings = registeredClient.getClientSettings();
            claims.putAll(this.clientMetadata.stream()
                    .filter(metadata -> clientSettings.getSetting(metadata) != null)
                    .collect(Collectors.toMap(Function.identity(), clientSettings::getSetting)));
        }
        return claims;
    }
}
