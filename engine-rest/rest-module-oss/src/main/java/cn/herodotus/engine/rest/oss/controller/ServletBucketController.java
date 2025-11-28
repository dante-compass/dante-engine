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

package cn.herodotus.engine.rest.oss.controller;

import cn.herodotus.engine.assistant.oss.entity.argument.CreateBucketArgument;
import cn.herodotus.engine.assistant.oss.entity.argument.DeleteBucketArgument;
import cn.herodotus.engine.assistant.oss.entity.argument.ListBucketsArgument;
import cn.herodotus.engine.assistant.oss.entity.result.CreateBucketResult;
import cn.herodotus.engine.assistant.oss.entity.result.DeleteBucketResult;
import cn.herodotus.engine.assistant.oss.entity.result.ListBucketsResult;
import cn.herodotus.engine.assistant.oss.service.servlet.ServletBucketService;
import cn.herodotus.dante.core.domain.Result;
import cn.herodotus.engine.web.core.annotation.AccessLimited;
import cn.herodotus.engine.web.core.annotation.Idempotent;
import cn.herodotus.engine.web.core.definition.Controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>Description: 阻塞式对象存储Bucket管理接口 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/22 18:16
 */
@RestController
@RequestMapping("/oss/bucket")
@Tags({
        @Tag(name = "对象存储管理接口"),
        @Tag(name = "阻塞式对象存储管理接口"),
        @Tag(name = "阻塞式对象存储Bucket管理接口")
})
public class ServletBucketController implements Controller {

    private final ServletBucketService servletBucketService;

    public ServletBucketController(ServletBucketService servletBucketService) {
        this.servletBucketService = servletBucketService;
    }


    @Idempotent
    @Operation(summary = "创建存储桶", description = "创建存储桶接口，该接口仅是创建，不包含是否已存在检查",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            responses = {
                    @ApiResponse(description = "Minio API 无返回值，所以返回200即表示成功，不成功会抛错", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "200", description = "操作成功"),
                    @ApiResponse(responseCode = "500", description = "操作失败，具体查看错误信息内容"),
                    @ApiResponse(responseCode = "503", description = "Server无法访问或未启动")
            })
    @Parameters({
            @Parameter(name = "argument", required = true, description = "CreateBucketArgument请求参数实体", schema = @Schema(implementation = CreateBucketArgument.class))
    })
    @PostMapping
    public Result<CreateBucketResult> createBucket(@Validated @RequestBody CreateBucketArgument argument) {
        CreateBucketResult bucket = servletBucketService.createBucket(argument);
        return result(bucket);
    }

    @Idempotent
    @Operation(summary = "删除存储桶", description = "根据存储桶名称删除数据，可指定 Region",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            responses = {
                    @ApiResponse(description = "Minio API 无返回值，所以返回200即表示成功，不成功会抛错", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "200", description = "操作成功"),
                    @ApiResponse(responseCode = "500", description = "操作失败，具体查看错误信息内容"),
                    @ApiResponse(responseCode = "503", description = "Server 无法访问或未启动")
            })
    @Parameters({
            @Parameter(name = "argument", required = true, description = "DeleteBucketArguments请求参数实体", schema = @Schema(implementation = DeleteBucketArgument.class))
    })
    @DeleteMapping
    public Result<DeleteBucketResult> deleteBucket(@Validated @RequestBody DeleteBucketArgument argument) {
        DeleteBucketResult result = servletBucketService.deleteBucket(argument);
        return result(result);
    }

    @AccessLimited
    @Operation(summary = "获取全部存储桶(Bucket)", description = "获取全部存储桶(Bucket)",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            responses = {
                    @ApiResponse(description = "所有Buckets", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Result.class))),
                    @ApiResponse(responseCode = "200", description = "查询成功，查到数据"),
                    @ApiResponse(responseCode = "204", description = "查询成功，未查到数据"),
                    @ApiResponse(responseCode = "500", description = "查询失败"),
                    @ApiResponse(responseCode = "503", description = "Server无法访问或未启动")
            })
    @Parameters({
            @Parameter(name = "argument", description = "CListBucketsArgument请求参数实体", schema = @Schema(implementation = ListBucketsArgument.class))
    })
    @GetMapping("/list")
    public Result<ListBucketsResult> listBuckets(ListBucketsArgument argument) {
        ListBucketsResult buckets = servletBucketService.listBuckets(argument);
        return result(buckets);
    }
}
