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
import cn.herodotus.engine.logic.upms.entity.security.SysAttribute;
import cn.herodotus.engine.logic.upms.entity.security.SysInterface;

/**
 * <p>Description: List<SysInterface> 转 List<SysAttribute> 转换器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/8/23 22:59
 */
public class SysInterfacesToSysAttributesConverter implements ListConverter<SysInterface, SysAttribute> {

    @Override
    public SysAttribute from(SysInterface source) {
        SysAttribute target = new SysAttribute();
        target.setAttributeId(source.getInterfaceId());
        target.setAttributeCode(source.getInterfaceCode());
        target.setRequestMethod(source.getRequestMethod());
        target.setServiceId(source.getServiceId());
        target.setClassName(source.getClassName());
        target.setMethodName(source.getMethodName());
        target.setUrl(source.getUrl());
        target.setStatus(source.getStatus());
        target.setReserved(source.getReserved());
        target.setDescription(source.getDescription());
        target.setReversion(source.getReversion());
        target.setCreateTime(source.getCreateTime());
        target.setUpdateTime(source.getUpdateTime());
        target.setRanking(source.getRanking());
        return target;
    }
}
