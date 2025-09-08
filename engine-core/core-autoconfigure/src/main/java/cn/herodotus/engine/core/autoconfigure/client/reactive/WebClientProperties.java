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

package cn.herodotus.engine.core.autoconfigure.client.reactive;

import cn.herodotus.engine.core.definition.constant.BaseConstants;
import com.google.common.base.MoreObjects;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * <p>Description: WebClient 配置参数 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/2/29 12:18
 */
@ConfigurationProperties(prefix = BaseConstants.PROPERTY_REACTIVE_WEBCLIENT)
public class WebClientProperties {

    /**
     * 连接超时时间，默认值：10000 毫秒
     */
    private Duration connectTimeout = Duration.ofMillis(10000L);
    /**
     * 读取超时时间，，默认值：10秒
     */
    private Duration readTimeout = Duration.ofSeconds(5000L);
    /**
     * 写入超时时间，，默认值：10秒
     */
    private Duration writeTimeout = Duration.ofSeconds(5000L);

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Duration getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(Duration writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("connectTimeout", connectTimeout)
                .add("readTimeout", readTimeout)
                .add("writeTimeout", writeTimeout)
                .toString();
    }
}
