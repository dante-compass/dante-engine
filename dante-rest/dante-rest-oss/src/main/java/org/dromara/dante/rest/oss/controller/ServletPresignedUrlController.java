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
import org.dromara.dante.assistant.oss.service.manager.PresignedUrlService;
import org.dromara.dante.core.domain.Result;
import org.dromara.dante.web.annotation.Idempotent;
import org.dromara.dante.web.definition.Controller;
import org.dromara.dante.web.definition.dto.OssPresigned;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>Description: 阻塞式对象存储预签名URL管理接口 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/10/22 16:26
 */
@RestController
@RequestMapping("/oss/presigned")
@Tags({
        @Tag(name = "对象存储管理接口"),
        @Tag(name = "阻塞式对象存储管理接口"),
        @Tag(name = "阻塞式对象存储预签名URL管理接口")
})
public class ServletPresignedUrlController implements Controller {

    private final PresignedUrlService presignedUrlService;

    public ServletPresignedUrlController(PresignedUrlService presignedUrlService) {
        this.presignedUrlService = presignedUrlService;
    }

    @Idempotent
    @Operation(summary = "创建预签名上传URL", description = "创建预签名上传URL",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            responses = {
                    @ApiResponse(description = "所以返回200即表示成功，不成功会抛错", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    @Parameters({
            @Parameter(name = "argument", required = true, description = "预签名上传请求参数实体", schema = @Schema(implementation = OssPresigned.class))
    })
    @PostMapping("/upload")
    public Result<String> upload(@Validated @RequestBody OssPresigned argument) {
        String url = presignedUrlService.createUploadPresignedUrl(argument.getBucketName(), argument.getObjectName());
        return result(url);
    }

    @Idempotent
    @Operation(summary = "创建预签名上传URL", description = "创建预签名上传URL",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            responses = {
                    @ApiResponse(description = "所以返回200即表示成功，不成功会抛错", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    @Parameters({
            @Parameter(name = "argument", required = true, description = "预签名上传请求参数实体", schema = @Schema(implementation = OssPresigned.class))
    })
    @PostMapping("/download")
    public Result<String> download(@Validated @RequestBody OssPresigned argument) {
        String url = presignedUrlService.createDownloadPresignedUrl(argument.getBucketName(), argument.getObjectName());
        return result(url);
    }

    @Idempotent
    @Operation(summary = "创建预签名删除URL", description = "创建预签名删除URL",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            responses = {
                    @ApiResponse(description = "所以返回200即表示成功，不成功会抛错", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    @Parameters({
            @Parameter(name = "argument", required = true, description = "预签名删除请求参数实体", schema = @Schema(implementation = OssPresigned.class))
    })
    @DeleteMapping
    public Result<String> delete(@Validated @RequestBody OssPresigned argument) {
        String url = presignedUrlService.createDeletePresignedUrl(argument.getBucketName(), argument.getObjectName());
        return result(url);
    }
}
