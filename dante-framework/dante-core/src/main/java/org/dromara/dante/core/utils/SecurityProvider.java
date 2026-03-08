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

package org.dromara.dante.core.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.dromara.dante.core.exception.KeyPairGenerationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.spec.RSAKeyGenParameterSpec;

/**
 * <p>Description: 安全相关工具类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2026/1/1 16:29
 */
public class SecurityProvider {

    private static final Logger log = LoggerFactory.getLogger(SecurityProvider.class);

    private static final String SIGNATURE_ALGORITHM_SHA256_RSA = "SHA256withRSA";

    private static final String PROVIDER_NAME = BouncyCastleProvider.PROVIDER_NAME;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String providerName() {
        return PROVIDER_NAME;
    }

    public static String signatureAlgorithm() {
        return SIGNATURE_ALGORITHM_SHA256_RSA;
    }


    /**
     * 字符串类型密码转成 Char[] 类型
     * <p>
     * 提取个公共方法，方便统一修改
     *
     * @param password 密码
     * @return char 数组类型密码
     */
    public static char[] toChars(String password) {
        return password.toCharArray();
    }

    /**
     * 创建 RSA 秘钥对
     *
     * @return 秘钥对 {@link KeyPair}
     */
    public static KeyPair rsaKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", providerName());
            keyPairGenerator.initialize(new RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4));
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchProviderException | NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            log.error("[Herodotus] |- RSA keyPair generate catch [{}] error.", e.getMessage(), e);
            throw new KeyPairGenerationException(e);
        }
    }
}
