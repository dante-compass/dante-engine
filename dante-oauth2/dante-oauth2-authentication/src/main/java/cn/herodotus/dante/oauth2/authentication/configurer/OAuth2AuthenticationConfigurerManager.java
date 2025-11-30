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

package cn.herodotus.dante.oauth2.authentication.configurer;

import cn.herodotus.dante.core.support.crypto.DigitalEnvelopeProcessor;
import cn.herodotus.dante.web.servlet.template.ThymeleafTemplateHandler;
import cn.herodotus.dante.oauth2.authentication.customizer.OAuth2ExceptionHandlingConfigurerCustomizer;
import cn.herodotus.dante.oauth2.authentication.customizer.OAuth2FormLoginConfigurerCustomizer;
import cn.herodotus.dante.oauth2.authentication.response.OAuth2AccessTokenResponseHandler;
import cn.herodotus.dante.oauth2.authentication.response.OAuth2AuthenticationFailureHandler;
import cn.herodotus.dante.oauth2.commons.properties.OAuth2AuthenticationProperties;

/**
 * <p>Description: 授权服务器通用 Bean 配置器 </p>
 * <p>
 * 重新嵌套一层 Bean，将授权服务器中通用的 Bean 进行封装。减少使用时重复注入相同的内容，方便使用。
 *
 * @author : gengwei.zheng
 * @date : 2025/3/8 0:25
 */
public class OAuth2AuthenticationConfigurerManager {

    private final DigitalEnvelopeProcessor digitalEnvelopeProcessor;

    private final OAuth2AuthenticationProperties oauth2AuthenticationProperties;
    private final OAuth2FormLoginConfigurerCustomizer oauth2FormLoginConfigurerCustomizer;
    private final OAuth2ExceptionHandlingConfigurerCustomizer oauth2ExceptionHandlingConfigurerCustomizer;
    private final OAuth2AccessTokenResponseHandler oauth2AccessTokenResponseHandler;
    private final OAuth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;

    public OAuth2AuthenticationConfigurerManager(
            ThymeleafTemplateHandler thymeleafTemplateHandler,
            DigitalEnvelopeProcessor digitalEnvelopeProcessor,
            OAuth2AuthenticationProperties oauth2AuthenticationProperties) {
        this.digitalEnvelopeProcessor = digitalEnvelopeProcessor;
        this.oauth2AuthenticationProperties = oauth2AuthenticationProperties;
        this.oauth2FormLoginConfigurerCustomizer = new OAuth2FormLoginConfigurerCustomizer(oauth2AuthenticationProperties);
        this.oauth2ExceptionHandlingConfigurerCustomizer = new OAuth2ExceptionHandlingConfigurerCustomizer(oauth2AuthenticationProperties);
        this.oauth2AccessTokenResponseHandler = new OAuth2AccessTokenResponseHandler(digitalEnvelopeProcessor);
        this.oauth2AuthenticationFailureHandler = new OAuth2AuthenticationFailureHandler(thymeleafTemplateHandler);
    }

    public DigitalEnvelopeProcessor getDigitalEnvelopeProcessor() {
        return digitalEnvelopeProcessor;
    }

    public OAuth2AuthenticationProperties getOAuth2AuthenticationProperties() {
        return oauth2AuthenticationProperties;
    }

    public OAuth2FormLoginConfigurerCustomizer getOAuth2FormLoginConfigurerCustomizer() {
        return oauth2FormLoginConfigurerCustomizer;
    }

    public OAuth2ExceptionHandlingConfigurerCustomizer getOAuth2ExceptionHandlingConfigurerCustomizer() {
        return oauth2ExceptionHandlingConfigurerCustomizer;
    }

    public OAuth2AccessTokenResponseHandler getOAuth2AccessTokenResponseHandler() {
        return oauth2AccessTokenResponseHandler;
    }

    public OAuth2AuthenticationFailureHandler getOAuth2AuthenticationFailureHandler() {
        return oauth2AuthenticationFailureHandler;
    }
}
