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
