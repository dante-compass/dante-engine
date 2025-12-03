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

package cn.herodotus.dante.oauth2.persistence.sas.jpa.jackson;

import cn.herodotus.dante.security.jackson.HerodotusSecurityJacksonModules;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: OAuth2 相关 Jackson 处理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/4/29 16:05
 */
public class OAuth2JacksonProcessor {

    private static final Logger log = LoggerFactory.getLogger(OAuth2JacksonProcessor.class);

    private ObjectMapper objectMapper;

    private OAuth2JacksonProcessor() {
    }

    public static Builder builder() {
        return new Builder();
    }

    private void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public Map<String, Object> parseMap(String data) {
        try {
            return this.objectMapper.readValue(data, new TypeReference<>() {
            });
        } catch (Exception ex) {
            log.error("[Herodotus] |- OAuth2 jackson processing parseMap catch error {}", ex.getMessage());
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    public String writeMap(Map<String, Object> data) {
        try {
            return this.objectMapper.writeValueAsString(data);
        } catch (Exception ex) {
            log.error("[Herodotus] |- OAuth2 jackson processing writeMap catch error {}", ex.getMessage());
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    public static class Builder {
        private ObjectMapper objectMapper;
        private Class<?> target;
        private Class<?> mixinSource;
        private final List<JacksonModule> modules = new ArrayList<>();

        protected Builder() {
        }

        public Builder mixIn(Class<?> target, Class<?> mixinSource) {
            this.target = target;
            this.mixinSource = mixinSource;
            return this;
        }

        public Builder module(JacksonModule module) {
            this.modules.add(module);
            return this;
        }

        public OAuth2JacksonProcessor build() {
            ClassLoader classLoader = OAuth2JacksonProcessor.class.getClassLoader();
            List<JacksonModule> securityModules = HerodotusSecurityJacksonModules.getModules(classLoader);

            JsonMapper.Builder builder = JsonMapper.builder();
            builder.addModules(securityModules);

            if (ObjectUtils.isNotEmpty(target) && ObjectUtils.isNotEmpty(mixinSource)) {
                builder.addMixIn(target, mixinSource);
            }

            builder.addModules(this.modules);

            OAuth2JacksonProcessor processor = new OAuth2JacksonProcessor();
            processor.setObjectMapper(builder.build());
            return processor;
        }
    }
}
