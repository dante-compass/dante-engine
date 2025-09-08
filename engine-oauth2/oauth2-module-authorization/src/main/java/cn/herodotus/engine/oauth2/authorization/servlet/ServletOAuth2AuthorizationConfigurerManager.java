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

package cn.herodotus.engine.oauth2.authorization.servlet;

import cn.herodotus.engine.core.autoconfigure.oauth2.OAuth2AuthorizationProperties;
import cn.herodotus.engine.core.autoconfigure.oauth2.servlet.ServletOAuth2ResourceMatcherConfigurer;
import cn.herodotus.engine.web.core.servlet.template.ThymeleafTemplateHandler;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

/**
 * <p>Description: 资源服务器通用 Bean 配置器 </p>
 * <p>
 * 重新嵌套一层 Bean，将资源服务器中通用的 Bean 进行封装。减少使用时重复注入相同的内容，方便使用。
 *
 * @author : gengwei.zheng
 * @date : 2025/3/8 0:50
 */
public class ServletOAuth2AuthorizationConfigurerManager {

    private final OAuth2ResourceServerConfigurerCustomer oauth2ResourceServerConfigurerCustomer;
    private final OAuth2SessionManagementConfigurerCustomer oauth2SessionManagementConfigurerCustomer;
    private final OAuth2AuthorizeHttpRequestsConfigurerCustomer oauth2AuthorizeHttpRequestsConfigurerCustomer;
    private final OAuth2ExceptionHandlingConfigurerCustomizer oauth2ExceptionHandlingConfigurerCustomizer;

    public ServletOAuth2AuthorizationConfigurerManager(
            ThymeleafTemplateHandler thymeleafTemplateHandler,
            OAuth2AuthorizationProperties oauth2AuthorizationProperties,
            JwtDecoder jwtDecoder,
            OpaqueTokenIntrospector opaqueTokenIntrospector,
            OAuth2SessionManagementConfigurerCustomer oauth2SessionManagementConfigurerCustomer,
            ServletOAuth2ResourceMatcherConfigurer servletOAuth2ResourceMatcherConfigurer,
            ServletSecurityAuthorizationManager servletSecurityAuthorizationManager) {
        this.oauth2ResourceServerConfigurerCustomer = new OAuth2ResourceServerConfigurerCustomer(oauth2AuthorizationProperties, jwtDecoder, opaqueTokenIntrospector);
        this.oauth2SessionManagementConfigurerCustomer = oauth2SessionManagementConfigurerCustomer;
        this.oauth2AuthorizeHttpRequestsConfigurerCustomer = new OAuth2AuthorizeHttpRequestsConfigurerCustomer(servletOAuth2ResourceMatcherConfigurer, servletSecurityAuthorizationManager);
        this.oauth2ExceptionHandlingConfigurerCustomizer = new OAuth2ExceptionHandlingConfigurerCustomizer(thymeleafTemplateHandler);
    }

    public OAuth2ResourceServerConfigurerCustomer getOAuth2ResourceServerConfigurerCustomer() {
        return oauth2ResourceServerConfigurerCustomer;
    }

    public OAuth2SessionManagementConfigurerCustomer getOAuth2SessionManagementConfigurerCustomer() {
        return oauth2SessionManagementConfigurerCustomer;
    }

    public OAuth2AuthorizeHttpRequestsConfigurerCustomer getOAuth2AuthorizeHttpRequestsConfigurerCustomer() {
        return oauth2AuthorizeHttpRequestsConfigurerCustomer;
    }

    public OAuth2ExceptionHandlingConfigurerCustomizer getOAuth2ExceptionHandlingConfigurerCustomizer() {
        return oauth2ExceptionHandlingConfigurerCustomizer;
    }
}
