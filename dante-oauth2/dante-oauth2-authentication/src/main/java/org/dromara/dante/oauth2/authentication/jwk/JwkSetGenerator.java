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

package org.dromara.dante.oauth2.authentication.jwk;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.dante.core.utils.SecurityProvider;
import org.dromara.dante.oauth2.commons.properties.OAuth2AuthenticationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;

import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * <p>Description: JwtSet 生成器 </p>
 * <p>
 * 用于 Spring Security SAS 认证服务 JwtSet 的构建
 *
 * @author : gengwei.zheng
 * @date : 2026/3/7 22:45
 */
public class JwkSetGenerator {

    private static final Logger log = LoggerFactory.getLogger(JwkSetGenerator.class);

    private final OAuth2AuthenticationProperties authenticationProperties;
    private final SslBundles sslBundles;

    public JwkSetGenerator(OAuth2AuthenticationProperties authenticationProperties, SslBundles sslBundles) {
        this.authenticationProperties = authenticationProperties;
        this.sslBundles = sslBundles;
    }

    /**
     * 使用默认的 {@link RSAKey} 生成策略生成 RSAKey
     *
     * @return {@link RSAKey}
     */
    private RSAKey useDefaultRSAKeyStrategy() {
        KeyPair keyPair = SecurityProvider.rsaKeyPair();
        log.debug("[Herodotus] |- Use default strategy generate RSA key.");
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    private RSAKey createRSAKey(SslBundle sslBundle) {
        KeyStore keyStore = sslBundle.getStores().getKeyStore();
        String alias = sslBundle.getKey().getAlias();
        String password = sslBundle.getKey().getPassword();
        if (ObjectUtils.isNotEmpty(keyStore) && StringUtils.isNotEmpty(alias) && StringUtils.isNotEmpty(password)) {
            try {
                Certificate certificate = keyStore.getCertificate(alias);

                log.debug("[Herodotus] |- Use spring standard ssl bundle generate RSA key.");

                return new RSAKey.Builder((RSAPublicKey) certificate.getPublicKey())
                        .keyUse(KeyUse.SIGNATURE)
                        .keyID(UUID.randomUUID().toString())
                        .keyStore(keyStore)
                        .privateKey((RSAPrivateKey) keyStore.getKey(alias, SecurityProvider.toChars(password)))
                        .x509CertChain(Collections.singletonList(Base64.encode(certificate.getEncoded())))
                        .build();
            } catch (CertificateEncodingException | UnrecoverableKeyException | NoSuchAlgorithmException |
                     KeyStoreException e) {
                log.error("[Herodotus] |- Generate jwk set use SslBundle catch error.", e);
                return null;
            }
        }
        return null;
    }

    /**
     * 使用 Spring 标准 SSl 配置信息生成 {@link RSAKey}
     *
     * @param sslBundleName SSL Bundle 名称
     * @param sslBundles    SSL Bundle 配置
     * @return RSA Key {@link RSAKey} 或者 null
     */
    private RSAKey useStandardRSAKeyStrategy(String sslBundleName, SslBundles sslBundles) {
        return Optional.ofNullable(sslBundles)
                .filter(bundles -> CollectionUtils.isNotEmpty(bundles.getBundleNames()) && bundles.getBundleNames().contains(sslBundleName))
                .map(bundles -> bundles.getBundle(sslBundleName))
                .map(this::createRSAKey)
                .orElse(null);
    }

    /**
     * 根据自定义配置或者标准的 Spring SSL bundle 配置生成 {@link JWKSet}
     * <p>
     * 如果没有配置自定义配置或者标准的 Spring SSL bundle，或者相关配置读取失败，则采用默认生成逻辑进行兜底
     *
     * @return {@link JWKSet}
     */
    public JWKSet generate() {
        RSAKey rsaKey = null;

        if (ObjectUtils.isNotEmpty(sslBundles) && ObjectUtils.isNotEmpty(authenticationProperties.getSslBundleProvider())) {
            rsaKey = useStandardRSAKeyStrategy(authenticationProperties.getSslBundleProvider(), sslBundles);
        }

        // 如果标准 Spring SSL 读取证书内容失败，则使用默认方式提供证书内容
        if (ObjectUtils.isEmpty(rsaKey)) {
            rsaKey = useDefaultRSAKeyStrategy();
        }

        return new JWKSet(rsaKey);
    }
}
