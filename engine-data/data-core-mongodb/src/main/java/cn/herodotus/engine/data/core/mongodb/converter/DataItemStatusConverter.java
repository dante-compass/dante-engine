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
 * along with this program.  If not, see <https://www.herodotus.vip>.
 */

package cn.herodotus.engine.data.core.mongodb.converter;

import cn.herodotus.engine.data.core.enums.DataItemStatus;
import org.springframework.data.convert.PropertyValueConverter;
import org.springframework.data.mongodb.core.convert.MongoConversionContext;

/**
 * <p>Description: {@link DataItemStatus} 转换器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/3/31 15:27
 */
public class DataItemStatusConverter implements PropertyValueConverter<DataItemStatus, Integer, MongoConversionContext> {

    @Override
    public DataItemStatus read(Integer value, MongoConversionContext context) {
        return DataItemStatus.get(value);
    }

    @Override
    public Integer write(DataItemStatus value, MongoConversionContext context) {
        return value.ordinal();
    }
}
