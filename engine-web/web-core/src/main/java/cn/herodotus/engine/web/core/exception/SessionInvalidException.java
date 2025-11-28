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

package cn.herodotus.engine.web.core.exception;

import cn.herodotus.dante.core.domain.Feedback;
import cn.herodotus.dante.core.exception.PlatformRuntimeException;
import cn.herodotus.engine.web.core.constant.WebErrorCodes;

/**
 * <p>Description: Session 不可用错误 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/10/4 16:46
 */
public class SessionInvalidException extends PlatformRuntimeException {

    public SessionInvalidException() {
        super();
    }

    public SessionInvalidException(String message) {
        super(message);
    }

    public SessionInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionInvalidException(Throwable cause) {
        super(cause);
    }

    public SessionInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public Feedback getFeedback() {
        return WebErrorCodes.SESSION_INVALID;
    }
}
