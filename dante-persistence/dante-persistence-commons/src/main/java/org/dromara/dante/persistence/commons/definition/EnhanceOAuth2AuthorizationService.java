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

package org.dromara.dante.persistence.commons.definition;

import org.dromara.dante.persistence.commons.domain.HerodotusAuthorizationDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;

import java.util.List;

/**
 * <p>Description: 扩展 SAS 默认 OAuth2AuthorizationService 接口 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/3/14 19:19
 */
public interface EnhanceOAuth2AuthorizationService extends OAuth2AuthorizationService {

    /**
     * 查询某个用户的认证（Token）数据量
     *
     * @param registeredClientId 客户端ID
     * @param principalName      用户唯一标识
     * @return 数量
     */
    int findAuthorizationCount(String registeredClientId, String principalName);

    /**
     * 选项用户当前有效的所有认证（Token）
     *
     * @param registeredClientId 客户端ID
     * @param principalName      用户唯一标识
     * @return 有效的认证（Token） 列表
     */
    List<OAuth2Authorization> findAvailableAuthorizations(String registeredClientId, String principalName);

    /**
     * 根据ID删除数据
     *
     * @param id 数据对应ID
     */
    void deleteById(String id);

    /**
     * 查询分页数据
     *
     * @param pageNumber 当前页码, 起始页码 0
     * @param pageSize   每页显示的数据条数
     * @param direction  {@link Sort.Direction}
     * @param properties 排序的属性名称
     * @return 分页数据
     */
    Page<HerodotusAuthorizationDetails> findByPage(int pageNumber, int pageSize, Sort.Direction direction, String... properties);

    /**
     * 查询分页数据
     *
     * @param pageNumber 当前页码, 起始页码 0
     * @param pageSize   每页显示的数据条数
     * @return 分页数据
     */
    Page<HerodotusAuthorizationDetails> findByPage(int pageNumber, int pageSize);
}
