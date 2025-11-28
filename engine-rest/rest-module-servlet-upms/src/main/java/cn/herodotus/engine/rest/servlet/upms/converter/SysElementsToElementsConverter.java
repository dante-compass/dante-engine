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

package cn.herodotus.engine.rest.servlet.upms.converter;

import cn.herodotus.dante.core.constant.SystemConstants;
import cn.herodotus.dante.logic.upms.entity.security.SysElement;
import cn.herodotus.dante.logic.upms.enums.ElementCategory;
import cn.herodotus.engine.rest.servlet.upms.dto.Elements;
import cn.hutool.v7.core.tree.MapTree;
import cn.hutool.v7.core.tree.TreeNode;
import cn.hutool.v7.core.tree.TreeUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>Description: 将 {@link SysElement} 集合，转成成 {@link Elements} </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/11/21 21:25
 */
public class SysElementsToElementsConverter implements Converter<List<SysElement>, Elements> {

    private final Converter<SysElement, TreeNode<String>> toTreeNode;

    public SysElementsToElementsConverter() {
        this.toTreeNode = new SysElementToTreeNodeConverter();
    }

    @Override
    public Elements convert(List<SysElement> source) {

        Map<ElementCategory, List<SysElement>> groupedElements = source.stream().collect(Collectors.groupingBy(SysElement::getElementCategory));

        Elements elements = new Elements();
        elements.setMenus(toTreeNodes(groupedElements.get(ElementCategory.MENU)));
        elements.setButtons(elements.getButtons());

        return elements;
    }

    private List<MapTree<String>> toTreeNodes(List<SysElement> sysElements) {
        if (ObjectUtils.isNotEmpty(sysElements)) {
            List<TreeNode<String>> treeNodes = sysElements.stream().map(toTreeNode::convert).collect(Collectors.toList());
            return TreeUtil.build(treeNodes, SystemConstants.TREE_ROOT_ID);
        } else {
            return new ArrayList<>();
        }
    }
}
