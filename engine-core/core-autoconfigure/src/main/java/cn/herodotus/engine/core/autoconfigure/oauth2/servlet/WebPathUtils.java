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

package cn.herodotus.engine.core.autoconfigure.oauth2.servlet;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

/**
 * <p>Description: Web 路径处理工具类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/4/21 16:48
 */
public class WebPathUtils {

    /**
     * 将配置路径转换为 {@link RequestMatcher} 数组
     *
     * @param path 静态请求路径
     * @return {@link RequestMatcher} 数组
     */
    public static RequestMatcher toRequestMatcher(String path) {
        return PathPatternRequestMatcher.withDefaults().matcher(path);
    }

    /**
     * 将配置路径转换为 {@link RequestMatcher} 数组
     *
     * @param paths 静态请求路径
     * @return {@link RequestMatcher} 数组
     */
    public static RequestMatcher[] toRequestMatchers(List<String> paths) {
        if (CollectionUtils.isNotEmpty(paths)) {
            List<PathPatternRequestMatcher> matchers = paths.stream().map(item -> PathPatternRequestMatcher.withDefaults().matcher(item)).toList();
            RequestMatcher[] result = new RequestMatcher[matchers.size()];
            return matchers.toArray(result);
        } else {
            return new RequestMatcher[]{};
        }
    }

    /**
     * 判断请求是否与设定的模式匹配
     *
     * @param matchers 路径匹配模式
     * @param request  请求
     * @return 是否匹配
     */
    public static boolean isRequestMatched(RequestMatcher[] matchers, HttpServletRequest request) {
        for (RequestMatcher matcher : matchers) {
            if (matcher.matches(request)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断请求是否与设定的模式匹配
     *
     * @param paths   路径
     * @param request 请求
     * @return 是否匹配
     */
    public static boolean isRequestMatched(List<String> paths, HttpServletRequest request) {
        RequestMatcher[] matchers = toRequestMatchers(paths);
        return isRequestMatched(matchers, request);
    }
}
