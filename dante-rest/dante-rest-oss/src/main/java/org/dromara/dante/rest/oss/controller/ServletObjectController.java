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

package org.dromara.dante.rest.oss.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.servlet.http.HttpServletResponse;
import org.dromara.dante.assistant.oss.converter.result.ResponseToPutObjectResultConverter;
import org.dromara.dante.assistant.oss.definition.converter.ResponseConverter;
import org.dromara.dante.assistant.oss.entity.argument.*;
import org.dromara.dante.assistant.oss.entity.result.*;
import org.dromara.dante.assistant.oss.service.base.S3TransferManagerService;
import org.dromara.dante.assistant.oss.service.servlet.ServletObjectService;
import org.dromara.dante.core.domain.Result;
import org.dromara.dante.spring.exception.oss.DownloadObjectException;
import org.dromara.dante.spring.exception.oss.UploadObjectException;
import org.dromara.dante.rest.oss.service.ServletObjectStreamService;
import org.dromara.dante.web.annotation.AccessLimited;
import org.dromara.dante.web.annotation.Idempotent;
import org.dromara.dante.web.definition.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.BlockingInputStreamAsyncRequestBody;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.transfer.s3.model.CompletedUpload;
import software.amazon.awssdk.transfer.s3.model.UploadRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * <p>Description: 阻塞式对象存储Object管理接口 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/26 21:19
 */
@RestController
@RequestMapping("/oss/object")
@Tags({
        @Tag(name = "对象存储管理接口"),
        @Tag(name = "阻塞式对象存储管理接口"),
        @Tag(name = "阻塞式对象存储Object管理接口")
})
public class ServletObjectController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(ServletObjectController.class);

    private final ServletObjectService servletObjectService;
    private final ServletObjectStreamService servletObjectStreamService;
    private final S3TransferManagerService s3TransferManagerService;

    public ServletObjectController(ServletObjectService servletObjectService, ServletObjectStreamService servletObjectStreamService, S3TransferManagerService s3TransferManagerService) {
        this.servletObjectService = servletObjectService;
        this.servletObjectStreamService = servletObjectStreamService;
        this.s3TransferManagerService = s3TransferManagerService;
    }

    @AccessLimited
    @Operation(summary = "获取对象列表V2", description = "获取对象列表V2",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            responses = {
                    @ApiResponse(description = "所有对象", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Result.class))),
                    @ApiResponse(responseCode = "200", description = "查询成功，查到数据"),
                    @ApiResponse(responseCode = "500", description = "查询失败"),
                    @ApiResponse(responseCode = "503", description = "Server无法访问或未启动")
            })
    @Parameters({
            @Parameter(name = "argument", required = true, description = "ListObjectsV2Argument参数实体", schema = @Schema(implementation = ListObjectsV2Argument.class))
    })
    @GetMapping("/list")
    public Result<ListObjectsV2Result> listObjects(@Validated ListObjectsV2Argument argument) {
        ListObjectsV2Result domain = servletObjectService.listObjectsV2(argument);
        return result(domain);
    }

    @Idempotent
    @Operation(summary = "删除一个对象", description = "根据传入的参数对指定对象进行删除",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            responses = {
                    @ApiResponse(description = "所有对象", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Result.class))),
                    @ApiResponse(responseCode = "200", description = "操作成功"),
                    @ApiResponse(responseCode = "500", description = "操作失败，具体查看错误信息内容"),
                    @ApiResponse(responseCode = "503", description = "Server无法访问或未启动")
            })
    @Parameters({
            @Parameter(name = "argument", required = true, description = "删除对象请求参数实体", schema = @Schema(implementation = DeleteObjectArgument.class))
    })
    @DeleteMapping
    public Result<DeleteObjectResult> deleteObject(@Validated @RequestBody DeleteObjectArgument argument) {
        DeleteObjectResult result = servletObjectService.deleteObject(argument);
        return result(result);
    }

    @Idempotent
    @Operation(summary = "删除多个对象", description = "根据传入的参数对指定多个对象进行删除",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            responses = {
                    @ApiResponse(description = "返回删除操作出错对象的具体信息", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Result.class))),
                    @ApiResponse(responseCode = "200", description = "查询成功，查到数据"),
                    @ApiResponse(responseCode = "500", description = "查询失败"),
                    @ApiResponse(responseCode = "503", description = "Server无法访问或未启动")
            })
    @Parameters({
            @Parameter(name = "argument", required = true, description = "批量删除对象请求参数实体", schema = @Schema(implementation = DeleteObjectsArgument.class))
    })
    @DeleteMapping("/multi")
    public Result<DeleteObjectsResult> deleteObjects(@Validated @RequestBody DeleteObjectsArgument argument) {
        DeleteObjectsResult result = servletObjectService.deleteObjects(argument);
        return result(result);
    }

    @Idempotent
    @Operation(summary = "流式文件上传", description = "以文件流的方式上传文件",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)),
            responses = {
                    @ApiResponse(description = "所有对象", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PutObjectResult.class))),
                    @ApiResponse(responseCode = "200", description = "操作成功"),
                    @ApiResponse(responseCode = "500", description = "操作失败"),
                    @ApiResponse(responseCode = "503", description = "Minio Server无法访问或未启动")
            })
    @Parameters({
            @Parameter(name = "bucketName", required = true, description = "存储桶名称"),
            @Parameter(name = "file", required = true, description = "文件", schema = @Schema(implementation = MultipartFile.class))
    })
    @PostMapping("/upload")
    public Result<PutObjectResult> upload(@RequestParam(value = "bucketName") String bucketName, @RequestPart(value = "file") MultipartFile file) {

        try {
            // 创建异步请求体（length如果为空会报错）
            BlockingInputStreamAsyncRequestBody body = AsyncRequestBody.forBlockingInputStream(file.getSize());

            UploadRequest uploadRequest = UploadRequest.builder()
                    .requestBody(body)
                    .putObjectRequest(r -> r
                            .bucket(bucketName)
                            .key(file.getOriginalFilename())
                            .contentLength(file.getSize())
                            .contentType(file.getContentType())
                    )
                    .build();

            CompletableFuture<CompletedUpload> future = s3TransferManagerService.upload(uploadRequest);
            // upload 操作要在 writeInputStream 之前执行，否则会出现没有 doBlockingWrite 错误
            body.writeInputStream(new ByteArrayInputStream(file.getBytes()));

            ResponseConverter<PutObjectResponse, PutObjectResult> response = new ResponseToPutObjectResultConverter();
            PutObjectResult result = response.convert(future.join().response());
            return result(result);
        } catch (IOException e) {
            log.error("[Herodotus] |- Upload object to OSS bucket [{}] failed", bucketName, e);
            throw new UploadObjectException(e.getMessage());
        }
    }

    @Idempotent
    @Operation(summary = "流式下载", description = "后台返回响应流，下载对应的对象",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            responses = {
                    @ApiResponse(description = "所有对象", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "200", description = "操作成功"),
                    @ApiResponse(responseCode = "500", description = "操作失败"),
                    @ApiResponse(responseCode = "503", description = "Server无法访问或未启动")
            })
    @Parameters({
            @Parameter(name = "arguments", required = true, description = "GetObjectArgument参数实体", schema = @Schema(implementation = GetObjectArgument.class))
    })
    @PostMapping("/download")
    public void download(@Validated @RequestBody GetObjectArgument arguments, HttpServletResponse response) {
        try {
            servletObjectStreamService.download(arguments, response);
        } catch (IOException e) {
            log.error("[Herodotus] |- Download object [{}] from OSS bucket [{}] catch error", arguments.getObjectName(), arguments.getBucketName(), e);
            throw new DownloadObjectException(e.getMessage());
        }
    }

    @Idempotent
    @Operation(summary = "流式打开", description = "后台返回响应流，直接打开对应的对象",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            responses = {
                    @ApiResponse(description = "所有对象", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "200", description = "操作成功"),
                    @ApiResponse(responseCode = "500", description = "操作失败"),
                    @ApiResponse(responseCode = "503", description = "Server无法访问或未启动")
            })
    @Parameters({
            @Parameter(name = "arguments", required = true, description = "GetObjectArgument参数实体", schema = @Schema(implementation = GetObjectArgument.class))
    })
    @PostMapping("/display")
    public void display(@Validated @RequestBody GetObjectArgument arguments, HttpServletResponse response) {
        try {
            servletObjectStreamService.display(arguments, response);
        } catch (IOException e) {
            log.error("[Herodotus] |- Display bucket [{}] object [{}] catch error", arguments.getBucketName(), arguments.getObjectName(), e);
            throw new DownloadObjectException(e.getMessage());
        }
    }

    @AccessLimited
    @Operation(summary = "获取对象属性", description = "该接口为非 S3 官方接口，在对象官方信息基础上扩展了更多信息",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            responses = {
                    @ApiResponse(description = "所有对象", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Result.class))),
                    @ApiResponse(responseCode = "200", description = "查询成功，查到数据"),
                    @ApiResponse(responseCode = "500", description = "查询失败"),
                    @ApiResponse(responseCode = "503", description = "Server无法访问或未启动")
            })
    @Parameters({
            @Parameter(name = "argument", required = true, description = "获取对象属性请求参数实体", schema = @Schema(implementation = GetObjectAttributesArgument.class))
    })
    @GetMapping("/attributes")
    public Result<GetObjectAttributesResult> getObjectAttributes(@Validated GetObjectAttributesArgument argument) {
        GetObjectAttributesResult domain = servletObjectService.getObjectAttributes(argument);
        return result(domain);
    }

    @Idempotent
    @Operation(summary = "变更对象留存状态", description = "该接口仅在存储桶开启了 Object Lock 才会生效",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            responses = {
                    @ApiResponse(description = "所有对象", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Result.class))),
                    @ApiResponse(responseCode = "200", description = "操作成功"),
                    @ApiResponse(responseCode = "500", description = "操作失败，具体查看错误信息内容"),
                    @ApiResponse(responseCode = "503", description = "Server 无法访问或未启动")
            })
    @Parameters({
            @Parameter(name = "argument", required = true, description = "设置对象留存请求参数实体", schema = @Schema(implementation = PutObjectLegalHoldArgument.class))
    })
    @PutMapping("/legalhold")
    public Result<PutObjectLegalHoldResult> putObjectLegalHold(@Validated @RequestBody PutObjectLegalHoldArgument argument) {
        PutObjectLegalHoldResult result = servletObjectService.putObjectLegalHold(argument);
        return result(result);
    }

    @Idempotent
    @Operation(summary = "变更对象保留色湖之", description = "该接口仅在存储桶开启了 Object Lock 才会生效",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            responses = {
                    @ApiResponse(description = "所有对象", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Result.class))),
                    @ApiResponse(responseCode = "200", description = "操作成功"),
                    @ApiResponse(responseCode = "500", description = "操作失败，具体查看错误信息内容"),
                    @ApiResponse(responseCode = "503", description = "Server 无法访问或未启动")
            })
    @Parameters({
            @Parameter(name = "argument", required = true, description = "变更对象保留设置请求参数实体", schema = @Schema(implementation = PutObjectRetentionArgument.class))
    })
    @PutMapping("/retention")
    public Result<PutObjectRetentionResult> putObjectRetention(@Validated @RequestBody PutObjectRetentionArgument argument) {
        PutObjectRetentionResult result = servletObjectService.putObjectRetention(argument);
        return result(result);
    }

    @AccessLimited
    @Operation(summary = "获取某个对象的版本信息", description = "需要存储桶开启对象锁定以及版本控制",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            responses = {
                    @ApiResponse(description = "所有Buckets", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Result.class))),
                    @ApiResponse(responseCode = "200", description = "查询成功，查到数据"),
                    @ApiResponse(responseCode = "500", description = "查询失败"),
                    @ApiResponse(responseCode = "503", description = "Server无法访问或未启动")
            })
    @Parameters({
            @Parameter(name = "argument", description = "对象版本列表请求参数实体", schema = @Schema(implementation = ListObjectVersionsArgument.class))
    })
    @GetMapping("/versions")
    public Result<ListObjectVersionsResult> listVersions(ListObjectVersionsArgument argument) {
        ListObjectVersionsResult results = servletObjectService.listObjectVersions(argument);
        return result(results);
    }
}
