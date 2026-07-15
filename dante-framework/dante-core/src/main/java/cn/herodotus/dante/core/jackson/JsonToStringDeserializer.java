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

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;

/**
 * <p>Description: 标准 JSON 格式数据反序列化 String 类型 JSON 数据反序列化器 </p>
 * <p>
 * 应用场景通常为 REST API 接收的数据中，某个字段的类型为 JSON（可以理解为 JS 中的 Object 类型），后端与该数据对应的实体属性为 String 类型。
 * 通过 {@link JsonToStringDeserializer} 可以将 JSON 反序列化为不包含 "\" 的字符串，方便后端将其存储在数据库中。
 * <p>
 * 反序列化器 {@link JsonToStringDeserializer} 与 序列化器 {@link StringToJsonSerializer} 作用正好相反。通常配对使用。
 *
 * @author : gengwei.zheng
 * @date : 2024/9/21 4:30
 */
public class JsonToStringDeserializer extends StdDeserializer<String> {

    protected JsonToStringDeserializer() {
        super(String.class);
    }

    @Override
    public String deserialize(JsonParser parser, DeserializationContext context) throws JacksonException {
        JsonNode jsonNode = context.readTree(parser);
        return jsonNode.toString();
    }
}
