/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2020-2030 郑庚伟 ZHENGGENGWEI (码匠君), <herodotus@aliyun.com> Licensed under the AGPL License
 *
 * This file is part of Herodotus Engine.
 *
 * Herodotus Engine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Herodotus Engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.herodotus.cn>.
 */

package cn.herodotus.engine.oauth2.management.controller;

import cn.herodotus.engine.assistant.definition.domain.Result;
import cn.herodotus.engine.data.core.service.WriteableService;
import cn.herodotus.engine.oauth2.management.entity.OAuth2Compliance;
import cn.herodotus.engine.oauth2.management.service.OAuth2ComplianceService;
import cn.herodotus.engine.rest.core.controller.BaseWriteableRestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>Description: OAuth2ComplianceController </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/7/7 22:56
 */
@RestController
@RequestMapping("/authorize/compliance")
@Tags({
        @Tag(name = "OAuth2 认证服务接口"),
        @Tag(name = "OAuth2 应用安全合规接口"),
        @Tag(name = "OAuth2 审计管理接口")
})
public class OAuth2ComplianceController extends BaseWriteableRestController<OAuth2Compliance, String> {

    private final OAuth2ComplianceService complianceService;

    @Autowired
    public OAuth2ComplianceController(OAuth2ComplianceService complianceService) {
        this.complianceService = complianceService;
    }

    @Override
    public WriteableService<OAuth2Compliance, String> getWriteableService() {
        return complianceService;
    }

    @Operation(summary = "模糊条件查询合规信息", description = "根据动态输入的字段模糊查询合规信息",
            responses = {@ApiResponse(description = "人员分页列表", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))})
    @Parameters({
            @Parameter(name = "pageNumber", required = true, description = "当前页码"),
            @Parameter(name = "pageSize", required = true, description = "每页显示数量"),
            @Parameter(name = "principalName", description = "用户账号"),
            @Parameter(name = "clientId", description = "客户端ID"),
            @Parameter(name = "ip", description = "IP地址"),
    })
    @GetMapping("/condition")
    public Result<Map<String, Object>> findByCondition(@NotNull @RequestParam("pageNumber") Integer pageNumber,
                                                       @NotNull @RequestParam("pageSize") Integer pageSize,
                                                       @RequestParam(value = "principalName", required = false) String principalName,
                                                       @RequestParam(value = "clientId", required = false) String clientId,
                                                       @RequestParam(value = "ip", required = false) String ip) {
        Page<OAuth2Compliance> pages = complianceService.findByCondition(pageNumber, pageSize, principalName, clientId, ip);
        return result(pages);
    }
}
