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

package cn.herodotus.engine.oauth2.core.definition.details;

import cn.herodotus.engine.assistant.core.utils.http.SessionUtils;
import cn.herodotus.engine.oauth2.core.utils.SymmetricUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * <p>Description: 表单登录 Details 定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/4/12 10:32
 */
public class FormLoginWebAuthenticationDetails extends WebAuthenticationDetails {

    /**
     * 验证码是否关闭
     */
    private final Boolean enabled;
    /**
     * 请求中，验证码对应的表单参数名。对应UI Properties 里面的 captcha parameter
     */
    private final String parameterName;
    /**
     * 验证码分类
     */
    private final String category;
    private final String code;

    public FormLoginWebAuthenticationDetails(String remoteAddress, String sessionId, Boolean enabled, String parameterName, String category, String code) {
        super(remoteAddress, sessionId);
        this.enabled = enabled;
        this.parameterName = parameterName;
        this.category = category;
        this.code = code;
    }

    public FormLoginWebAuthenticationDetails(HttpServletRequest request, Boolean enabled, String parameterName, String category, String code) {
        super(request);
        this.enabled = enabled;
        this.parameterName = parameterName;
        this.category = category;
        this.code = code;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public String getCategory() {
        return category;
    }

    public String getCode() {
        return code;
    }

    public String getParameterName() {
        return parameterName;
    }
}
