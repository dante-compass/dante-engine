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

package cn.herodotus.engine.logic.upms.converter;

import cn.herodotus.engine.core.foundation.founction.ListConverter;
import cn.herodotus.engine.core.identity.domain.AttributeTransmitter;
import cn.herodotus.engine.logic.upms.entity.security.SysAttribute;
import cn.herodotus.engine.logic.upms.entity.security.SysPermission;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * <p>Description: SysAttribute 转 SecurityAttribute 转换器</p>
 *
 * @author : gengwei.zheng
 * @date : 2023/8/23 22:59
 */
public class SysAttributeToAttributeTransmitterConverter implements ListConverter<SysAttribute, AttributeTransmitter> {
    @Override
    public AttributeTransmitter from(SysAttribute source) {
        AttributeTransmitter target = new AttributeTransmitter();
        target.setAttributeId(source.getAttributeId());
        target.setAttributeCode(source.getAttributeCode());
        target.setWebExpression(source.getWebExpression());
        target.setPermissions(permissionToCommaDelimitedString(source.getPermissions()));
        target.setUrl(source.getUrl());
        target.setRequestMethod(source.getRequestMethod());
        target.setServiceId(source.getServiceId());
        target.setAttributeName(source.getDescription());
        return target;
    }

    private String permissionToCommaDelimitedString(Set<SysPermission> sysAuthorities) {
        if (CollectionUtils.isNotEmpty(sysAuthorities)) {
            List<String> codes = sysAuthorities.stream().map(SysPermission::getPermissionCode).toList();
            return StringUtils.collectionToCommaDelimitedString(codes);
        } else {
            return "";
        }
    }
}
