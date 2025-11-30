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

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import tools.jackson.databind.module.SimpleModule;

/**
 * <p>Description: 自定义 OAutho2 Module </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/10/24 15:51
 */
public class OAuth2TokenJackson2Module extends SimpleModule {

    public OAuth2TokenJackson2Module() {
        super(OAuth2TokenJackson2Module.class.getName(), Jackson2Constants.VERSION);
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixIn(ClientAuthenticationMethod.class, ClientAuthenticationMethodMixin.class);
        context.setMixIn(AuthorizationGrantType.class, AuthorizationGrantTypeMixin.class);
        context.setMixIn(TokenSettings.class, TokenSettingsMixin.class);
        context.setMixIn(ClientSettings.class, ClientSettingsMixin.class);
        context.setMixIn(RegisteredClient.class, RegisteredClientMixin.class);
        context.setMixIn(OAuth2ClientAuthenticationToken.class, OAuth2ClientAuthenticationTokenMixin.class);
    }
}
