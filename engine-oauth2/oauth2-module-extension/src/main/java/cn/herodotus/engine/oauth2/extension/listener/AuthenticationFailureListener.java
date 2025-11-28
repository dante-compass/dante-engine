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

package cn.herodotus.engine.oauth2.extension.listener;

import cn.herodotus.dante.core.constant.SystemConstants;
import cn.herodotus.engine.oauth2.extension.manager.OAuth2ComplianceManager;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>Description: 登出成功监听 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/12/18 17:58
 */
@Component
public class AuthenticationFailureListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFailureListener.class);

    private final OAuth2ComplianceManager complianceManager;

    public AuthenticationFailureListener(OAuth2ComplianceManager complianceManager) {
        this.complianceManager = complianceManager;
    }


    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {

        log.debug("[Herodotus] |- User sign in catch failure event : [{}].", event.getClass().getName());

        if (event instanceof AuthenticationFailureBadCredentialsEvent) {
            Authentication authentication = event.getAuthentication();

            String username = null;

            if (authentication instanceof OAuth2AuthorizationGrantAuthenticationToken token) {

                log.debug("[Herodotus] |- Toke object in failure event  is OAuth2AuthorizationGrantAuthenticationToken");

                Map<String, Object> params = token.getAdditionalParameters();
                username = getPrincipal(params);
            }

            if (authentication instanceof UsernamePasswordAuthenticationToken token) {

                log.debug("[Herodotus] |- Toke object in failure event  is UsernamePasswordAuthenticationToken");

                Object principal = token.getPrincipal();
                if (principal instanceof String) {
                    username = (String) principal;
                }
            }

            if (StringUtils.isNotBlank(username)) {
                complianceManager.countingSignInFailureTimes(username);
            }
        }
    }

    private String getPrincipal(Map<String, Object> params) {
        if (MapUtils.isNotEmpty(params)) {
            if (params.containsKey(SystemConstants.USERNAME)) {
                Object value = params.get(SystemConstants.USERNAME);
                if (ObjectUtils.isNotEmpty(value)) {
                    return (String) value;
                }
            }
        }

        return null;
    }
}
