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
import org.dromara.dante.persistence.commons.definition.ClientSettingsDetails;
import org.dromara.dante.persistence.commons.enums.AllJwsAlgorithm;

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
