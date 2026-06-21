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

package cn.herodotus.dante.autoconfigure.jackson;

import cn.herodotus.dante.autoconfigure.jackson.initializer.JacksonInitializer;
import cn.herodotus.dante.spring.context.ServiceContextHolder;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: Jackson 配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/6/1 0:09
 */
@AutoConfiguration
public class JacksonAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(JacksonAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [Jackson] Configure.");
    }

    @Bean
    public JsonMapperBuilderCustomizer defaultJsonMapperBuilderCustomizer() {
        JacksonDefaultObjectMapperBuilderCustomizer customizer = new JacksonDefaultObjectMapperBuilderCustomizer();
        log.debug("[Herodotus] |- Strategy [Jackson Default JsonMapper Builder Customizer] Configure.");
        return customizer;
    }

    /**
     * 注意：这里必须要使用 {@link ComponentScan} 的方式来扫描 {@link JacksonInitializer} 。这种方式会让 {@link JacksonAutoConfiguration} 在启动时优先配置。
     * <p>
     * 如果要换成用 @Bean 标注方法的方式，
     * 1. 将会直接导致初始化 Jackson 相关配置初始化延后，
     * 2. 间接导致 {@link ServiceContextHolder} 初始化延后使得 {@link ServiceContextHolder#getTokenIntrospectionUri()} 为空
     * 3. 最终会引起系统启动抛错
     * <p>
     * 目测 @ComponentScan 注解的优先级比 @Configuration 中 @Bean 的优先级更高。
     */
    @Configuration(proxyBeanMethods = false)
    @ComponentScan({
            "cn.herodotus.dante.autoconfigure.jackson.initializer"
    })
    static class JacksonUtilsConfiguration {

    }
}
