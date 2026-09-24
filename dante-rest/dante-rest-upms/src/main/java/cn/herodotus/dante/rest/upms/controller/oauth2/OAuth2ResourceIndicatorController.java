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

package cn.herodotus.dante.rest.upms.controller.oauth2;

import cn.herodotus.dante.core.domain.Result;
import cn.herodotus.dante.data.jpa.service.BaseJpaWriteableService;
import cn.herodotus.dante.data.rest.servlet.AbstractJpaEntityWriteableController;
import cn.herodotus.dante.logic.upms.entity.oauth2.OAuth2ResourceIndicator;
import cn.herodotus.dante.logic.upms.service.oauth2.OAuth2ResourceIndicatorService;
import cn.herodotus.dante.web.annotation.AccessLimited;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>Description: OAuth2 资源标识接口 </p>
 *
 * @author : gengwei_zheng
 * @date : 2026/9/20 13:58
 */

@RestController
@RequestMapping("/authorize/indicator")
@Tags({
        @Tag(name = "OAuth2 资源服务接口"),
        @Tag(name = "OAuth2 资源标识接口")
})
public class OAuth2ResourceIndicatorController extends AbstractJpaEntityWriteableController<OAuth2ResourceIndicator, String> {

    private final OAuth2ResourceIndicatorService oauth2ResourceIndicatorService;

    public OAuth2ResourceIndicatorController(OAuth2ResourceIndicatorService oauth2ResourceIndicatorService) {
        this.oauth2ResourceIndicatorService = oauth2ResourceIndicatorService;
    }

    @Override
    public BaseJpaWriteableService<OAuth2ResourceIndicator, String> getService() {
        return oauth2ResourceIndicatorService;
    }

    @AccessLimited
    @Operation(summary = "根据资源标识值查询资源标识", description = "根据输入的资源标识值，查询对应的资源标识",
            responses = {
                    @ApiResponse(description = "查询到的资源标识", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = OAuth2ResourceIndicator.class))),
                    @ApiResponse(responseCode = "500", description = "查询失败")
            }
    )
    @Parameters({
            @Parameter(name = "indicatorValue", in = ParameterIn.PATH, required = true, description = "资源标识值"),
    })
    @GetMapping("/{indicatorValue}")
    public Result<OAuth2ResourceIndicator> findByIndicatorValue(@PathVariable("indicatorValue") String indicatorValue) {
        OAuth2ResourceIndicator domain = oauth2ResourceIndicatorService.findByIndicatorValue(indicatorValue);
        return result(domain);
    }

    @AccessLimited
    @Operation(summary = "获取全部资源标识接口", description = "获取全部资源标识接口",
            responses = {@ApiResponse(description = "资源标识列表", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Result.class)))})
    @GetMapping("/list")
    public Result<List<OAuth2ResourceIndicator>> findAll() {
        List<OAuth2ResourceIndicator> indicators = oauth2ResourceIndicatorService.findAll();
        return result(indicators);
    }
}
