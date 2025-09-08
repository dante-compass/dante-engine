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

package cn.herodotus.engine.core.autoconfigure.oauth2.definition;

import cn.herodotus.engine.core.definition.constant.ErrorCodes;
import cn.herodotus.engine.core.definition.domain.Feedback;
import cn.herodotus.engine.core.definition.domain.Result;
import cn.herodotus.engine.core.definition.exception.GlobalExceptionHandler;
import cn.herodotus.engine.core.definition.exception.PlatformRuntimeException;
import cn.herodotus.engine.core.identity.constant.OAuth2ErrorKeys;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * <p>Description: 统一异常处理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2019/11/18 8:12
 */
public class SecurityGlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(SecurityGlobalExceptionHandler.class);

    private static final Map<String, Feedback> EXCEPTION_DICTIONARY = new HashMap<>();

    static {
        EXCEPTION_DICTIONARY.put(OAuth2ErrorKeys.ACCESS_DENIED, ErrorCodes.ACCESS_DENIED);
        EXCEPTION_DICTIONARY.put(OAuth2ErrorKeys.INSUFFICIENT_SCOPE, ErrorCodes.INSUFFICIENT_SCOPE);
        EXCEPTION_DICTIONARY.put(OAuth2ErrorKeys.INVALID_CLIENT, ErrorCodes.INVALID_CLIENT);
        EXCEPTION_DICTIONARY.put(OAuth2ErrorKeys.INVALID_GRANT, ErrorCodes.INVALID_GRANT);
        EXCEPTION_DICTIONARY.put(OAuth2ErrorKeys.INVALID_REDIRECT_URI, ErrorCodes.INVALID_REDIRECT_URI);
        EXCEPTION_DICTIONARY.put(OAuth2ErrorKeys.INVALID_REQUEST, ErrorCodes.INVALID_REQUEST);
        EXCEPTION_DICTIONARY.put(OAuth2ErrorKeys.INVALID_SCOPE, ErrorCodes.INVALID_SCOPE);
        EXCEPTION_DICTIONARY.put(OAuth2ErrorKeys.INVALID_TOKEN, ErrorCodes.INVALID_TOKEN);
        EXCEPTION_DICTIONARY.put(OAuth2ErrorKeys.SERVER_ERROR, ErrorCodes.SERVER_ERROR);
        EXCEPTION_DICTIONARY.put(OAuth2ErrorKeys.TEMPORARILY_UNAVAILABLE, ErrorCodes.TEMPORARILY_UNAVAILABLE);
        EXCEPTION_DICTIONARY.put(OAuth2ErrorKeys.UNAUTHORIZED_CLIENT, ErrorCodes.UNAUTHORIZED_CLIENT);
        EXCEPTION_DICTIONARY.put(OAuth2ErrorKeys.UNSUPPORTED_GRANT_TYPE, ErrorCodes.UNSUPPORTED_GRANT_TYPE);
        EXCEPTION_DICTIONARY.put(OAuth2ErrorKeys.UNSUPPORTED_RESPONSE_TYPE, ErrorCodes.UNSUPPORTED_RESPONSE_TYPE);
        EXCEPTION_DICTIONARY.put(OAuth2ErrorKeys.UNSUPPORTED_TOKEN_TYPE, ErrorCodes.UNSUPPORTED_TOKEN_TYPE);
        EXCEPTION_DICTIONARY.put(OAuth2ErrorKeys.ACCOUNT_EXPIRED, ErrorCodes.ACCOUNT_EXPIRED);
        EXCEPTION_DICTIONARY.put(OAuth2ErrorKeys.BAD_CREDENTIALS, ErrorCodes.BAD_CREDENTIALS);
        EXCEPTION_DICTIONARY.put(OAuth2ErrorKeys.CREDENTIALS_EXPIRED, ErrorCodes.CREDENTIALS_EXPIRED);
        EXCEPTION_DICTIONARY.put(OAuth2ErrorKeys.ACCOUNT_DISABLED, ErrorCodes.ACCOUNT_DISABLED);
        EXCEPTION_DICTIONARY.put(OAuth2ErrorKeys.ACCOUNT_LOCKED, ErrorCodes.ACCOUNT_LOCKED);
        EXCEPTION_DICTIONARY.put(OAuth2ErrorKeys.ACCOUNT_ENDPOINT_LIMITED, ErrorCodes.ACCOUNT_ENDPOINT_LIMITED);
        EXCEPTION_DICTIONARY.put(OAuth2ErrorKeys.USERNAME_NOT_FOUND, ErrorCodes.USERNAME_NOT_FOUND);
        EXCEPTION_DICTIONARY.put(OAuth2ErrorKeys.SESSION_EXPIRED, ErrorCodes.SESSION_EXPIRED);
    }

    public static Result<String> resolveException(Exception ex, String path) {
        return GlobalExceptionHandler.resolveException(ex, path);
    }

    private static Result<String> handle(Exception exception, String path, String key, BiFunction<Feedback, String, Result<String>> toResult) {
        Optional<Feedback> optional = Optional.ofNullable(EXCEPTION_DICTIONARY.get(key));
        return optional
                .map(feedback -> toResult.apply(feedback, key))
                .orElseGet(() -> resolveException(exception, path));
    }

    private static Result<String> handleAuthenticationException(AuthenticationException authenticationException, String path) {
        Throwable throwable = authenticationException.getCause();
        if (ObjectUtils.isNotEmpty(throwable)) {
            // 此处是判断 throwable.getClass() 的父类是不是 PlatformRuntimeException
            // 注意：父类 PlatformRuntimeException.class 放左侧，子类放右侧。
            if (PlatformRuntimeException.class.isAssignableFrom(throwable.getClass())) {
                PlatformRuntimeException platformRuntimeException = (PlatformRuntimeException) throwable;
                Result<String> result = platformRuntimeException.getResult();
                return result.path(path);
            }
        }
        return resolveException(authenticationException, path);
    }

    /**
     * 静态解析认证异常
     *
     * @param exception 错误信息
     * @return Result 对象
     */
    public static Result<String> resolveSecurityException(Exception exception, String path) {

        Exception reason = new Exception();
        if (exception instanceof OAuth2AuthenticationException oAuth2AuthenticationException) {
            OAuth2Error oAuth2Error = oAuth2AuthenticationException.getError();
            if (EXCEPTION_DICTIONARY.containsKey(oAuth2Error.getErrorCode())) {
                Feedback feedback = EXCEPTION_DICTIONARY.get(oAuth2Error.getErrorCode());
                Result<String> result = Result.failure(feedback, oAuth2Error.getErrorCode());
                result.path(oAuth2Error.getUri());
                result.stackTrace(exception.getStackTrace());
                result.detail(exception.getMessage());
                return result;
            }
        } else if (exception instanceof InternalAuthenticationServiceException internalAuthenticationServiceException) {
            return handleAuthenticationException(internalAuthenticationServiceException, path);
        } else if (exception instanceof InsufficientAuthenticationException insufficientAuthenticationException) {
            return handleAuthenticationException(insufficientAuthenticationException, path);
        } else {
            String exceptionName = exception.getClass().getSimpleName();
            if (StringUtils.isNotEmpty(exceptionName) && EXCEPTION_DICTIONARY.containsKey(exceptionName)) {
                Feedback feedback = EXCEPTION_DICTIONARY.get(exceptionName);
                return Result.failure(feedback);
            } else {
                reason = exception;
            }
        }

        return resolveException(reason, path);
    }
}
