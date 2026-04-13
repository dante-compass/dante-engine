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

package org.dromara.dante.core.utils;

import cn.hutool.v7.core.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.dromara.dante.core.constant.SymbolConstants;
import org.dromara.dante.core.constant.SystemConstants;

/**
 * <p>Description: Token 工具类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/6/30 12:42
 */
public class TokenUtils {

    /**
     * 拼装 Bearer Token 标准格式
     *
     * @param accessToken JWT 或 Opaque Token.
     * @return 标准格式 Bearer Token
     */
    public static String bearer(String accessToken) {
        return SystemConstants.BEARER_TOKEN + accessToken;
    }

    /**
     * 拼装 Basic Token 标准格式
     *
     * @param clientId     客户端ID
     * @param clientSecret 客户端密钥
     * @return 标准格式 Basic Token
     */
    public static String basic(String clientId, String clientSecret) {
        return SystemConstants.BASIC_TOKEN + Base64.encode(clientId + SymbolConstants.COLON + clientSecret);
    }

    /**
     * 判断是否为标准格式 Token
     *
     * @param token 以 Bearer 或者 Basic 开头的 Token 内容
     * @return true 格式正确，false 格式错误。
     */
    public static boolean isToken(String token) {
        if (StringUtils.isNotBlank(token)) {
            return Strings.CS.startsWith(token, SystemConstants.BEARER_TOKEN) || Strings.CS.startsWith(token, SystemConstants.BASIC_TOKEN);
        } else {
            return false;
        }
    }

    /**
     * 从以 Bearer 或者 Basic 开头的 Token 中，提取 Token 部分。
     *
     * @param token 以 Bearer 或者 Basic 开头的 Token
     * @return 去除了 Bearer 或者 Basic 的 Token
     */
    public static String extract(String token) {
        if (StringUtils.isNotBlank(token)) {
            if (Strings.CS.startsWith(token, SystemConstants.BEARER_TOKEN)) {
                return StringUtils.substringAfter(token, SystemConstants.BEARER_TOKEN);
            }

            if (Strings.CS.startsWith(token, SystemConstants.BASIC_TOKEN)) {
                return StringUtils.substringAfter(token, SystemConstants.BASIC_TOKEN);
            }
        }

        return null;
    }
}
