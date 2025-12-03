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

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.type.TypeFactory;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @author gengwei.zheng
 */
public class JacksonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JacksonUtils.class);

    private static volatile JacksonUtils instance;

    private ObjectMapper objectMapper;

    private JacksonUtils() {
        this.objectMapper = JsonMapper.builder()
                .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
                .enable(JsonReadFeature.ALLOW_SINGLE_QUOTES)
                .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
                .build();
    }

    public static JacksonUtils getInstance() {
        if (ObjectUtils.isEmpty(instance)) {
            synchronized (JacksonUtils.class) {
                if (ObjectUtils.isEmpty(instance)) {
                    instance = new JacksonUtils();
                }
            }
        }

        return instance;
    }

    private ObjectMapper objectMapper() {
        return objectMapper;
    }

    private void objectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static ObjectMapper getObjectMapper() {
        return getInstance().objectMapper();
    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        getInstance().objectMapper(objectMapper);
    }

    public static <T> String toJson(T domain) {
        try {
            return getObjectMapper().writeValueAsString(domain);
        } catch (JacksonException e) {
            logger.error("[Herodotus] |- Jackson json processing error, when to json! {}", e.getMessage());
            return null;
        }
    }

    public static TypeFactory getTypeFactory() {
        return getObjectMapper().getTypeFactory();
    }

    public static <T> T toObject(Map<String, Object> content, Class<T> valueType) {
        try {
            return getObjectMapper().convertValue(content, valueType);
        } catch (IllegalArgumentException e) {
            logger.error("[Herodotus] |- Jackson toObject use STRING with valueType processing error! {}", e.getMessage());
            return null;
        }
    }

    public static <T> T toObject(String content, Class<T> valueType) {
        try {
            return getObjectMapper().readValue(content, valueType);
        } catch (JacksonException e) {
            logger.error("[Herodotus] |- Jackson json processing error, when to object with value type! {}", e.getMessage());
            return null;
        }
    }

    public static <T> T toObject(String content, TypeReference<T> typeReference) {
        try {
            return getObjectMapper().readValue(content, typeReference);
        } catch (JacksonException e) {
            logger.error("[Herodotus] |- Jackson toObject use STRING with typeReference processing error! {}", e.getMessage());
            return null;
        }
    }

    public static <T> T toObject(String content, JavaType javaType) {
        try {
            return getObjectMapper().readValue(content, javaType);
        } catch (JacksonException e) {
            logger.error("[Herodotus] |- Jackson toObject use STRING with javaType processing error! {}", e.getMessage());
            return null;
        }
    }

    public static <T> T toObject(byte[] bytes, Class<T> valueType) {
        try {
            return getObjectMapper().readValue(bytes, valueType);
        } catch (JacksonException e) {
            logger.error("[Herodotus] |- Jackson toObject use BYTE_ARRAY with class processing catch IO error {}.", e.getMessage());
            return null;
        }
    }

    public static <T> T toObject(byte[] bytes, TypeReference<T> typeReference) {
        try {
            return getObjectMapper().readValue(bytes, typeReference);
        } catch (JacksonException e) {
            logger.error("[Herodotus] |- Jackson toObject use BYTE_ARRAY with typeReference processing catch IO error {}.", e.getMessage());
            return null;
        }
    }

    public static <T> T toObject(byte[] bytes, JavaType javaType) {
        try {
            return getObjectMapper().readValue(bytes, javaType);
        } catch (JacksonException e) {
            logger.error("[Herodotus] |- Jackson toObject use BYTE_ARRAY with javaType processing catch IO error {}.", e.getMessage());
            return null;
        }
    }

    public static <T> T toObject(InputStream inputStream, Class<T> valueType) {
        try {
            return getObjectMapper().readValue(inputStream, valueType);
        } catch (JacksonException e) {
            logger.error("[Herodotus] |- Jackson toObject use INPUT_STREAM with class processing catch IO error {}.", e.getMessage());
            return null;
        }
    }

    public static <T> T toObject(InputStream inputStream, TypeReference<T> typeReference) {
        try {
            return getObjectMapper().readValue(inputStream, typeReference);
        } catch (JacksonException e) {
            logger.error("[Herodotus] |- Jackson toObject use INPUT_STREAM with typeReference processing catch IO error {}.", e.getMessage());
            return null;
        }
    }

    public static <T> T toObject(InputStream inputStream, JavaType javaType) {
        try {
            return getObjectMapper().readValue(inputStream, javaType);
        } catch (JacksonException e) {
            logger.error("[Herodotus] |- Jackson toObject use INPUT_STREAM with javaType processing catch IO error {}.", e.getMessage());
            return null;
        }
    }

    public static <T> T toObject(JsonNode jsonNode, Class<T> valueType) {
        return getObjectMapper().convertValue(jsonNode, valueType);
    }

    public static <T> T toObject(JsonNode jsonNode, TypeReference<T> typeReference) {
        return getObjectMapper().convertValue(jsonNode, typeReference);
    }

    public static <T> T toObject(JsonNode jsonNode, JavaType javaType) {
        return getObjectMapper().convertValue(jsonNode, javaType);
    }

    public static <T> List<T> toList(String content, Class<T> clazz) {
        JavaType javaType = getObjectMapper().getTypeFactory().constructParametricType(List.class, clazz);
        return toObject(content, javaType);
    }

    public static <K, V> Map<K, V> toMap(String content, Class<K> keyClass, Class<V> valueClass) {
        JavaType javaType = getObjectMapper().getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
        return toObject(content, javaType);
    }

    public static Map<String, Object> toMap(String content) {
        return toMap(content, String.class, Object.class);
    }

    public static <T> Set<T> toSet(String content, Class<T> clazz) {
        JavaType javaType = getTypeFactory().constructCollectionLikeType(Set.class, clazz);
        return toObject(content, javaType);
    }

    public static <T> T[] toArray(String content, Class<T> clazz) {
        JavaType javaType = getTypeFactory().constructArrayType(clazz);
        return toObject(content, javaType);
    }

    public static <T> T[] toArray(String content) {
        return toObject(content, new TypeReference<>() {
        });
    }

    public static JsonNode toNode(String content) {
        try {
            return getObjectMapper().readTree(content);
        } catch (JacksonException e) {
            logger.error("[Herodotus] |- Jackson toNode use STRING processing catch json error {}.", e.getMessage());
            return null;
        }
    }

    public static JsonNode toNode(byte[] bytes) {
        try {
            return getObjectMapper().readTree(bytes);
        } catch (JacksonException e) {
            logger.error("[Herodotus] |- Jackson toNode use STRING processing catch io error {}.", e.getMessage());
            return null;
        }
    }

    public static JsonNode toNode(JsonParser jsonParser) {
        try {
            return getObjectMapper().readTree(jsonParser);
        } catch (JacksonException e) {
            logger.error("[Herodotus] |- Jackson toNode use JsonParser processing catch io error {}.", e.getMessage());
            return null;
        }
    }

    public static JsonParser createParser(String content) {
        try {
            return getObjectMapper().createParser(content);
        } catch (JacksonException e) {
            logger.error("[Herodotus] |- Jackson io error, when create parser! {}", e.getMessage());
            return null;
        }
    }

    public static <R> R loop(JsonNode jsonNode, Function<JsonNode, R> function) {

        if (jsonNode.isObject()) {
            Set<Map.Entry<String, JsonNode>> it = jsonNode.properties();

            for (Map.Entry<String, JsonNode> entry : it) {
                loop(entry.getValue(), function);
            }
        }

        if (jsonNode.isArray()) {
            for (JsonNode node : jsonNode) {
                loop(node, function);
            }
        }

        if (jsonNode.isValueNode()) {
            return function.apply(jsonNode);
        } else {
            return null;
        }
    }
}
