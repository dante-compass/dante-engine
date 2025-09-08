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
 * along with this program.  If not, see <https://www.herodotus.vip>.
 */

package cn.herodotus.engine.core.autoconfigure.oauth2.definition;

import cn.herodotus.engine.core.autoconfigure.oauth2.OAuth2AuthorizationProperties;
import cn.herodotus.engine.core.autoconfigure.oauth2.domain.HerodotusRequest;
import cn.herodotus.engine.core.definition.constant.SecurityResources;
import cn.herodotus.engine.core.definition.utils.ListUtils;
import cn.herodotus.engine.core.identity.domain.HerodotusSecurityAttribute;
import cn.herodotus.engine.core.identity.enums.PermissionExpression;
import org.apache.commons.collections4.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 静态安全资源配置器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/1/26 21:15
 */
public abstract class AbstractSecurityMatcherConfigurer {

    private final OAuth2AuthorizationProperties authorizationProperties;
    private final List<String> staticResources;
    private final List<String> permitAllResources;
    private final List<String> hasAuthenticatedResources;
    private final Map<HerodotusRequest, List<HerodotusSecurityAttribute>> permitAllAttributes;

    protected AbstractSecurityMatcherConfigurer(OAuth2AuthorizationProperties authorizationProperties) {
        this.authorizationProperties = authorizationProperties;
        this.staticResources = ListUtils.merge(authorizationProperties.getMatcher().getStaticResources(), SecurityResources.DEFAULT_IGNORED_STATIC_RESOURCES);
        this.permitAllResources = ListUtils.merge(authorizationProperties.getMatcher().getPermitAll(), SecurityResources.DEFAULT_PERMIT_ALL_RESOURCES);
        this.hasAuthenticatedResources = ListUtils.merge(authorizationProperties.getMatcher().getHasAuthenticated(), SecurityResources.DEFAULT_HAS_AUTHENTICATED_RESOURCES);
        this.permitAllAttributes = createPermitAllAttributes(permitAllResources);
    }

    /**
     * 获取 SecurityFilterChain 中配置的 RequestMatchers 信息。
     * <p>
     * RequestMatchers 可以理解为“静态配置”，将其与平台后端的“动态配置”有机结合。同时，防止因动态配置导致的静态配置失效的问题。
     * <p>
     * 目前只处理了 permitAll 类型。其它内容根据后续使用情况再行添加。
     *
     * @return RequestMatchers 中配置的权限数据
     */
    private Map<HerodotusRequest, List<HerodotusSecurityAttribute>> createPermitAllAttributes(List<String> permitAllResources) {
        if (CollectionUtils.isNotEmpty(permitAllResources)) {
            Map<HerodotusRequest, List<HerodotusSecurityAttribute>> result = new LinkedHashMap<>();
            permitAllResources.forEach(item -> {
                result.put(new HerodotusRequest(item), List.of(new HerodotusSecurityAttribute(PermissionExpression.PERMIT_ALL.getValue())));
            });
            return result;
        }
        return new LinkedHashMap<>();
    }

    public boolean isStrictMode() {
        return authorizationProperties.getStrict();
    }

    protected List<String> getStaticResources() {
        return staticResources;
    }

    protected List<String> getPermitAllResources() {
        return permitAllResources;
    }

    protected List<String> getHasAuthenticatedResources() {
        return hasAuthenticatedResources;
    }

    public Map<HerodotusRequest, List<HerodotusSecurityAttribute>> getPermitAllAttributes() {
        return permitAllAttributes;
    }
}
