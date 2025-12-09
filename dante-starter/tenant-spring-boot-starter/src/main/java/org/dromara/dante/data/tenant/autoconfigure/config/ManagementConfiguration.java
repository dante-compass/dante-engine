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

package org.dromara.dante.data.tenant.autoconfigure.config;

import org.dromara.dante.data.tenant.autoconfigure.constant.TenantConstants;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * <p>Description: 数据源管理端配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/11/28 0:05
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBooleanProperty(prefix = TenantConstants.PROPERTY_PREFIX_MULTI_TENANT, name = "management")
@EntityScan(basePackages = {
        "org.dromara.dante.data.tenant.autoconfigure.entity",
})
@EnableJpaRepositories(basePackages = {
        "org.dromara.dante.data.tenant.autoconfigure.repository",
})
@ComponentScan(basePackages = {
        "org.dromara.dante.data.tenant.autoconfigure.service",
        "org.dromara.dante.data.tenant.autoconfigure.controller",
})
class ManagementConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ManagementConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- Module [Tenant DataSource Management] Configure.");
    }
}
