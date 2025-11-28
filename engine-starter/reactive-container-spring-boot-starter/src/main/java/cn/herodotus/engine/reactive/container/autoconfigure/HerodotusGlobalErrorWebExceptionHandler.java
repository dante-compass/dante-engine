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

package cn.herodotus.engine.reactive.container.autoconfigure;

import cn.herodotus.dante.core.constant.SymbolConstants;
import cn.herodotus.dante.core.domain.Feedback;
import cn.herodotus.dante.core.domain.Result;
import cn.herodotus.dante.core.exception.PlatformRuntimeException;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.webflux.autoconfigure.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.webflux.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * <p>Description: WebFlux 全局错误定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/5/1 21:31
 */
@Component
public class HerodotusGlobalErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

    /**
     * Create a new {@code DefaultErrorWebExceptionHandler} instance.
     *
     * @param errorAttributes    the error attributes
     * @param resources          the resources configuration properties
     * @param errorProperties    the error configuration properties
     * @param applicationContext the current application context
     * @since 2.4.0
     */
    public HerodotusGlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resources, errorProperties, applicationContext);
    }

    @Override
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {

        Map<String, Object> error = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        int status = getHttpStatus(error);

        String path = getError(error, "path");
        String message = getError(error, "message");

        Throwable throwable = getError(request);
        if (throwable instanceof PlatformRuntimeException exception) {
            Feedback feedback = exception.getFeedback();
            Result<String> result = Result.failure(feedback);
            result.detail(message);
            result.path(path);
            result.stackTrace(exception.getStackTrace());
            status = result.getStatus();
            error = result.toModel();
        }

        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(error));
    }

    private String getError(Map<String, Object> error, String key) {
        if (MapUtils.isNotEmpty(error)) {
            Object value = error.get(key);
            if (ObjectUtils.isNotEmpty(value)) {
                return String.valueOf(value);
            }
        }

        return SymbolConstants.BLANK;
    }
}
