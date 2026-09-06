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

package cn.herodotus.dante.persistence.commons.converter;

import cn.herodotus.dante.core.constant.SystemConstants;
import cn.herodotus.dante.persistence.commons.definition.ClientSettingsDetails;
import cn.herodotus.dante.persistence.commons.domain.HerodotusClientSettings;
import cn.herodotus.dante.persistence.commons.enums.AllJwsAlgorithm;
import cn.herodotus.dante.security.domain.OAuth2ClientType;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.util.StringUtils;

/**
 * <p>Description: {@link HerodotusClientSettings} 转 {@link ClientSettings} 转换器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/12/20 15:28
 */
public class HerodotusToClientSettingsConverter<S extends ClientSettingsDetails> implements Converter<S, ClientSettings> {

    @Override
    public ClientSettings convert(S source) {
        ClientSettings.Builder clientSettings = ClientSettings.builder();
        clientSettings.requireAuthorizationConsent(source.getRequireAuthorizationConsent());
        clientSettings.requireProofKey(source.getRequireProofKey());
        if (StringUtils.hasText(source.getJwkSetUrl())) {
            clientSettings.jwkSetUrl(source.getJwkSetUrl());
        }

        // 支持 OAuth2 Resource Indicator 所需配置
        if (StringUtils.hasText(source.getResourceIds())) {
            clientSettings.setting(SystemConstants.PARAMETER__RESOURCE_IDS, source.getParentClientId());
        }
        // 支持客户端动态注册指定注册来源。默认指定为 'web'
        if (StringUtils.hasText(source.getClientType())) {
            clientSettings.setting(SystemConstants.PARAMETER__APPLICATION_TYPE, source.getClientType());
        } else {
            clientSettings.setting(SystemConstants.PARAMETER__APPLICATION_TYPE, OAuth2ClientType.WEB.getValue());
        }

        if (StringUtils.hasText(source.getParentClientId())) {
            clientSettings.setting(SystemConstants.PARAMETER__PRODUCT_KEY, source.getParentClientId());
        }

        AllJwsAlgorithm allJwsAlgorithm = source.getAuthenticationSigningAlgorithm();
        if (ObjectUtils.isNotEmpty(allJwsAlgorithm)) {
            if (allJwsAlgorithm.ordinal() < AllJwsAlgorithm.HS256.ordinal()) {
                // 如果是签名算法, 转换成 SAS 签名算法
                SignatureAlgorithm algorithm = SignatureAlgorithm.from(allJwsAlgorithm.name());
                clientSettings.tokenEndpointAuthenticationSigningAlgorithm(ObjectUtils.isNotEmpty(algorithm) ? algorithm : SignatureAlgorithm.RS256);
            } else {
                // 如果是 Mac 算法, 转换成 Mac 签名算法
                MacAlgorithm algorithm = MacAlgorithm.from(allJwsAlgorithm.name());
                clientSettings.tokenEndpointAuthenticationSigningAlgorithm(ObjectUtils.isNotEmpty(algorithm) ? algorithm : MacAlgorithm.HS256);
            }
        }
        if (StringUtils.hasText(source.getX509CertificateSubjectDN())) {
            clientSettings.x509CertificateSubjectDN(source.getX509CertificateSubjectDN());
        }

        return clientSettings.build();
    }
}
