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

package org.dromara.dante.persistence.commons.converter;

import org.apache.commons.lang3.ObjectUtils;
import org.dromara.dante.persistence.commons.definition.TokenSettingsDetails;
import org.dromara.dante.persistence.commons.domain.HerodotusTokenSettings;
import org.dromara.dante.persistence.commons.enums.SignatureJwsAlgorithm;
import org.dromara.dante.persistence.commons.enums.TokenFormat;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

/**
 * <p>Description: {@link HerodotusTokenSettings} 转 {@link TokenSettings} 转换器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/12/20 15:28
 */
public class HerodotusToTokenSettingsConverter<S extends TokenSettingsDetails> implements Converter<S, TokenSettings> {

    @Override
    public TokenSettings convert(S source) {
        TokenSettings.Builder tokenSettingsBuilder = TokenSettings.builder();
        tokenSettingsBuilder.authorizationCodeTimeToLive(source.getAuthorizationCodeTimeToLive());
        tokenSettingsBuilder.accessTokenTimeToLive(source.getAccessTokenTimeToLive());
        tokenSettingsBuilder.accessTokenFormat(convert(source.getTokenFormat()));
        tokenSettingsBuilder.deviceCodeTimeToLive(source.getDeviceCodeTimeToLive());
        // 是否可重用刷新令牌
        tokenSettingsBuilder.reuseRefreshTokens(source.getReuseRefreshTokens());
        // refreshToken 的有效期
        tokenSettingsBuilder.refreshTokenTimeToLive(source.getRefreshTokenTimeToLive());
        tokenSettingsBuilder.x509CertificateBoundAccessTokens(source.getX509CertificateBoundAccessTokens());
        SignatureJwsAlgorithm signatureJwsAlgorithm = source.getIdTokenSignatureAlgorithmJwsAlgorithm();
        if (ObjectUtils.isNotEmpty(signatureJwsAlgorithm)) {
            tokenSettingsBuilder.idTokenSignatureAlgorithm(SignatureAlgorithm.from(signatureJwsAlgorithm.name()));
        }
        return tokenSettingsBuilder.build();
    }

    private OAuth2TokenFormat convert(TokenFormat tokenFormat) {
        if (ObjectUtils.isEmpty(tokenFormat)) {
            return OAuth2TokenFormat.REFERENCE;
        } else {
            return tokenFormat == TokenFormat.REFERENCE ? OAuth2TokenFormat.REFERENCE : OAuth2TokenFormat.SELF_CONTAINED;
        }
    }
}
