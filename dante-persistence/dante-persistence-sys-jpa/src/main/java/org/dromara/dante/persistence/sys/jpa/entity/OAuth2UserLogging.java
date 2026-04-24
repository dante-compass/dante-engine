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

package org.dromara.dante.persistence.sys.jpa.entity;

import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.dromara.dante.persistence.commons.constant.PersistenceConstants;
import org.dromara.dante.persistence.sys.jpa.definition.AbstractOAuth2AuditRecord;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;

import java.util.Objects;

/**
 * <p>Description: 用户操作审计 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/7/7 18:55
 */
@Entity
@Table(name = "oauth2_user_logging", indexes = {@Index(name = "oauth2_user_logging_id_idx", columnList = "logging_id")})
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = PersistenceConstants.REGION_OAUTH2_USER_LOGGING)
public class OAuth2UserLogging extends AbstractOAuth2AuditRecord {

    @Id
    @UuidGenerator
    @Column(name = "logging_id", length = 64)
    private String loggingId;

    @Schema(name = "具体操作")
    @Column(name = "operation", length = 50)
    private String operation;

    @Schema(name = "登录位置", description = "由 Ip2Region 解析出的地址信息")
    @Column(name = "location", length = 200)
    private String location;

    public String getLoggingId() {
        return loggingId;
    }

    public void setLoggingId(String complianceId) {
        this.loggingId = complianceId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OAuth2UserLogging that = (OAuth2UserLogging) o;
        return Objects.equals(loggingId, that.loggingId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(loggingId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("loggingId", loggingId)
                .add("operation", operation)
                .add("location", location)
                .toString();
    }
}
