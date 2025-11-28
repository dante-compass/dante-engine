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

package cn.herodotus.engine.data.core.jpa.entity;


import cn.herodotus.dante.core.constant.SystemConstants;
import cn.herodotus.engine.core.definition.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

/**
 * <p>Description: 通用的基础实体定义 </p>
 * <p>
 * 该类仅包含数据库审计所需的 create_time 和 update_time 两个字段
 *
 * @author : gengwei.zheng
 * @date : 2024/1/29 19:16
 */
@MappedSuperclass
public abstract class AbstractEntity implements BaseEntity {

    @Schema(name = "数据创建时间", title = "数据库审计通用字段", description = "由 JPA 自动填充无需手动设置。改字段不允许更新")
    @Column(name = "create_time", updatable = false)
    @CreatedDate
    @JsonFormat(pattern = SystemConstants.DATE_TIME_FORMAT)
    private Date createTime = new Date();

    @Schema(name = "数据更新时间", title = "数据库审计通用字段", description = "由 JPA 自动填充无需手动设置")
    @Column(name = "update_time")
    @LastModifiedDate
    @JsonFormat(pattern = SystemConstants.DATE_TIME_FORMAT)
    private Date updateTime = new Date();

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}
