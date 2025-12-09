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

package org.dromara.dante.web.servlet.template;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dromara.dante.core.domain.Result;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.util.Map;

/**
 * <p>Description: Servlet 环境 Thymeleaf 模版工具类 </p>
 * <p>
 * 采用静态类注入的方式，可以实现使用通过 Spring Boot 环境配置化的 SpringTemplateEngine。
 *
 * @author : gengwei.zheng
 * @date : 2025/3/7 17:11
 */
public class ThymeleafTemplateHandler {

    private final SpringTemplateEngine springTemplateEngine;

    public ThymeleafTemplateHandler(SpringTemplateEngine springTemplateEngine) {
        this.springTemplateEngine = springTemplateEngine;
    }

    public String renderToError(HttpServletRequest request, HttpServletResponse response, Result<String> result) {
        return render(request, response, "error", result.toErrorModel());
    }

    public String render(String template, Map<String, Object> model) {
        Context context = new Context();
        context.setVariables(model);
        return springTemplateEngine.process(template, context);
    }

    public String render(HttpServletRequest request, HttpServletResponse response, String template, Map<String, Object> model) {
        final IWebExchange webExchange = JakartaServletWebApplication
                .buildApplication(request.getServletContext())
                .buildExchange(request, response);
        WebContext context = new WebContext(webExchange);
        context.setVariables(model);
        return springTemplateEngine.process(template, context);
    }
}
