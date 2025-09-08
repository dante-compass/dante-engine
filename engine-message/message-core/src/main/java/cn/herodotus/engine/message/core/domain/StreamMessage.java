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

package cn.herodotus.engine.message.core.domain;

import com.google.common.base.MoreObjects;
import org.springframework.util.MimeType;

import java.io.Serializable;

/**
 * <p>Description: Spring Cloud Stream 类型消息参数实体 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/10/26 14:57
 */
public class StreamMessage implements Serializable {

    private String bindingName;
    private String binderName;
    private Object payload;
    private MimeType outputContentType;

    public String getBindingName() {
        return bindingName;
    }

    public void setBindingName(String bindingName) {
        this.bindingName = bindingName;
    }

    public String getBinderName() {
        return binderName;
    }

    public void setBinderName(String binderName) {
        this.binderName = binderName;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public MimeType getOutputContentType() {
        return outputContentType;
    }

    public void setOutputContentType(MimeType outputContentType) {
        this.outputContentType = outputContentType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("bindingName", bindingName)
                .add("binderName", binderName)
                .add("payload", payload)
                .add("outputContentType", outputContentType)
                .toString();
    }
}
