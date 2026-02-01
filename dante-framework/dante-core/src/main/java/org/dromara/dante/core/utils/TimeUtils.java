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

import cn.hutool.v7.core.math.NumberUtil;
import org.apache.commons.lang3.ObjectUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * <p>Description: 特殊日期处理 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/7/2 22:54
 */
public class TimeUtils {

    /**
     * {@link LocalDateTime} 转换成 {@link Instant}
     *
     * @param localDateTime 时间类型 {@link LocalDateTime}
     * @return {@link Instant} 或 null
     */
    public static Instant toInstant(LocalDateTime localDateTime) {
        if (ObjectUtils.isEmpty(localDateTime)) {
            return null;
        }

        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    /**
     * {@link Instant} 转换成 {@link LocalDateTime}
     *
     * @param instant 时间类型 {@link Instant}
     * @return {@link LocalDateTime} 或 null
     */
    public static LocalDateTime toLocalDateTime(Instant instant) {
        if (ObjectUtils.isEmpty(instant)) {
            return null;
        }

        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * 时间戳转换为 {@link LocalDateTime}
     *
     * @param timestamp 时间戳 {@link Long}
     * @return 如果 timestamp 有值，则返回转换后的 {@link LocalDateTime}；如果为 0, 则返回null
     */
    public static LocalDateTime toLocalDateTime(long timestamp) {
        if (NumberUtil.isZero(timestamp)) {
            return null;
        }

        Instant instant = Instant.ofEpochMilli(timestamp);
        return toLocalDateTime(instant);
    }

    /**
     * {@link Date} 转换为 {@link LocalDateTime}
     *
     * @param date 日期 {@link Date}
     * @return {@link LocalDateTime} 或 null
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (ObjectUtils.isEmpty(date)) {
            return null;
        }

        return toLocalDateTime(date.toInstant());
    }

    /**
     * {@link ZonedDateTime} 转换成 {@link Date}
     *
     * @param zonedDateTime {@link ZonedDateTime}
     * @return 如果 ZonedDateTime 有值，则返回转换后的 {@link Date}；如果为空，则返回null。
     */
    public static Date toDate(ZonedDateTime zonedDateTime) {
        if (ObjectUtils.isEmpty(zonedDateTime)) {
            return null;
        }
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * {@link LocalDateTime} 转换成 {@link Date}
     *
     * @param localDateTime {@link LocalDateTime}
     * @return 如果 localDateTime 有值，则返回转换后的 {@link Date}；如果为空，则返回null。
     */
    public static Date toDate(LocalDateTime localDateTime) {
        if (ObjectUtils.isEmpty(localDateTime)) {
            return null;
        }

        Instant instant = toInstant(localDateTime);
        if (ObjectUtils.isNotEmpty(instant)) {
            return Date.from(instant);
        }

        return null;
    }
}
