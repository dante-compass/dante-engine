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

package org.dromara.dante.logic.identity.definition;

import cn.hutool.v7.core.data.id.IdUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import org.dromara.dante.logic.identity.entity.OAuth2Scope;
import org.dromara.dante.logic.identity.enums.AllJwsAlgorithm;
import org.dromara.dante.logic.identity.enums.SignatureJwsAlgorithm;
import org.dromara.dante.logic.identity.enums.TokenFormat;
import org.dromara.dante.oauth2.persistence.sas.jpa.definition.AbstractRegisteredClient;

import java.time.Duration;
import java.util.Set;

/**
 * <p>Description: 应用对象转 RegisteredClient 共性属性 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/5/21 17:46
 */
@MappedSuperclass
public abstract class AbstractOAuth2RegisteredClient extends AbstractRegisteredClient {

    @Schema(name = "客户端Id", title = "默认为系统自动生成")
    @Column(name = "client_id", length = 100)
    private String clientId = IdUtil.fastSimpleUUID();

    @Schema(name = "客户端秘钥", title = "这里存储的客户端秘钥是明文，方便使用。默认为系统自动生成")
    @Column(name = "client_secret", length = 100)
    private String clientSecret = IdUtil.fastSimpleUUID();

    /* --- ClientSettings Begin --- */
    @Schema(name = "是否需要证明Key", title = "如果客户端在执行授权码授予流时需要提供验证密钥质询和验证器, 默认False")
    @Column(name = "require_proof_key")
    private Boolean requireProofKey = Boolean.FALSE;

    @Schema(name = "是否需要认证确认", title = "如果客户端在执行授权码授予流时需要提供验证密钥质询和验证器, 默认False")
    @Column(name = "require_authorization_consent")
    private Boolean requireAuthorizationConsent = Boolean.TRUE;

    @Schema(name = "客户端JSON Web密钥集的URL", title = "客户端JSON Web密钥集的URL")
    @Column(name = "jwk_set_url", length = 1000)
    private String jwkSetUrl;

    @Schema(name = "JWT 签名算法", title = "仅在 clientAuthenticationMethods 为 private_key_jwt 和 client_secret_jwt 方法下使用")
    @Column(name = "signing_algorithm")
    @Enumerated(EnumType.ORDINAL)
    private AllJwsAlgorithm authenticationSigningAlgorithm;

    @Schema(name = "X509证书DN")
    @Column(name = "subject_dn")
    private String x509CertificateSubjectDN;
    /* --- ClientSettings End --- */


    /* --- TokenSettings Begin --- */
    @Schema(name = "授权码有效时间", title = "默认5分钟，使用 Duration 时间格式")
    @Column(name = "authorization_code_validity")
    private Duration authorizationCodeValidity = Duration.ofMinutes(5);

    @Schema(name = "激活码有效时间", title = "默认5分钟，使用 Duration 时间格式")
    @Column(name = "device_code_validity")
    private Duration deviceCodeValidity = Duration.ofMinutes(5);

    @Schema(name = "AccessToken 有效时间", title = "默认5分钟，使用 Duration 时间格式")
    @Column(name = "access_token_validity")
    private Duration accessTokenValidity = Duration.ofMinutes(5);

    @Schema(name = "RefreshToken 有效时间", title = "默认60分钟，使用 Duration 时间格式")
    @Column(name = "refresh_token_validity")
    private Duration refreshTokenValidity = Duration.ofMinutes(60);

    @Schema(name = "Access Token 格式", title = "OAuth 2.0令牌的标准数据格式")
    @Column(name = "access_token_format")
    @Enumerated(EnumType.ORDINAL)
    private TokenFormat accessTokenFormat = TokenFormat.REFERENCE;

    @Schema(name = "是否重用 Refresh Token", title = "默认值 True")
    @Column(name = "reuse_refresh_tokens")
    private Boolean reuseRefreshTokens = Boolean.TRUE;

    @Schema(name = "IdToken 签名算法", title = "JWT 算法用于签名 ID Token， 默认值 RS256")
    @Column(name = "signature_algorithm")
    @Enumerated(EnumType.ORDINAL)
    private SignatureJwsAlgorithm idTokenSignatureAlgorithmJwsAlgorithm = SignatureJwsAlgorithm.RS256;

    @Schema(name = "X509证书是否绑定 AccessToken", title = "默认值 false")
    @Column(name = "bound_access_token")
    private Boolean x509CertificateBoundAccessTokens = Boolean.FALSE;
    /* --- TokenSettings End --- */

    public abstract Set<OAuth2Scope> getScopes();

    @Override
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Boolean getRequireProofKey() {
        return requireProofKey;
    }

    public void setRequireProofKey(Boolean requireProofKey) {
        this.requireProofKey = requireProofKey;
    }

    public Boolean getRequireAuthorizationConsent() {
        return requireAuthorizationConsent;
    }

    public void setRequireAuthorizationConsent(Boolean requireAuthorizationConsent) {
        this.requireAuthorizationConsent = requireAuthorizationConsent;
    }

    public String getJwkSetUrl() {
        return jwkSetUrl;
    }

    public void setJwkSetUrl(String jwkSetUrl) {
        this.jwkSetUrl = jwkSetUrl;
    }

    public AllJwsAlgorithm getAuthenticationSigningAlgorithm() {
        return authenticationSigningAlgorithm;
    }

    public void setAuthenticationSigningAlgorithm(AllJwsAlgorithm authenticationSigningAlgorithm) {
        this.authenticationSigningAlgorithm = authenticationSigningAlgorithm;
    }

    public Duration getAuthorizationCodeValidity() {
        return authorizationCodeValidity;
    }

    public void setAuthorizationCodeValidity(Duration authorizationCodeValidity) {
        this.authorizationCodeValidity = authorizationCodeValidity;
    }

    public Duration getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public void setAccessTokenValidity(Duration accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    public Duration getDeviceCodeValidity() {
        return deviceCodeValidity;
    }

    public void setDeviceCodeValidity(Duration deviceCodeValidity) {
        this.deviceCodeValidity = deviceCodeValidity;
    }

    public Duration getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public void setRefreshTokenValidity(Duration refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public TokenFormat getAccessTokenFormat() {
        return accessTokenFormat;
    }

    public void setAccessTokenFormat(TokenFormat accessTokenFormat) {
        this.accessTokenFormat = accessTokenFormat;
    }

    public Boolean getReuseRefreshTokens() {
        return reuseRefreshTokens;
    }

    public void setReuseRefreshTokens(Boolean reuseRefreshTokens) {
        this.reuseRefreshTokens = reuseRefreshTokens;
    }

    public SignatureJwsAlgorithm getIdTokenSignatureAlgorithm() {
        return idTokenSignatureAlgorithmJwsAlgorithm;
    }

    public void setIdTokenSignatureAlgorithm(SignatureJwsAlgorithm idTokenSignatureAlgorithmJwsAlgorithm) {
        this.idTokenSignatureAlgorithmJwsAlgorithm = idTokenSignatureAlgorithmJwsAlgorithm;
    }

    public String getX509CertificateSubjectDN() {
        return x509CertificateSubjectDN;
    }

    public void setX509CertificateSubjectDN(String x509CertificateSubjectDN) {
        this.x509CertificateSubjectDN = x509CertificateSubjectDN;
    }

    public SignatureJwsAlgorithm getIdTokenSignatureAlgorithmJwsAlgorithm() {
        return idTokenSignatureAlgorithmJwsAlgorithm;
    }

    public void setIdTokenSignatureAlgorithmJwsAlgorithm(SignatureJwsAlgorithm idTokenSignatureAlgorithmJwsAlgorithm) {
        this.idTokenSignatureAlgorithmJwsAlgorithm = idTokenSignatureAlgorithmJwsAlgorithm;
    }

    public Boolean getX509CertificateBoundAccessTokens() {
        return x509CertificateBoundAccessTokens;
    }

    public void setX509CertificateBoundAccessTokens(Boolean x509CertificateBoundAccessTokens) {
        this.x509CertificateBoundAccessTokens = x509CertificateBoundAccessTokens;
    }
}
