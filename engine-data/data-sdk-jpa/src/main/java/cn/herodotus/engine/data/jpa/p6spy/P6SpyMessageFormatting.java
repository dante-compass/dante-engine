/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2020-2030 郑庚伟 ZHENGGENGWEI (码匠君), <herodotus@aliyun.com> Licensed under the AGPL License
 *
 * This file is part of Herodotus Engine.
 *
 * Herodotus Engine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Herodotus Engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.herodotus.cn>.
 */

package cn.herodotus.engine.data.jpa.p6spy;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.apache.commons.lang3.StringUtils;
import org.dromara.hutool.core.date.DateFormatPool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>Description: P6Spy自定义格式化</p>
 *
 * @author gengwei.zheng
 * @date 2019/1/20
 */
public class P6SpyMessageFormatting implements MessageFormattingStrategy {

    private final SimpleDateFormat format = new SimpleDateFormat(DateFormatPool.NORM_DATETIME_MS_PATTERN);

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {

        /**
         *
         * StringBuilder 是为了避免字符串拼接过程中产生很多不必要的字符串对象。
         * 经过编译器优化，多个字符串相‘+’，优化后，与StringBuilder等价
         *
         * 关注idea的“'StringBuilder builder' can be replaced with 'String'”提示
         */
        String builder = this.format.format(new Date()) + " | took " +
                elapsed +
                "ms | " +
                category +
                " | connection " +
                connectionId +
                " | url " +
                url +
                "\n------------------------| " +
                sql +
                ";";
        return StringUtils.isNotEmpty(sql.trim()) ? String.valueOf(builder) : "";
    }
}
