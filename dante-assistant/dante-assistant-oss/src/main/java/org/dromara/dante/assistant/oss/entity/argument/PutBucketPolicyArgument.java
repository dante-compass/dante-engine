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

package org.dromara.dante.assistant.oss.entity.argument;

import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.dromara.dante.assistant.oss.definition.argument.AbstractBucketContentMd5Argument;

/**
 * <p>Description: 设置存储桶策略请求参数实体 </p>
 *
 * @author : gengwei.zheng
 * @date : 2026/2/5 17:24
 */
@Schema(name = "设置存储桶策略请求参数实体", title = "设置存储桶策略请求参数实体")
public class PutBucketPolicyArgument extends AbstractBucketContentMd5Argument {

    @Schema(name = "是否为公开权限")
    @NotNull(message = "是否为公开权限值不能为空")
    private Boolean doesPublic;

    @Schema(name = "是否删除用户自己的存储桶策略")
    private Boolean confirmRemoveSelfBucketAccess;

    public Boolean getDoesPublic() {
        return doesPublic;
    }

    public void setDoesPublic(Boolean doesPublic) {
        this.doesPublic = doesPublic;
    }

    public Boolean getConfirmRemoveSelfBucketAccess() {
        return confirmRemoveSelfBucketAccess;
    }

    public void setConfirmRemoveSelfBucketAccess(Boolean confirmRemoveSelfBucketAccess) {
        this.confirmRemoveSelfBucketAccess = confirmRemoveSelfBucketAccess;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("doesPublic", doesPublic)
                .add("confirmRemoveSelfBucketAccess", confirmRemoveSelfBucketAccess)
                .addValue(super.toString())
                .toString();
    }
}
