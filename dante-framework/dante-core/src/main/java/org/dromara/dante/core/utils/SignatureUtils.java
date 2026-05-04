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

import cn.hutool.v7.crypto.SecureUtil;
import org.apache.commons.lang3.Strings;
import org.dromara.dante.core.constant.SymbolConstants;
import org.dromara.dante.core.domain.SignatureValidationResult;
import org.dromara.dante.core.enums.SignatureMethod;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * <p>Description: 签名算法工具类 </p>
 * <p>
 * 参考阿里云物联网 Mqtt 签名算法逻辑实现
 *
 * @author : gengwei.zheng
 * @date : 2025/9/22 12:32
 */
public class SignatureUtils {

    /**
     * hmacmd5 签名算法
     *
     * @param key     签名密钥
     * @param content 签名内容
     * @return 使用 Hex 转换后的 16制字符串
     */
    private static String hexHmacMd5(String key, String content) {
        return SecureUtil.hmacMd5(key).digestHex(content);
    }

    /**
     * hexHmacSha1 签名算法
     *
     * @param key     签名密钥
     * @param content 签名内容
     * @return 使用 Hex 转换后的 16制字符串
     */
    private static String hexHmacSha1(String key, String content) {
        return SecureUtil.hmacSha1(key).digestHex(content);
    }

    /**
     * hexHmacSha256 签名算法
     *
     * @param key     签名密钥
     * @param content 签名内容
     * @return 使用 Hex 转换后的 16制字符串
     */
    private static String hexHmacSha256(String key, String content) {
        return SecureUtil.hmacSha256(key).digestHex(content);
    }

    /**
     * 生成签名
     *
     * @param key     签名密钥
     * @param content 签名内容
     * @param method  签名算法 {@link SignatureMethod}
     * @return 签名
     */
    public static String signature(String key, String content, SignatureMethod method) {
        return switch (method) {
            case HMAC_SHA1 -> hexHmacSha1(key, content);
            case HMAC_SHA256 -> hexHmacSha256(key, content);
            default -> hexHmacMd5(key, content);
        };
    }

    /**
     * 生成参数数据。按照参数名称首字母字典排序， 然后将参数值依次拼接
     *
     * @param contents 签名内容 {@link Map}
     * @return 参数拼接后生成的 content 内容
     */
    public static String content(Map<String, String> contents) {
        SortedMap<String, Object> sortedMap = new TreeMap<>(contents);
        return sortedMap.entrySet().stream()
                .map(entry -> entry.getKey() + entry.getValue())
                .collect(Collectors.joining(SymbolConstants.BLANK));
    }

    /**
     * 按照指定的签名算法，根据参数内容生成签名
     *
     * @param key      签名密钥
     * @param method   签名算法 {@link SignatureMethod}
     * @param contents 签名内容 {@link Map}
     * @return 签名字符串
     */
    public static String generate(String key, SignatureMethod method, Map<String, String> contents) {
        String content = content(contents);
        return signature(key, content, method);
    }

    /**
     * 默认使用 {@link SignatureMethod#HMAC_MD5} 签名算法，根据参数内容生成签名
     *
     * @param key      签名密钥
     * @param contents 签名内容 {@link Map}
     * @return 签名字符串
     */
    public static String generate(String key, Map<String, String> contents) {
        return generate(key, SignatureMethod.HMAC_MD5, contents);
    }

    /**
     * 签名验证
     *
     * @param key       签名密钥
     * @param method    签名算法 {@link SignatureMethod}
     * @param content   参数 {@link Map}
     * @param signature 待验证签名
     * @return 验证结果 {@link SignatureValidationResult}
     */
    public static SignatureValidationResult validate(String key, SignatureMethod method, Map<String, String> content, String signature) {
        String newSignature = generate(key, method, content);
        if (Strings.CS.equals(signature, newSignature)) {
            return SignatureValidationResult.valid();
        } else {
            return SignatureValidationResult.invalid("Signature validation failed");
        }
    }
}
