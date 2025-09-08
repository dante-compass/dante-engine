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

package cn.herodotus.engine.core.definition.domain;


import cn.herodotus.engine.core.definition.constant.ErrorCodes;
import cn.herodotus.engine.core.definition.constant.SystemConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.HttpStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: 统一响应实体 </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/2/29 14:50
 */
@Schema(name = "统一响应返回实体", description = "所有Rest接口统一返回的实体定义", example = "new Result<T>().ok().message(\"XXX\")")
public class Result<T> extends Response<T, Integer> {

    @Schema(name = "响应时间戳", pattern = SystemConstants.DATE_TIME_FORMAT)
    @JsonFormat(pattern = SystemConstants.DATE_TIME_FORMAT)
    private final Date timestamp = new Date();

    @Schema(name = "请求路径")
    private String path;

    @Schema(name = "http状态码")
    private int status;

    @Schema(name = "链路追踪TraceId")
    private String traceId;

    @Schema(name = "校验错误信息")
    private final Error error = new Error();

    public Result() {
        super();
    }

    private static <T> Result<T> create(String message, String detail, int code, int status, T data, StackTraceElement[] stackTrace) {
        Result<T> result = new Result<>();
        if (StringUtils.isNotBlank(message)) {
            result.message(message);
        }

        if (StringUtils.isNotBlank(detail)) {
            result.detail(detail);
        }

        result.code(code);
        result.status(status);

        if (ObjectUtils.isNotEmpty(data)) {
            result.data(data);
        }

        if (ArrayUtils.isNotEmpty(stackTrace)) {
            result.stackTrace(stackTrace);
        }

        return result;
    }

    public static <T> Result<T> success(String message, int code, int status, T data) {
        return create(message, null, code, status, data, null);
    }

    public static <T> Result<T> success(String message, int code, T data) {
        return success(message, code, HttpStatus.SC_OK, data);
    }

    public static <T> Result<T> success(String message, T data) {
        return success(message, ErrorCodes.OK.getSequence(), data);
    }

    public static <T> Result<T> success(String message) {
        return success(message, null);
    }

    public static <T> Result<T> success() {
        return success("操作成功！");
    }

    public static <T> Result<T> content(T data) {
        return success("操作成功！", data);
    }

    public static <T> Result<T> failure(String message, String detail, int code, int status, T data, StackTraceElement[] stackTrace) {
        return create(message, detail, code, status, data, stackTrace);
    }

    public static <T> Result<T> failure(String message, String detail, int code, int status, T data) {
        return failure(message, detail, code, status, data, null);
    }

    public static <T> Result<T> failure(String message, int code, int status, T data) {
        return failure(message, message, code, status, data);
    }

    public static <T> Result<T> failure(String message, String detail, int code, T data) {
        return failure(message, detail, code, HttpStatus.SC_INTERNAL_SERVER_ERROR, data);
    }

    public static <T> Result<T> failure(String message, int code, T data) {
        return failure(message, message, code, data);
    }

    public static Result<String> failure(Feedback feedback) {
        return failure(feedback, "");
    }

    public static <T> Result<T> failure(Feedback feedback, T data) {
        Feedback result = ObjectUtils.isNotEmpty(feedback) ? feedback : ErrorCodes.DISCOVERED_UNRECORDED_ERROR_EXCEPTION;
        Integer code = ErrorCodeMapper.get(result);
        return failure(feedback.getMessage(), code, feedback.getStatus(), data);
    }

    public static <T> Result<T> failure(String message, T data) {
        return failure(message, ErrorCodes.INTERNAL_SERVER_ERROR.getSequence(), data);
    }

    public static <T> Result<T> failure(String message) {
        return failure(message, null);
    }

    public static <T> Result<T> failure() {
        return failure("操作失败！");
    }

    public static <T> Result<T> empty(String message, int code, int status) {
        return create(message, null, code, status, null, null);
    }

    public static <T> Result<T> empty(String message, int code) {
        // 不再支持 204 No Content 状态码。204 用于 options 请求，放在其它类型请求时，Axios 会将其视为错误，并将请求 Cancel 状态
        return empty(message, code, ErrorCodes.OK.getStatus());
    }

    public static <T> Result<T> empty(Feedback feedback) {
        int code = ErrorCodeMapper.get(feedback);
        return empty(feedback.getMessage(), code, feedback.getStatus());
    }

    public static <T> Result<T> empty(String message) {
        // 不再支持 204 No Content 状态码。204 用于 options 请求，放在其它类型请求时，Axios 会将其视为错误，并将请求 Cancel 状态
        return empty(message, ErrorCodes.OK.getSequence());
    }

    public static <T> Result<T> empty() {
        return empty("未查询到相关内容！");
    }

    public String getPath() {
        return path;
    }

    public int getStatus() {
        return status;
    }

    public String getTraceId() {
        return traceId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Error getError() {
        return error;
    }

    public Result<T> code(int code) {
        setCode(code);
        return this;
    }

    public Result<T> message(String message) {
        setMessage(message);
        return this;
    }

    public Result<T> data(T data) {
        setData(data);
        return this;
    }

    public Result<T> path(String path) {
        this.path = path;
        return this;
    }

    public Result<T> type(Feedback feedback) {
        setCode(ErrorCodeMapper.get(feedback));
        setMessage(feedback.getMessage());
        this.status = feedback.getStatus();
        return this;
    }

    public Result<T> status(int httpStatus) {
        this.status = httpStatus;
        return this;
    }

    public Result<T> traceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public Result<T> stackTrace(StackTraceElement[] stackTrace) {
        this.error.setStackTrace(stackTrace);
        return this;
    }

    public Result<T> detail(String detail) {
        this.error.setDetail(detail);
        return this;
    }

    public Result<T> validation(String message, String code, String field) {
        this.error.setMessage(message);
        this.error.setCode(code);
        this.error.setField(field);
        return this;
    }

    private Map<String, Object> createModel() {
        Map<String, Object> result = new HashMap<>(8);
        result.put("code", getCode());
        result.put("message", getMessage());
        result.put("path", path);
        result.put("status", status);
        result.put("timestamp", timestamp);
        return result;
    }

    public Map<String, Object> toModel() {
        Map<String, Object> result = createModel();
        result.put("data", getData());
        result.put("error", error);
        return result;
    }

    public Map<String, Object> toErrorModel() {
        Map<String, Object> result = createModel();
        result.put("exception", getData());
        result.put("error", ObjectUtils.isNotEmpty(getError()) ? getError().getDetail() : StringUtils.EMPTY);
//        result.put("trace", ObjectUtils.isNotEmpty(getError()) ? getError().getStackTrace() : null);
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("code", getCode())
                .add("message", getMessage())
                .add("path", path)
                .add("data", getData())
                .add("status", status)
                .add("timestamp", timestamp)
                .add("error", error)
                .toString();
    }
}
