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

package cn.herodotus.engine.web.core.definition;

import cn.herodotus.engine.core.definition.constant.SystemConstants;
import cn.herodotus.engine.core.definition.domain.BaseDomain;
import cn.herodotus.engine.core.definition.domain.Pagination;
import cn.herodotus.engine.core.definition.domain.Result;
import cn.hutool.v7.core.tree.MapTree;
import cn.hutool.v7.core.tree.TreeNode;
import cn.hutool.v7.core.tree.TreeUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p> Description : Controller基础定义 </p>
 * <p>
 * 这里只在方法上做了泛型，主要是考虑到返回的结果数据可以是各种类型，而不一定受限于某一种类型。
 *
 * @author : gengwei.zheng
 * @date : 2024/1/30 1:06
 */
public interface Controller extends Pagination {

    /**
     * 数据实体转换为统一响应实体
     *
     * @param domain 数据实体
     * @param <D>    {@link BaseDomain} 子类型
     * @return {@link Result} Entity
     */
    default <D extends BaseDomain> Result<D> result(D domain) {
        if (ObjectUtils.isNotEmpty(domain)) {
            return Result.content(domain);
        } else {
            return Result.empty();
        }
    }

    /**
     * 数据列表转换为统一响应实体
     *
     * @param domains 数据实体 List
     * @param <D>     {@link BaseDomain} 子类型
     * @return {@link Result} List
     */
    default <D extends BaseDomain> Result<List<D>> result(List<D> domains) {

        if (null == domains) {
            return Result.failure("查询数据失败！");
        }

        if (CollectionUtils.isNotEmpty(domains)) {
            return Result.success("查询数据成功！", domains);
        } else {
            return Result.empty("未查询到数据！");
        }
    }

    /**
     * 数组转换为统一响应实体
     *
     * @param domains 数组
     * @param <T>     数组类型
     * @return {@link Result} List
     */
    default <T> Result<T[]> result(T[] domains) {

        if (null == domains) {
            return Result.failure("查询数据失败！");
        }

        if (ArrayUtils.isNotEmpty(domains)) {
            return Result.success("查询数据成功！", domains);
        } else {
            return Result.empty("未查询到数据！");
        }
    }

    /**
     * 数据 Map 转换为统一响应实体
     *
     * @param map 数据 Map
     * @return {@link Result} Map
     */
    default Result<Map<String, Object>> result(Map<String, Object> map) {

        if (null == map) {
            return Result.failure("查询数据失败！");
        }

        if (MapUtils.isNotEmpty(map)) {
            return Result.success("查询数据成功！", map);
        } else {
            return Result.empty("未查询到数据！");
        }
    }

    /**
     * 数据操作结果转换为统一响应实体
     *
     * @param parameter 数据ID
     * @return {@link Result} String
     */
    default Result<String> result(String parameter) {
        if (ObjectUtils.isNotEmpty(parameter)) {
            return Result.success("操作成功!", parameter);
        } else {
            return Result.failure("操作失败!", parameter);
        }
    }

    /**
     * 数据操作结果转换为统一响应实体
     *
     * @param status 操作状态
     * @return {@link Result} String
     */
    default Result<Boolean> result(boolean status) {
        if (status) {
            return Result.success("操作成功!", true);
        } else {
            return Result.failure("操作失败!", false);
        }
    }

    default <D extends BaseDomain> Result<List<MapTree<String>>> result(List<D> domains, Converter<D, TreeNode<String>> toTreeNode) {
        if (ObjectUtils.isNotEmpty(domains)) {
            List<TreeNode<String>> treeNodes = domains.stream().map(toTreeNode::convert).collect(Collectors.toList());
            return Result.success("查询数据成功", TreeUtil.build(treeNodes, SystemConstants.TREE_ROOT_ID));
        } else {
            return Result.empty("未查询到数据！");
        }
    }
}
