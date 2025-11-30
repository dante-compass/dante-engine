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

package cn.herodotus.dante.core.support.crypto;

import cn.herodotus.dante.core.domain.SecretKey;

import java.time.Duration;

/**
 * <p>Description: 数字信封处理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/11/29 21:54
 */
public interface DigitalEnvelopeProcessor {

    /**
     * 数字信封加密
     * @param identity 身份标识
     * @param content 待加密内容
     * @return 加密后内容
     */
    String encrypt(String identity, String content);

    /**
     * 数字信封解密
     * @param identity 身份标识
     * @param content 待解密内容
     * @return 解密后内容
     */
    String decrypt(String identity, String content);

    /**
     * 根据SessionId创建SecretKey {@link SecretKey}。如果前端有可以唯一确定的SessionId，并且使用该值，则用该值创建SecretKey。否则就由后端动态生成一个SessionId。
     *
     * @param identity                   SessionId，可以为空。
     * @param accessTokenValiditySeconds Session过期时间，单位秒
     * @return {@link SecretKey}
     */
    SecretKey createSecretKey(String identity, Duration accessTokenValiditySeconds);

    /**
     * 前端获取后端生成 AES Key
     *
     * @param identity     Session ID
     * @param confidential 前端和后端加解密结果都
     * @return 前端 PublicKey 加密后的 AES KEY
     */
    String exchange(String identity, String confidential);
}
