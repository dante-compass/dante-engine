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

package cn.herodotus.engine.rest.servlet.upms.controller.security;

import cn.herodotus.dante.core.domain.Result;
import cn.herodotus.dante.data.jpa.service.BaseJpaWriteableService;
import cn.herodotus.dante.logic.upms.entity.security.SysDictionary;
import cn.herodotus.dante.logic.upms.service.security.SysDictionaryService;
import cn.herodotus.engine.web.api.servlet.AbstractJpaWriteableController;
import cn.herodotus.engine.web.core.annotation.AccessLimited;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.collections4.MapUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>Description: 数据字典管理接口 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/8/24 16:28
 */
@RestController
@RequestMapping("/security/dictionary")
@Tags({
        @Tag(name = "用户安全管理接口"),
        @Tag(name = "数据字典管理接口")
})
public class SysDictionaryController extends AbstractJpaWriteableController<SysDictionary, String> {

    private final SysDictionaryService sysDictionaryService;

    public SysDictionaryController(SysDictionaryService sysDictionaryService) {
        this.sysDictionaryService = sysDictionaryService;
    }

    @Override
    public BaseJpaWriteableService<SysDictionary, String> getService() {
        return sysDictionaryService;
    }

    @Operation(summary = "模糊条件查询数据字典", description = "根据动态输入的字段模糊查询数据字典",
            responses = {@ApiResponse(description = "数据字典列表", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Map.class)))})
    @Parameters({
            @Parameter(name = "pageNumber", required = true, description = "当前页码"),
            @Parameter(name = "pageSize", required = true, description = "每页显示数量"),
            @Parameter(name = "category", description = "字典分类"),
            @Parameter(name = "name", description = "字典枚举字面量"),
            @Parameter(name = "label", description = "字典枚举显示值")
    })
    @GetMapping("/condition")
    public Result<Map<String, Object>> findByCondition(
            @NotNull @RequestParam("pageNumber") Integer pageNumber,
            @NotNull @RequestParam("pageSize") Integer pageSize,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "label", required = false) String label) {
        Page<SysDictionary> pages = sysDictionaryService.findByCondition(pageNumber, pageSize, category, name, label);
        return resultFromPage(pages);
    }

    @AccessLimited
    @Operation(summary = "根据字典分类查询数据字典", description = "根据字典分类查询数据字典",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            responses = {@ApiResponse(description = "操作消息", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @Parameters({
            @Parameter(name = "category", required = true, in = ParameterIn.PATH, description = "字典分类")
    })
    @GetMapping("/items/{category}")
    public Result<List<SysDictionary>> findAllByCategory(@PathVariable String category) {
        List<SysDictionary> dictionaries = sysDictionaryService.findAllByCategory(category);
        return result(dictionaries);
    }

    @AccessLimited
    @Operation(summary = "根据字典分类查询数据字典", description = "根据字典分类查询数据字典",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            responses = {@ApiResponse(description = "操作消息", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @Parameters({
            @Parameter(name = "categories", required = true, description = "多个字典分类，以逗号分隔的字符串")
    })
    @GetMapping("/items")
    public Result<Map<String, List<SysDictionary>>> findByCategories(@RequestParam("categories") Set<String> categories) {
        Map<String, List<SysDictionary>> dictionaries = sysDictionaryService.findByCategories(categories);

        if (null == dictionaries) {
            return Result.failure("查询数据失败！");
        }

        if (MapUtils.isNotEmpty(dictionaries)) {
            return Result.success("查询数据成功！", dictionaries);
        } else {
            return Result.empty("未查询到数据！");
        }
    }
}
