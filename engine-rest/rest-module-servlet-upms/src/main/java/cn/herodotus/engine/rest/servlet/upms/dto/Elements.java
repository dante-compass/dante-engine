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

package cn.herodotus.engine.rest.servlet.upms.dto;

import cn.herodotus.dante.core.domain.BaseDto;
import cn.herodotus.dante.logic.upms.entity.security.SysElement;
import cn.hutool.v7.core.tree.MapTree;
import com.google.common.base.MoreObjects;

import java.util.List;

/**
 * <p>Description: 前端页面元素 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/11/21 21:25
 */
public class Elements implements BaseDto {

    private List<MapTree<String>> menus;

    private List<SysElement> buttons;

    public List<MapTree<String>> getMenus() {
        return menus;
    }

    public void setMenus(List<MapTree<String>> menus) {
        this.menus = menus;
    }

    public List<SysElement> getButtons() {
        return buttons;
    }

    public void setButtons(List<SysElement> buttons) {
        this.buttons = buttons;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("menus", menus)
                .add("buttons", buttons)
                .toString();
    }
}
