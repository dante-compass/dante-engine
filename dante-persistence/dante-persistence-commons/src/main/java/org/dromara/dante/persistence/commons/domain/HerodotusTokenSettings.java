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

package org.dromara.dante.persistence.commons.domain;

import com.google.common.base.MoreObjects;
import org.dromara.dante.core.domain.BaseModel;
import org.dromara.dante.persistence.commons.definition.TokenSettingsDetails;
import org.dromara.dante.persistence.commons.enums.SignatureJwsAlgorithm;
import org.dromara.dante.persistence.commons.enums.TokenFormat;

import java.time.Duration;

/**
 * <p>Description: Token Setting 通用实体定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/12/19 22:52
 */
public class HerodotusTokenSettings implements BaseModel, TokenSettingsDetails {

    private Duration authorizationCodeTimeToLive = Duration.ofMinutes(5);

    private Duration accessTokenTimeToLive = Duration.ofMinutes(5);

    private Duration refreshTokenTimeToLive = Duration.ofMinutes(60);

    private TokenFormat tokenFormat = TokenFormat.REFERENCE;

    private Duration deviceCodeTimeToLive = Duration.ofMinutes(5);

    private Boolean reuseRefreshTokens = Boolean.TRUE;

    private SignatureJwsAlgorithm idTokenSignatureAlgorithmJwsAlgorithm = SignatureJwsAlgorithm.RS256;

    private Boolean x509CertificateBoundAccessTokens = Boolean.FALSE;

    @Override
    public Duration getAuthorizationCodeTimeToLive() {
        return authorizationCodeTimeToLive;
    }

    public void setAuthorizationCodeTimeToLive(Duration authorizationCodeTimeToLive) {
        this.authorizationCodeTimeToLive = authorizationCodeTimeToLive;
    }

    @Override
    public Duration getAccessTokenTimeToLive() {
        return accessTokenTimeToLive;
    }

    public void setAccessTokenTimeToLive(Duration accessTokenTimeToLive) {
        this.accessTokenTimeToLive = accessTokenTimeToLive;
    }

    @Override
    public Duration getRefreshTokenTimeToLive() {
        return refreshTokenTimeToLive;
    }

    public void setRefreshTokenTimeToLive(Duration refreshTokenTimeToLive) {
        this.refreshTokenTimeToLive = refreshTokenTimeToLive;
    }

    @Override
    public TokenFormat getTokenFormat() {
        return tokenFormat;
    }

    public void setTokenFormat(TokenFormat tokenFormat) {
        this.tokenFormat = tokenFormat;
    }

    @Override
    public Duration getDeviceCodeTimeToLive() {
        return deviceCodeTimeToLive;
    }

    public void setDeviceCodeTimeToLive(Duration deviceCodeTimeToLive) {
        this.deviceCodeTimeToLive = deviceCodeTimeToLive;
    }

    @Override
    public Boolean getReuseRefreshTokens() {
        return reuseRefreshTokens;
    }

    public void setReuseRefreshTokens(Boolean reuseRefreshTokens) {
        this.reuseRefreshTokens = reuseRefreshTokens;
    }

    @Override
    public SignatureJwsAlgorithm getIdTokenSignatureAlgorithmJwsAlgorithm() {
        return idTokenSignatureAlgorithmJwsAlgorithm;
    }

    public void setIdTokenSignatureAlgorithmJwsAlgorithm(SignatureJwsAlgorithm idTokenSignatureAlgorithmJwsAlgorithm) {
        this.idTokenSignatureAlgorithmJwsAlgorithm = idTokenSignatureAlgorithmJwsAlgorithm;
    }

    @Override
    public Boolean getX509CertificateBoundAccessTokens() {
        return x509CertificateBoundAccessTokens;
    }

    public void setX509CertificateBoundAccessTokens(Boolean x509CertificateBoundAccessTokens) {
        this.x509CertificateBoundAccessTokens = x509CertificateBoundAccessTokens;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("authorizationCodeTimeToLive", authorizationCodeTimeToLive)
                .add("accessTokenTimeToLive", accessTokenTimeToLive)
                .add("refreshTokenTimeToLive", refreshTokenTimeToLive)
                .add("tokenFormat", tokenFormat)
                .add("deviceCodeTimeToLive", deviceCodeTimeToLive)
                .add("reuseRefreshTokens", reuseRefreshTokens)
                .add("idTokenSignatureAlgorithmJwsAlgorithm", idTokenSignatureAlgorithmJwsAlgorithm)
                .add("x509CertificateBoundAccessTokens", x509CertificateBoundAccessTokens)
                .toString();
    }
}
