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

package cn.herodotus.engine.core.autoconfigure.oauth2.definition;

import cn.herodotus.engine.core.definition.constant.SystemConstants;
import cn.herodotus.engine.core.identity.domain.HerodotusUser;
import cn.herodotus.engine.core.identity.domain.UserPrincipal;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;

import java.util.HashSet;
import java.util.List;

/**
 * <p>Description: UserPrincipal 工具类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/4/8 15:48
 */
public class UserPrincipalConverter {

    public static UserPrincipal toUserPrincipal(HerodotusUser herodotusUser) {
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId(herodotusUser.getUserId());
        userPrincipal.setName(herodotusUser.getUsername());
        userPrincipal.setRoles(herodotusUser.getRoles());
        userPrincipal.setAvatar(herodotusUser.getAvatar());
        userPrincipal.setEmployeeId(herodotusUser.getEmployeeId());
        return userPrincipal;
    }

    public static UserPrincipal toUserPrincipal(OAuth2AuthenticatedPrincipal oauth2AuthenticatedPrincipal) {
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId(oauth2AuthenticatedPrincipal.getAttribute(SystemConstants.SCOPE_OPENID));
        userPrincipal.setName(oauth2AuthenticatedPrincipal.getName());
        List<String> roles = oauth2AuthenticatedPrincipal.getAttribute(SystemConstants.ROLES);
        if (CollectionUtils.isNotEmpty(roles)) {
            userPrincipal.setRoles(new HashSet<>(roles));
        }
        userPrincipal.setAvatar(oauth2AuthenticatedPrincipal.getAttribute(SystemConstants.AVATAR));
        userPrincipal.setEmail(oauth2AuthenticatedPrincipal.getAttribute(SystemConstants.SCOPE_EMAIL));
        userPrincipal.setEmployeeId(oauth2AuthenticatedPrincipal.getAttribute(SystemConstants.EMPLOYEE_ID));
        return userPrincipal;
    }

    public static UserPrincipal toUserPrincipal(Jwt jwt) {
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId(jwt.getClaimAsString(SystemConstants.SCOPE_OPENID));
        userPrincipal.setName(jwt.getClaimAsString(JwtClaimNames.SUB));
        userPrincipal.setRoles(jwt.getClaim(SystemConstants.ROLES));
        userPrincipal.setEmail(jwt.getClaim(SystemConstants.SCOPE_EMAIL));
        userPrincipal.setAvatar(jwt.getClaimAsString(SystemConstants.AVATAR));
        userPrincipal.setEmployeeId(jwt.getClaimAsString(SystemConstants.EMPLOYEE_ID));
        return userPrincipal;
    }
}
