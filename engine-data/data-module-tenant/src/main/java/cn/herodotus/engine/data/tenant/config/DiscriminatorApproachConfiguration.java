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

package cn.herodotus.engine.data.tenant.config;

import cn.herodotus.engine.data.hibernate.tenant.HerodotusTenantIdentifierResolver;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: 共享数据库，独立Schema，共享数据表多租户配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/3/28 22:26
 */
@Configuration(proxyBeanMethods = false)
class DiscriminatorApproachConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DiscriminatorApproachConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- Module [Discriminator Approach] Configure.");
    }

    @Bean
    public HibernatePropertiesCustomizer herodotusTenantIdentifierResolver() {
        HerodotusTenantIdentifierResolver resolver = new HerodotusTenantIdentifierResolver();
        log.trace("[Herodotus] |- Bean [Current Tenant Identifier Resolver] Configure.");
        return resolver;
    }
}
