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

package cn.herodotus.engine.web.core.servlet.utils;

import cn.herodotus.engine.core.definition.domain.Result;
import cn.herodotus.engine.core.definition.utils.JacksonUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * <p>Description:  Http与Servlet工具类. </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/3/4 11:39
 */
public class ResponseUtils {

    private static final Logger log = LoggerFactory.getLogger(ResponseUtils.class);

    /**
     * 将内容写入到响应
     *
     * @param response    响应 {@link HttpServletResponse}
     * @param statusCode  状态码
     * @param content     待写入的内容
     * @param contentType 内容类型
     */
    public static void render(HttpServletResponse response, int statusCode, String content, String contentType) {
        try {
            response.setStatus(statusCode);
            response.setContentType(contentType);
            response.setContentType(contentType);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            response.getWriter().print(content);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            log.error("[Herodotus] |- Render response error!");
        }
    }

    /**
     * 将 JSON 写入到响应。
     *
     * @param response   响应 {@link HttpServletResponse}
     * @param statusCode 状态码
     * @param content    待写入的内容
     */
    public static void renderJson(HttpServletResponse response, int statusCode, String content) {
        render(response, statusCode, content, MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * 将 JSON 写入到响应。
     *
     * @param response   响应 {@link HttpServletResponse}
     * @param statusCode 状态码
     * @param object     待写入的内容
     */
    public static void renderJson(HttpServletResponse response, int statusCode, Object object) {
        renderJson(response, statusCode, JacksonUtils.toJson(object));
    }

    /**
     * 将 Result 以 JSON 格式输出到响应。
     *
     * @param response 响应 {@link HttpServletResponse}
     * @param result   待写入的内容 {@link Result}
     */
    public static void renderResult(HttpServletResponse response, Result<String> result) {
        renderJson(response, result.getStatus(), result);
    }

    /**
     * 将 HTML 写入到响应。
     *
     * @param response   响应 {@link HttpServletResponse}
     * @param statusCode 状态码
     * @param content    待写入的内容
     */
    public static void renderHtml(HttpServletResponse response, int statusCode, String content) {
        render(response, statusCode, content, MediaType.TEXT_HTML_VALUE);
    }
}
