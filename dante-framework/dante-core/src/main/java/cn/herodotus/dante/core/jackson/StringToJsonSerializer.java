/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2020-2030 郑庚伟 ZHENGGENGWEI (码匠君), <herodotus@aliyun.com> Licensed under the AGPL License
 *
 * This file is part of Herodotus ThingsMesh.
 *
 * Herodotus ThingsMesh is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Herodotus ThingsMesh is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.herodotus.cn>.
 */

package cn.herodotus.dante.core.jackson;

import org.apache.commons.lang3.StringUtils;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

/**
 * <p>Description: String 类型 JSON 数据序列化为标准 JSON 格式序列化器</p>
 * <p>
 * 后端实体属性为 String 类型，设定为存储 String 类型的 JSON 数据。将该实体对应数据通过 REST API 返回给前端时，需要将 String 类型的 JSON 转换为标准 JSON 格式（可以理解为 JS 中的 Object 类型），
 * 通过 {@link StringToJsonSerializer} 可以将 String 类型的 JSON 数据序列化为标准 JSON 格式。即前端接收到数据时，该字段不在是 String 而是变成 JS Object。
 * 这避免了如果直接传递 String类型 JSON 数据，接收到的 JSON 字符串中会包含 "\"
 * <p>
 * 序列化器 {@link StringToJsonSerializer} 与 反序列化器 {@link JsonToStringDeserializer} 作用正好相反。通常配对使用。
 *
 * @author : gengwei.zheng
 * @date : 2024/9/21 15:15
 */
public class StringToJsonSerializer extends StdSerializer<String> {

    protected StringToJsonSerializer() {
        super(String.class);
    }

    @Override
    public void serialize(String value, JsonGenerator generator, SerializationContext context) throws JacksonException {
        if (StringUtils.isBlank(value)) {
            generator.writeNull();
        } else {
            generator.writeRawValue(value);
        }
    }
}
