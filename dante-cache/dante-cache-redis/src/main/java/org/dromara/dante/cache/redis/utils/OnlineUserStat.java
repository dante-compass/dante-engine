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

package org.dromara.dante.cache.redis.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.dromara.dante.cache.commons.constant.CacheConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;

/**
 * <p>Description: 在线用户实时统计工具类 </p>
 * <p>
 * 该工具类要基于 Redis 才能使用。当前主要用于 WebSocket 实时统计。
 *
 * @author : gengwei.zheng
 * @date : 2024/3/11 12:15
 */
public class OnlineUserStat {

    private static final Logger log = LoggerFactory.getLogger(OnlineUserStat.class);

    /**
     * 获取当前实时在线人数
     *
     * @return 在线人数
     */
    public static int getCount() {
        Long count = RedisBitMapUtils.bitCount(CacheConstants.ONLINE_USER_STAT);
        return count.intValue();
    }

    private static int status(Principal principal, boolean isOnline) {
        if (ObjectUtils.isNotEmpty(principal)) {

            RedisBitMapUtils.setBit(CacheConstants.ONLINE_USER_STAT, principal.getName(), isOnline);

            String status = isOnline ? "Online" : "Offline";

            log.debug("[Herodotus] |- WebSocket user [{}] is [{}].", principal, status);

            return getCount();
        }

        return 0;
    }

    /**
     * 用户上线
     *
     * @param principal 用户信息{@link Principal}
     * @return 当前实时在线人数
     */
    public static int connected(Principal principal) {
        return status(principal, true);
    }

    /**
     * 用户下线
     *
     * @param principal 用户信息{@link Principal}
     * @return 当前实时在线人数
     */
    public static int disconnected(Principal principal) {
        return status(principal, false);
    }
}
