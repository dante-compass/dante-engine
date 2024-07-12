/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2020-2030 郑庚伟 ZHENGGENGWEI (码匠君), <herodotus@aliyun.com> Licensed under the AGPL License
 *
 * This file is part of Herodotus Engine.
 *
 * Herodotus Engine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Herodotus Engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.herodotus.cn>.
 */

package cn.herodotus.engine.access.all.customizer;

import cn.herodotus.engine.access.core.constants.AccessErrorCodes;
import cn.herodotus.engine.assistant.definition.constants.ErrorCodeMapperBuilderOrdered;
import cn.herodotus.engine.assistant.definition.function.ErrorCodeMapperBuilderCustomizer;
import cn.herodotus.engine.assistant.definition.support.ErrorCodeMapperBuilder;
import org.springframework.core.Ordered;

/**
 * <p>Description: Access 错误代码映射定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/9/26 23:35
 */
public class AccessErrorCodeMapperBuilderCustomizer implements ErrorCodeMapperBuilderCustomizer, Ordered {
    @Override
    public void customize(ErrorCodeMapperBuilder builder) {
        builder.preconditionFailed(
                AccessErrorCodes.ACCESS_CONFIG_ERROR,
                AccessErrorCodes.ACCESS_HANDLER_NOT_FOUND,
                AccessErrorCodes.ACCESS_IDENTITY_VERIFICATION_FAILED,
                AccessErrorCodes.ACCESS_PRE_PROCESS_FAILED_EXCEPTION,
                AccessErrorCodes.ILLEGAL_ACCESS_ARGUMENT,
                AccessErrorCodes.ILLEGAL_ACCESS_SOURCE
        );
    }

    @Override
    public int getOrder() {
        return ErrorCodeMapperBuilderOrdered.ACCESS;
    }
}
