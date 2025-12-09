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

package org.dromara.dante.security.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.ObjectUtils;
import org.dromara.dante.core.constant.SystemConstants;
import org.dromara.dante.security.domain.UserPrincipal;

import java.util.Optional;

/**
 * <p>Description: Servlet 环境安全工具类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/10/17 23:36
 */
public class ServletSecurityUtils {

    /**
     * 从 {@link HttpSession} 读取用户信息 {@link UserPrincipal}
     * <p>
     * 注意：该方法依赖于整体的 Session 环境，后端 Session 以及前端 Session 的配合。对于不支持 Session 的前端，该方法可能会获取不到值。
     *
     * @param request {@link HttpServletRequest}
     * @return 用户信息 {@link UserPrincipal}
     */
    public static UserPrincipal getUserPrincipal(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (ObjectUtils.isNotEmpty(session)) {
            Object object = session.getAttribute(SystemConstants.KEY__USER_PRINCIPAL);
            if (object instanceof UserPrincipal) {
                return (UserPrincipal) object;
            }
        }

        return null;
    }

    /**
     * 从 {@link HttpSession} 读取用户信息 {@link UserPrincipal}
     * <p>
     * 注意：该方法依赖于整体的 Session 环境，后端 Session 以及前端 Session 的配合。对于不支持 Session 的前端，该方法可能会获取不到值。
     *
     * @param request {@link HttpServletRequest}
     * @return optional {@link Optional}
     */
    public static Optional<UserPrincipal> findUserPrincipal(HttpServletRequest request) {
        return Optional.ofNullable(getUserPrincipal(request));
    }
}
