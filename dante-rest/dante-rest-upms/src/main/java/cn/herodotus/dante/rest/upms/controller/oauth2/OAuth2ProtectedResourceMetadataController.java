/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2020-2030 郑庚伟 ZHENGGENGWEI (码匠君), <herodotus@aliyun.com> Licensed under the AGPL License
 *
 * This file is part of Herodotus Stirrup.
 *
 * Herodotus Stirrup is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Herodotus Stirrup is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.herodotus.cn>.
 */

package cn.herodotus.dante.rest.upms.controller.oauth2;

import cn.herodotus.dante.data.jpa.service.BaseJpaWriteableService;
import cn.herodotus.dante.data.rest.servlet.AbstractJpaEntityWriteableController;
import cn.herodotus.dante.logic.upms.entity.oauth2.OAuth2ProtectedResourceMetadata;
import cn.herodotus.dante.logic.upms.service.oauth2.OAuth2ProtectedResourceMetadataService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Description: OAuth2 保护资源接口 </p>
 *
 * @author : gengwei_zheng
 * @date : 2026/9/20 15:30
 */
@RestController
@RequestMapping("/authorize/prm")
@Tags({
        @Tag(name = "OAuth2 资源服务接口"),
        @Tag(name = "OAuth2 保护资源接口")
})
public class OAuth2ProtectedResourceMetadataController extends AbstractJpaEntityWriteableController<OAuth2ProtectedResourceMetadata, String> {

    private final OAuth2ProtectedResourceMetadataService oauth2ProtectedResourceMetadataService;

    public OAuth2ProtectedResourceMetadataController(OAuth2ProtectedResourceMetadataService oauth2ProtectedResourceMetadataService) {
        this.oauth2ProtectedResourceMetadataService = oauth2ProtectedResourceMetadataService;
    }

    @Override
    public BaseJpaWriteableService<OAuth2ProtectedResourceMetadata, String> getService() {
        return oauth2ProtectedResourceMetadataService;
    }
}
