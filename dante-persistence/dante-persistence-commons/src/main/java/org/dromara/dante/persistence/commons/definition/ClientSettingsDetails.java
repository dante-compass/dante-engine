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

package org.dromara.dante.persistence.commons.definition;

import org.dromara.dante.persistence.commons.enums.AllJwsAlgorithm;

/**
 * <p>Description: 客户端设置详情定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/12/21 21:46
 */
public interface ClientSettingsDetails {

    Boolean getRequireProofKey();

    Boolean getRequireAuthorizationConsent();

    String getJwkSetUrl();

    AllJwsAlgorithm getAuthenticationSigningAlgorithm();

    String getX509CertificateSubjectDN();

}
