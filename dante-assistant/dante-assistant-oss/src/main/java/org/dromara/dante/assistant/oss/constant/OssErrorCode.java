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

package org.dromara.dante.assistant.oss.constant;

import org.dromara.dante.core.feedback.BadRequestFeedback;
import org.dromara.dante.core.feedback.ConflictFeedback;
import org.dromara.dante.core.feedback.ForbiddenFeedback;
import org.dromara.dante.core.feedback.NotFoundFeedback;

/**
 * <p>Description: 对象存储模块错误代码 </p>
 *
 * @author : gengwei.zheng
 * @date : 2026/2/9 20:49
 */
public interface OssErrorCode {

    BadRequestFeedback INVALID_REQUEST_EXCEPTION = new BadRequestFeedback("无效的 OSS 请求");

    ForbiddenFeedback ACCESS_DENIED_EXCEPTION = new ForbiddenFeedback("您没有权限，拒绝访问当前 OSS");

    ConflictFeedback BUCKET_ALREADY_EXISTS_EXCEPTION = new ConflictFeedback("OSS 存储桶已存在");
    ConflictFeedback BUCKET_ALREADY_OWNED_BY_YOU_EXCEPTION = new ConflictFeedback("该 OSS 账户下已存在同名存储桶");
    ConflictFeedback INVALID_OBJECT_STATE_EXCEPTION = new ConflictFeedback("无效的 OSS 对象状态");

    NotFoundFeedback NO_SUCH_BUCKET_EXCEPTION = new NotFoundFeedback("OSS 中没有该存储桶");
    NotFoundFeedback NO_SUCH_KEY_EXCEPTION = new NotFoundFeedback("OSS 中没有该对象");
    NotFoundFeedback NO_SUCH_UPLOAD_EXCEPTION = new NotFoundFeedback("OSS 中没有该上传");
}
