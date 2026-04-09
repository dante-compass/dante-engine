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
import org.dromara.dante.persistence.commons.definition.ClientSettingsDetails;
import org.dromara.dante.persistence.commons.enums.AllJwsAlgorithm;
import com.google.common.base.MoreObjects;

/**
 * <p>Description: Client Settings 通用实体定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/12/19 22:52
 */
public class HerodotusClientSettings implements BaseModel, ClientSettingsDetails {

    private Boolean requireProofKey = Boolean.FALSE;

    private Boolean requireAuthorizationConsent = Boolean.FALSE;

    private String jwkSetUrl;

    private AllJwsAlgorithm authenticationSigningAlgorithm;

    private String x509CertificateSubjectDN;

    @Override
    public Boolean getRequireProofKey() {
        return requireProofKey;
    }

    public void setRequireProofKey(Boolean requireProofKey) {
        this.requireProofKey = requireProofKey;
    }

    @Override
    public Boolean getRequireAuthorizationConsent() {
        return requireAuthorizationConsent;
    }

    public void setRequireAuthorizationConsent(Boolean requireAuthorizationConsent) {
        this.requireAuthorizationConsent = requireAuthorizationConsent;
    }

    @Override
    public String getJwkSetUrl() {
        return jwkSetUrl;
    }

    public void setJwkSetUrl(String jwkSetUrl) {
        this.jwkSetUrl = jwkSetUrl;
    }

    @Override
    public AllJwsAlgorithm getAuthenticationSigningAlgorithm() {
        return authenticationSigningAlgorithm;
    }

    public void setAuthenticationSigningAlgorithm(AllJwsAlgorithm authenticationSigningAlgorithm) {
        this.authenticationSigningAlgorithm = authenticationSigningAlgorithm;
    }

    @Override
    public String getX509CertificateSubjectDN() {
        return x509CertificateSubjectDN;
    }

    public void setX509CertificateSubjectDN(String x509CertificateSubjectDN) {
        this.x509CertificateSubjectDN = x509CertificateSubjectDN;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("requireProofKey", requireProofKey)
                .add("requireAuthorizationConsent", requireAuthorizationConsent)
                .add("jwkSetUrl", jwkSetUrl)
                .add("authenticationSigningAlgorithm", authenticationSigningAlgorithm)
                .add("x509CertificateSubjectDN", x509CertificateSubjectDN)
                .toString();
    }
}
