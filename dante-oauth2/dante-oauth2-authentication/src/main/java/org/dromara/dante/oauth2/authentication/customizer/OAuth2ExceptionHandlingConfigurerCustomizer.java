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

package org.dromara.dante.oauth2.authentication.customizer;

import org.dromara.dante.oauth2.commons.properties.OAuth2AuthenticationProperties;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

/**
 * <p>Description: 授权服务器错误处理自定义器 </p>
 * <p>
 * 注意：与资源服务错误处理自定义器有区别。所以单独进行定义。
 * <p>
 * 主要区别在于，授权服务器需要定义 defaultAuthenticationEntryPointFor，来解决匿名登录跳转到 /login 页面的问题
 * <p>
 * 同时，defaultAuthenticationEntryPointFor 不能与 authenticationEntryPoint、accessDeniedHandler 同时定义。如果定义了后者，defaultAuthenticationEntryPointFor 将不会生效。
 *
 * @author : gengwei.zheng
 * @date : 2025/3/7 22:27
 */
public class OAuth2ExceptionHandlingConfigurerCustomizer implements Customizer<ExceptionHandlingConfigurer<HttpSecurity>> {

    private final OAuth2AuthenticationProperties authenticationProperties;

    public OAuth2ExceptionHandlingConfigurerCustomizer(OAuth2AuthenticationProperties authenticationProperties) {
        this.authenticationProperties = authenticationProperties;
    }

    @Override
    public void customize(ExceptionHandlingConfigurer<HttpSecurity> configurer) {
        configurer
                .defaultAuthenticationEntryPointFor(
                        new LoginUrlAuthenticationEntryPoint(authenticationProperties.getFormLogin().getLoginPageUrl()),
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                );
    }
}

