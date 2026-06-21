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

package cn.herodotus.dante.data.mongodb.entity;

import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

/**
 * <p>Description: MongoDB 通用核心 Entity </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/2/26 20:30
 */
public abstract class AbstractAuditEntity extends AbstractEntity {

    public abstract String getId();

    public abstract void setId(String id);

    @Schema(name = "创建人")
    @CreatedBy
    private String createBy;

    @Schema(name = "最后修改人")
    @LastModifiedBy
    private String updateBy;

    @Schema(name = "版本号")
    private Integer reversion = 0;

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getReversion() {
        return reversion;
    }

    public void setReversion(Integer reversion) {
        this.reversion = reversion;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("createBy", createBy)
                .add("updateBy", updateBy)
                .add("reversion", reversion)
                .addValue(super.toString())
                .toString();
    }
}
