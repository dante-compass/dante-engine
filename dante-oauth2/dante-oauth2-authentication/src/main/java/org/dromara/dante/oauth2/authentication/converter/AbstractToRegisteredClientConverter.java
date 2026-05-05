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
import org.apache.commons.lang3.Strings;
import org.dromara.dante.core.constant.SymbolConstants;
import org.dromara.dante.core.constant.SystemConstants;
import org.dromara.dante.oauth2.authentication.utils.OAuth2EndpointUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.AbstractOAuth2ClientRegistration;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.util.List;

/**
 * <p>Description: SAS 客户端注册实体转 {@link RegisteredClient} 转换器抽象定义 </p>
 * <p>
 * 客户端动态注册时，增加自定义属性的扩展处理。
 * <p>
 * 自 Spring Authorization Server 合并至 Spring Security 7.0 后，新增了一个 OAuth2 方式的客户端动态注册。至此 SAS 中就包含 OAuth2 和 OIDC 两种方式的客户端动态注册。
 * 因为，扩展方式相同，所以提取一个抽象类处理公共逻辑。
 *
 * @author : gengwei_zheng
 * @date : 2026/4/27 18:09
 */
abstract class AbstractToRegisteredClientConverter<T extends AbstractOAuth2ClientRegistration> implements Converter<T, RegisteredClient> {

    private final List<String> clientMetadata;
    private final boolean isRemoteValidate;

    protected AbstractToRegisteredClientConverter(List<String> clientMetadata, boolean isRemoteValidate) {
        this.clientMetadata = clientMetadata;
        this.isRemoteValidate = isRemoteValidate;
    }

    private OAuth2TokenFormat getTokenFormat() {
        if (isRemoteValidate) {
            return OAuth2TokenFormat.REFERENCE;
        } else {
            return OAuth2TokenFormat.SELF_CONTAINED;
        }
    }

    protected abstract RegisteredClient convertToRegisteredClient(T source);

    @Override
    public RegisteredClient convert(T source) {
        // 先使用 Spring Authorization Server 默认的 XXXRegisteredClientConverter 将 XXXClientRegistration 转换为 RegisteredClient
        // 使用默认的 XXXRegisteredClientConverter 减少转换错误
        RegisteredClient registeredClient = this.convertToRegisteredClient(source);

        // 默认的 XXXRegisteredClientConverter 会设置一些默认值，不好进行修改，使用 from 重新生成一份 RegisteredClient.Builder 以便设定参数。
        RegisteredClient.Builder builder = RegisteredClient.from(registeredClient);

        // 自定义动态注册属性。
        ClientSettings.Builder clientSettingsBuilder = ClientSettings.withSettings(registeredClient.getClientSettings().getSettings());
        if (CollectionUtils.isNotEmpty(this.clientMetadata)) {
            // 检测是否有缺失的自定义参数，如果有则抛出错误
            this.clientMetadata.stream()
                    .filter(item -> !source.getClaims().containsKey(item))
                    .findAny()
                    .ifPresent(item -> OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, item));

            source.getClaims().forEach((claim, value) -> {
                if (this.clientMetadata.contains(claim)) {
                    // 自定义动态注册属性存入到客户端设置中
                    clientSettingsBuilder.setting(claim, value);

                    // 如果包含 ProductKey 同时 clientId 为空。那么就重新设置 clientId。物联网 clientId 格式为 {ProductKey}.{DeviceName}
                    if (Strings.CS.equals(claim, SystemConstants.PARAMETER__PRODUCT_KEY)) {
                        builder.clientId(value + SymbolConstants.PERIOD + source.getClientName());
                    }
                }
            });
        }

        builder.clientSettings(clientSettingsBuilder.build());

        builder.tokenSettings(TokenSettings.builder()
                .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
                .accessTokenFormat(getTokenFormat())
                .build());

        return builder.build();
    }
}
