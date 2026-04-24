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

package org.dromara.dante.persistence.sas.autoconfigure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.apache.commons.lang3.ArrayUtils;
import org.dromara.dante.core.domain.Result;
import org.dromara.dante.data.rest.servlet.PaginationController;
import org.dromara.dante.persistence.commons.definition.EnhanceOAuth2AuthorizationService;
import org.dromara.dante.persistence.commons.domain.HerodotusAuthorizationDetails;
import org.dromara.dante.web.annotation.AccessLimited;
import org.dromara.dante.web.annotation.Idempotent;
import org.dromara.dante.web.definition.dto.Pager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>Description: OAuth2 认证管理接口 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/3/1 18:52
 */
@RestController
@RequestMapping("/authorize/authorization")
@Tags({
        @Tag(name = "OAuth2 认证服务接口"),
        @Tag(name = "OAuth2 认证管理接口")
})
public class OAuth2AuthorizationController implements PaginationController {

    private final OAuth2AuthorizationService authorizationService;

    public OAuth2AuthorizationController(OAuth2AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    private EnhanceOAuth2AuthorizationService getEnhanceOAuth2AuthorizationService() {
        return (EnhanceOAuth2AuthorizationService) authorizationService;
    }

    @Idempotent
    @Operation(summary = "删除认证信息", description = "即删除Token信息让Token失效。这里没有使用SAS提供的吊销接口是为了规避系统内存储过多Token 信息 ",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            responses = {@ApiResponse(description = "操作消息", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @Parameters({
            @Parameter(name = "id", required = true, in = ParameterIn.PATH, description = "实体ID，@Id注解对应的实体属性")
    })
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable String id) {
        Result<String> result = result(String.valueOf(id));
        getEnhanceOAuth2AuthorizationService().deleteById(id);
        return result;
    }

    @AccessLimited
    @Operation(summary = "分页查询认证信息数据", description = "通过pageNumber和pageSize获取分页数据",
            responses = {
                    @ApiResponse(description = "认证信息列表", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "500", description = "查询失败")
            })
    @Parameters({
            @Parameter(name = "pager", required = true, in = ParameterIn.QUERY, description = "分页Bo对象", schema = @Schema(implementation = Pager.class))
    })
    @GetMapping
    public Result<Map<String, Object>> findByPage(@Validated Pager pager) {
        Page<HerodotusAuthorizationDetails> details;
        if (ArrayUtils.isNotEmpty(pager.getProperties())) {
            Sort.Direction direction = Sort.Direction.valueOf(pager.getDirection());
            details = getEnhanceOAuth2AuthorizationService().findByPage(pager.getPageNumber(), pager.getPageSize(), direction, pager.getProperties());
        } else {
            details = getEnhanceOAuth2AuthorizationService().findByPage(pager.getPageNumber(), pager.getPageSize());
        }
        return resultFromPage(details);
    }
}
