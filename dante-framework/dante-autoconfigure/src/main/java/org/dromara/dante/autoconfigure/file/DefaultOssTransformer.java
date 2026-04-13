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

package org.dromara.dante.autoconfigure.file;

import org.dromara.dante.core.support.file.OssTransformer;

import java.nio.file.Path;

/**
 * <p>Description: 默认的 FileTransformer </p>
 * <p>
 * 定义该类主要为了实现一种默认的 FileTransformer 的定义，方便 FileTransformer 的注入。
 *
 * @author : gengwei.zheng
 * @date : 2025/1/11 23:48
 */
public class DefaultOssTransformer implements OssTransformer {

    @Override
    public boolean upload(String bucketName, Path path) {
        return true;
    }

    @Override
    public boolean download(String bucketName, Path path) {
        return true;
    }

    @Override
    public boolean remove(String bucketName, String fileName) {
        return true;
    }
}
