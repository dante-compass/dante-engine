/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2020-2030 郑庚伟 ZHENGGENGWEI (码匠君), <herodotus@aliyun.com> Licensed under the AGPL License
 *
 * This file is part of Herodotus Stirrup.
 *
 * Herodotus Stirrup is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Herodotus Stirrup is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.herodotus.vip>.
 */

package cn.herodotus.engine.core.foundation.log;

import com.google.common.base.MoreObjects;
import org.springframework.boot.logging.LogLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: 基于 Logback 的日志输出通用属性对象 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/3/26 18:02
 */
public class LogbackLogging {

    /**
     * 日志级别，默认为INFO
     */
    private LogLevel level = LogLevel.INFO;

    /**
     * 日志输出内容配置
     */
    private Map<String, LogLevel> loggers = new HashMap<>();

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public Map<String, LogLevel> getLoggers() {
        return loggers;
    }

    public void setLoggers(Map<String, LogLevel> loggers) {
        this.loggers = loggers;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("level", level)
                .toString();
    }
}
