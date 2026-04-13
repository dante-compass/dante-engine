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

import org.dromara.dante.core.constant.SystemConstants;
import org.dromara.dante.core.support.file.CompositeFileManager;
import org.dromara.dante.core.support.file.FileTemplate;
import org.dromara.dante.core.support.file.OssTransformer;

/**
 * <p>Description:  </p>
 *
 * @author : gengwei.zheng
 * @date : 2026/1/5 17:16
 */
public class DefaultCompositeFileManager implements CompositeFileManager {

    private final FileTemplate fileTemplate;
    private final OssTransformer ossTransformer;

    public DefaultCompositeFileManager(FileTemplate fileTemplate, OssTransformer ossTransformer) {
        this.fileTemplate = fileTemplate;
        this.ossTransformer = ossTransformer;
    }

    @Override
    public OssTransformer getOssTransformer() {
        return ossTransformer;
    }

    @Override
    public FileTemplate getFileTemplate() {
        return fileTemplate;
    }

    @Override
    public String getDefaultDirectory() {
        return "herodotus";
    }

    @Override
    public String getDefaultBucketName() {
        return SystemConstants.DEFAULT_BUCKET_NAME;
    }
}
