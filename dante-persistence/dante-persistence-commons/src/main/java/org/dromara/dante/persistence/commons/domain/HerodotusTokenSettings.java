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

package org.dromara.dante.persistence.commons.domain;

import org.dromara.dante.core.domain.BaseModel;
import org.dromara.dante.persistence.commons.definition.TokenSettingsDetails;
import org.dromara.dante.persistence.commons.enums.SignatureJwsAlgorithm;
import org.dromara.dante.persistence.commons.enums.TokenFormat;
import com.google.common.base.MoreObjects;

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
