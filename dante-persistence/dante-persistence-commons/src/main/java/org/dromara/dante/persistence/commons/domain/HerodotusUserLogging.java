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
 * along with this program.  If not, see <https://www.herodotus.cn>.
 */

package org.dromara.dante.persistence.commons.domain;

import cn.herodotus.stirrup.persistence.commons.definition.AbstractAuditRecord;
import com.google.common.base.MoreObjects;

/**
 * <p>Description: 安全合规性 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/1/3 13:33
 */
public class HerodotusUserLogging extends AbstractAuditRecord {

    private String loggingId;

    private String operation;

    private String location;

    public HerodotusUserLogging() {
        super();
    }

    public HerodotusUserLogging(String userAgent) {
        super(userAgent);
    }

    public String getLoggingId() {
        return loggingId;
    }

    public void setLoggingId(String loggingId) {
        this.loggingId = loggingId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("complianceId", loggingId)
                .add("operation", operation)
                .add("location", location)
                .addValue(super.toString())
                .toString();
    }
}
