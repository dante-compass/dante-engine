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

package org.dromara.dante.security.converter;

import org.dromara.dante.security.domain.RegisteredClientDetails;
import org.dromara.dante.security.domain.RegisteredClientTransmitter;
import org.springframework.core.convert.converter.Converter;

/**
 * <p>Description: {@link RegisteredClientTransmitter} 通用转换器定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/2/23 0:04
 */
public abstract class AbstractRegisteredClientTransmitterConverter<S extends RegisteredClientDetails> implements Converter<S, RegisteredClientTransmitter> {

    @Override
    public RegisteredClientTransmitter convert(S source) {
        RegisteredClientTransmitter target = new RegisteredClientTransmitter();
        target.setId(source.getId());
        target.setClientId(source.getClientId());
        target.setClientSecret(source.getClientSecret());
        target.setClientName(source.getClientName());
        target.setParentClientId(source.getParentClientId());
        target.setRedirectUris(source.getRedirectUris());
        target.setLogo(source.getLogo());
        return target;
    }
}
