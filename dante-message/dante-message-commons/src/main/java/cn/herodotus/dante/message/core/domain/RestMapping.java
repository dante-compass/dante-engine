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

package cn.herodotus.dante.message.core.domain;

import cn.herodotus.dante.security.domain.AbstractRest;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * <p>Description: Controller 请求注解元数据封装实体 </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/6/2 19:52
 */
public class RestMapping extends AbstractRest {

    private String mappingId;

    private String mappingCode;

    private String className;

    private String methodName;

    private String description;

    public RestMapping() {
    }

    public String getMappingId() {
        return mappingId;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }

    public String getMappingCode() {
        return mappingCode;
    }

    public void setMappingCode(String mappingCode) {
        this.mappingCode = mappingCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RestMapping that = (RestMapping) o;
        return Objects.equal(mappingId, that.mappingId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mappingId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("mappingId", mappingId)
                .add("mappingCode", mappingCode)
                .add("className", className)
                .add("methodName", methodName)
                .add("description", description)
                .addValue(super.toString())
                .toString();
    }
}
