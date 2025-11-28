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

package cn.herodotus.dante.webmvc.autoconfigure;

import cn.herodotus.engine.message.core.definition.strategy.RestMappingScanEventManager;
import cn.herodotus.engine.web.core.condition.ConditionalOnRestScanEnabled;
import cn.herodotus.engine.web.service.properties.ServiceProperties;
import cn.herodotus.engine.web.servlet.initializer.RestMappingScanner;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: 接口扫描配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/16 18:40
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RestMappingScanEventManager.class)
@ConditionalOnRestScanEnabled
@EnableConfigurationProperties(ServiceProperties.class)
public class WebMvcRestMappingScanConfiguration {

    private static final Logger log = LoggerFactory.getLogger(WebMvcRestMappingScanConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [Servlet Rest Mapping Scan] Configure.");
    }

    @Bean
    @ConditionalOnBean(RestMappingScanEventManager.class)
    @ConditionalOnMissingBean
    public RestMappingScanner restMappingScanner(ServiceProperties serviceProperties, RestMappingScanEventManager requestMappingScanManager) {
        RestMappingScanner scanner = new RestMappingScanner(serviceProperties.getScan(), requestMappingScanManager);
        log.trace("[Herodotus] |- Bean [Servlet Rest Mapping Scanner] Configure.");
        return scanner;
    }
}
