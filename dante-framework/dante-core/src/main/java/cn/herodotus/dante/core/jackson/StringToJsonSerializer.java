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
