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

package org.dromara.dante.web.definition.utils;

import org.dromara.dante.core.constant.SymbolConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>Description: Http 通用工具类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/2/5 21:50
 */
public class HttpUtils {

    /**
     * 解析 Cookie 头的值解析为 Map
     *
     * @param cookie Cookie 头的值
     * @return Cookie 键值对。
     */
    private static Map<String, String> rawCookieToMap(String cookie) {
        if (StringUtils.isNotBlank(cookie)) {
            return Stream.of(cookie.split(SymbolConstants.SEMICOLON_AND_SPACE))
                    .map(pair -> pair.split(SymbolConstants.EQUAL))
                    .collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));
        } else {
            return Collections.emptyMap();
        }
    }

    /**
     * 获取多个 Cookie 请求头中的属性值
     *
     * @param cookie Cookie 请求头值
     * @param name   Cookie中的属性名
     * @return cookie 中属性值的集合
     */
    public static List<String> get(String cookie, String... name) {
        Map<String, String> cookies = rawCookieToMap(cookie);
        return Stream.of(name).map(cookies::get).toList();
    }

    /**
     * 从 Cookie 请求头中，找到给定任意给定属性的值
     *
     * @param cookie Cookie 请求头值
     * @param name   Cookie中的属性名
     * @return cookie 中属性值的集合
     */
    public static String getAny(String cookie, String... name) {
        List<String> result = get(cookie, name);
        return CollectionUtils.isNotEmpty(result) ? result.get(0) : null;
    }

    /**
     * 获取 Cookie 请求头中的属性值
     *
     * @param cookie Cookie 请求头值
     * @param name   Cookie中的属性名
     * @return cookie 中属性值
     */
    public static String get(String cookie, String name) {
        Map<String, String> cookies = rawCookieToMap(cookie);
        return cookies.get(name);
    }

    /**
     * 判断是否为 GET 请求
     *
     * @param method {@link HttpMethod}
     * @return true 是 GET 请求，false 不是 GET 请求
     */
    public static boolean isGetRequest(HttpMethod method) {
        return method == HttpMethod.GET;
    }

    /**
     * 判断是否为 POST 类型 请求
     *
     * @param method      {@link HttpMethod}
     * @param contentType 内容类型
     * @return true 是 POST 请求，false 不是 POST 请求
     */
    public static Boolean isPostRequest(HttpMethod method, String contentType) {
        return (method == HttpMethod.POST || method == HttpMethod.PUT) && (Strings.CI.equals(MediaType.APPLICATION_FORM_URLENCODED_VALUE, contentType) || MediaType.APPLICATION_JSON_VALUE.equals(contentType));
    }

    /**
     * 判断请求体中的数据是否为 application/json
     *
     * @param contentType Content Type
     * @return true 请求体数据类型为 application/json，false 请求体数据类型不是 application/json
     */
    public static boolean isJson(String contentType) {
        return Strings.CI.equals(MediaType.APPLICATION_JSON_VALUE, contentType);
    }

    /**
     * 判断请求体中的数据是否为 application/x-www-form-urlencoded
     *
     * @param contentType Content Type
     * @return true 请求体数据类型为 application/x-www-form-urlencoded，false 请求体数据类型不是 application/x-www-form-urlencoded
     */
    public static boolean isFormUrlencoded(String contentType) {
        return Strings.CI.equals(MediaType.APPLICATION_FORM_URLENCODED_VALUE, contentType);
    }

    /**
     * 判断是否为 GET 类型的请求，这里的 GET 类型是指通过 query 方式传递参数的请求类型。GET 类型请求包括 GET 和 DELETE
     *
     * @param method 请求类型 {@link HttpMethod}
     * @return true 是 Get 类型，false 不是 Get 类型
     */
    public static boolean isGetTypeRequest(HttpMethod method) {
        return method == HttpMethod.GET || method == HttpMethod.DELETE;
    }

    /**
     * 判断是否为 POST 类型的请求，这里的 POST 类型是指通过 RequestBody 方式传递参数的请求类型。POST 类型请求包括 POST 和 PUT
     *
     * @param method      请求类型 {@link HttpMethod}
     * @param contentType 内容类型
     * @return true 是 POST 类型，false 不是 POST 类型
     */
    public static Boolean isPostTypeRequest(HttpMethod method, String contentType) {
        return (method == HttpMethod.POST || method == HttpMethod.PUT) && (isFormUrlencoded(contentType) || isJson(contentType));
    }

    /**
     * 判断请求是否为 HTML，例如通过浏览器直接访问的。
     *
     * @param accept      Accept 请求头
     * @param contentType Content Type 请求头
     * @return true 请求体数据类型为 text/html，false 请求体数据类型不是 application/json
     */
    public static Boolean isHtml(String accept, String contentType) {
        if (StringUtils.isNotBlank(contentType) && Strings.CI.equals(MediaType.TEXT_HTML_VALUE, contentType)) {
            return true;
        } else {
            return Strings.CS.containsAny(accept, MediaType.TEXT_HTML_VALUE, MediaType.APPLICATION_XHTML_XML_VALUE);
        }
    }
}
