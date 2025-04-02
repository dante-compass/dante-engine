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

package cn.herodotus.engine.rest.core.definition.dto;

import cn.herodotus.engine.data.core.enums.DataItemStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>Description: 框架基础实体通用基础类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/3/18 15:47
 */
public class BaseSysDto extends BaseDto {

    @Schema(name = "数据状态")
    private DataItemStatus status = DataItemStatus.ENABLE;

    @Schema(name = "是否为保留数据", description = "True 为不能删，False为可以删除")
    private Boolean reserved = Boolean.FALSE;

    @Schema(name = "版本号")
    private Integer reversion = 0;

    /**
     * 角色描述,UI界面显示使用
     */
    @Schema(name = "备注")
    private String description;

    public DataItemStatus getStatus() {
        return status;
    }

    public void setStatus(DataItemStatus status) {
        this.status = status;
    }

    public Boolean getReserved() {
        return reserved;
    }

    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    public Integer getReversion() {
        return reversion;
    }

    public void setReversion(Integer reversion) {
        this.reversion = reversion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
