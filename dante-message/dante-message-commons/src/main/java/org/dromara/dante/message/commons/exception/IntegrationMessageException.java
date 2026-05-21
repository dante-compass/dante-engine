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

package org.dromara.dante.message.commons.exception;

import org.dromara.dante.core.domain.Feedback;
import org.dromara.dante.core.exception.PlatformRuntimeException;
import org.dromara.dante.message.commons.constant.MessageErrorCodes;

/**
 * <p>Description: Integration 消息异常 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/6/16 10:51
 */
public class IntegrationMessageException extends PlatformRuntimeException {

    public IntegrationMessageException() {
        super();
    }

    public IntegrationMessageException(Throwable cause) {
        super(cause);
    }

    public IntegrationMessageException(String message) {
        super(message);
    }

    public IntegrationMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public Feedback getFeedback() {
        return MessageErrorCodes.INTEGRATION_MESSAGE_EXCEPTION;
    }
}
