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

package cn.herodotus.engine.oauth2.core.utils;

import cn.herodotus.engine.assistant.core.json.jackson2.utils.Jackson2Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * <p>Description:  Http与Servlet工具类. </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/3/4 11:39
 */
public class WebUtils extends org.springframework.web.util.WebUtils {

    private static final Logger log = LoggerFactory.getLogger(WebUtils.class);

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

    /**
     * 客户端返回JSON字符串
     *
     * @param response HttpServletResponse
     * @param object   需要转换的对象
     */
    public static void renderJson(HttpServletResponse response, Object object) {
        renderJson(response, Jackson2Utils.toJson(object), MediaType.APPLICATION_JSON.toString());
    }

    /**
     * 客户端返回字符串
     *
     * @param response HttpServletResponse
     * @param string   需要绘制的信息
     */
    public static void renderJson(HttpServletResponse response, String string, String type) {
        try {
            response.setContentType(type);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            response.getWriter().print(string);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            log.error("[Herodotus] |- Render response to Json error!");
        }
    }
}
