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

package cn.herodotus.dante.rest.servlet.message.controller;

import cn.herodotus.dante.core.domain.Result;
import cn.herodotus.dante.data.jpa.service.BaseJpaWriteableService;
import cn.herodotus.dante.logic.message.entity.DialogueDetail;
import cn.herodotus.dante.logic.message.service.DialogueDetailService;
import cn.herodotus.engine.web.api.servlet.AbstractJpaWriteableController;
import cn.herodotus.engine.web.core.annotation.Idempotent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>Description: DialogueDetailController </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/12/17 12:49
 */
@RestController
@RequestMapping("/message/dialogue/detail")
@Tags({
        @Tag(name = "消息管理接口"),
        @Tag(name = "私信管理接口"),
        @Tag(name = "私信详情管理接口")
})
public class DialogueDetailController extends AbstractJpaWriteableController<DialogueDetail, String> {

    private final DialogueDetailService dialogueDetailService;

    public DialogueDetailController(DialogueDetailService dialogueDetailService) {
        this.dialogueDetailService = dialogueDetailService;
    }

    @Override
    public BaseJpaWriteableService<DialogueDetail, String> getService() {
        return dialogueDetailService;
    }

    @Operation(summary = "条件查询私信详情分页数据", description = "根据输入的字段条件查询详情信息",
            responses = {@ApiResponse(description = "详情列表", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Map.class)))})
    @Parameters({
            @Parameter(name = "pageNumber", required = true, description = "当前页码", schema = @Schema(type = "integer")),
            @Parameter(name = "pageSize", required = true, description = "每页显示数量", schema = @Schema(type = "integer")),
            @Parameter(name = "dialogueId", required = true, description = "对话ID"),
    })
    @GetMapping("/condition")
    public Result<Map<String, Object>> findByCondition(
            @NotNull @RequestParam("pageNumber") Integer pageNumber,
            @NotNull @RequestParam("pageSize") Integer pageSize,
            @NotBlank @RequestParam("dialogueId") String dialogueId) {
        Page<DialogueDetail> pages = dialogueDetailService.findByCondition(pageNumber, pageSize, dialogueId);
        return resultFromPage(pages);
    }

    @Idempotent
    @Operation(summary = "根据dialogueId删除私信整个对话", description = "根据实体dialogueId删除私信整个对话，包括相关联的关联数据",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            responses = {@ApiResponse(description = "操作消息", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @Parameters({
            @Parameter(name = "id", required = true, in = ParameterIn.PATH, description = "DialogueId 关联私信联系人和私信详情的ID")
    })
    @DeleteMapping("/dialogue/{id}")
    public Result<String> deleteDialogueById(@PathVariable String id) {
        dialogueDetailService.deleteDialogueById(id);
        return Result.success("删除成功");
    }
}
