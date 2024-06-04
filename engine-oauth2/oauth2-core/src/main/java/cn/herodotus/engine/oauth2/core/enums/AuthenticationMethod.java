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

package cn.herodotus.engine.oauth2.core.enums;

import cn.herodotus.engine.assistant.definition.enums.BaseUiEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 客户端身份验证模式 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/3/17 14:49
 */
@Schema(title = "OAuth2 Client 认证方式")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AuthenticationMethod implements BaseUiEnum<String> {

    /**
     * enum
     */
    CLIENT_SECRET_BASIC(ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue(), "基于Client Secret的Basic验证模式"),
    CLIENT_SECRET_POST(ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue(), "基于Client Secret的Post验证模式"),
    CLIENT_SECRET_JWT(ClientAuthenticationMethod.CLIENT_SECRET_JWT.getValue(), "基于Client Secret的JWT验证模式"),
    PRIVATE_KEY_JWT(ClientAuthenticationMethod.PRIVATE_KEY_JWT.getValue(), "基于私钥的JWT验证模式"),
    NONE(ClientAuthenticationMethod.NONE.getValue(), "不设置任何模式"),
    TLS_CLIENT_AUTH(ClientAuthenticationMethod.TLS_CLIENT_AUTH.getValue(), "TSL 客户端认证"),
    SELF_SIGNED_TLS_CLIENT_AUTH(ClientAuthenticationMethod.SELF_SIGNED_TLS_CLIENT_AUTH.getValue(), "自签名 TSL 客户端认证");

    private static final Map<Integer, AuthenticationMethod> INDEX_MAP = new HashMap<>();
    private static final List<Map<String, Object>> JSON_STRUCTURE = new ArrayList<>();

    static {
        for (AuthenticationMethod authenticationMethod : AuthenticationMethod.values()) {
            INDEX_MAP.put(authenticationMethod.ordinal(), authenticationMethod);
            JSON_STRUCTURE.add(authenticationMethod.ordinal(),
                    ImmutableMap.<String, Object>builder()
                            .put("value", authenticationMethod.getValue())
                            .put("key", authenticationMethod.name())
                            .put("text", authenticationMethod.getDescription())
                            .put("index", authenticationMethod.ordinal())
                            .build());
        }
    }

    @Schema(title = "认证方法")
    private final String value;
    @Schema(title = "文字")
    private final String description;

    AuthenticationMethod(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static AuthenticationMethod get(Integer index) {
        return INDEX_MAP.get(index);
    }

    public static List<Map<String, Object>> getPreprocessedJsonStructure() {
        return JSON_STRUCTURE;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getValue() {
        return value;
    }
}
