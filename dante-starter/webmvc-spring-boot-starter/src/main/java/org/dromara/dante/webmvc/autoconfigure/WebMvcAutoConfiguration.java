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

package org.dromara.dante.webmvc.autoconfigure;

import jakarta.annotation.PostConstruct;
import org.dromara.dante.core.constant.SystemConstants;
import org.dromara.dante.web.autoconfigure.WebAutoConfiguration;
import org.dromara.dante.webmvc.autoconfigure.config.SecureConfiguration;
import org.dromara.dante.webmvc.autoconfigure.config.TemplateConfiguration;
import org.dromara.dante.webmvc.autoconfigure.config.TenantConfiguration;
import org.dromara.dante.webmvc.autoconfigure.secure.AccessLimitedInterceptor;
import org.dromara.dante.webmvc.autoconfigure.secure.IdempotentInterceptor;
import org.dromara.dante.webmvc.autoconfigure.tenant.MultiTenantInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.LiteWebJarsResourceResolver;

/**
 * <p>Description: WebMVC 自动配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/4/10 9:29
 */
@AutoConfiguration
@Import({
        WebAutoConfiguration.class,
        SecureConfiguration.class,
        TenantConfiguration.class,
        TemplateConfiguration.class,
})
@EnableWebMvc
public class WebMvcAutoConfiguration implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebMvcAutoConfiguration.class);

    private final ObjectProvider<IdempotentInterceptor> idempotentInterceptor;
    private final ObjectProvider<AccessLimitedInterceptor> accessLimitedInterceptor;
    private final ObjectProvider<MultiTenantInterceptor> multiTenantInterceptor;

    public WebMvcAutoConfiguration(ObjectProvider<IdempotentInterceptor> idempotentInterceptor, ObjectProvider<AccessLimitedInterceptor> accessLimitedInterceptor, ObjectProvider<MultiTenantInterceptor> multiTenantInterceptor) {
        this.idempotentInterceptor = idempotentInterceptor;
        this.accessLimitedInterceptor = accessLimitedInterceptor;
        this.multiTenantInterceptor = multiTenantInterceptor;
    }


    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Starter [Web Servlet] Configure.");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        accessLimitedInterceptor.ifAvailable(registry::addInterceptor);
        idempotentInterceptor.ifAvailable(registry::addInterceptor);
        multiTenantInterceptor.ifAvailable(registry::addInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(SystemConstants.MATCHER_STATIC).addResourceLocations("classpath:/static/");
        registry.addResourceHandler(SystemConstants.MATCHER_WEBJARS)
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .resourceChain(false)
                .addResolver(new LiteWebJarsResourceResolver());
    }
}
