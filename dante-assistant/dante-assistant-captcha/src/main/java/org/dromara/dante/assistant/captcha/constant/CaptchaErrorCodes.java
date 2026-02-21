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

package org.dromara.dante.assistant.captcha.constant;

import org.dromara.dante.core.feedback.NotAcceptableFeedback;

/**
 * <p>Description: Captcha 模块错误代码 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/1/10 23:07
 */
public interface CaptchaErrorCodes {

    NotAcceptableFeedback CAPTCHA_CATEGORY_IS_INCORRECT = new NotAcceptableFeedback("验证码分类错误");
    NotAcceptableFeedback CAPTCHA_HANDLER_NOT_EXIST = new NotAcceptableFeedback("验证码处理器不存在");
    NotAcceptableFeedback CAPTCHA_HAS_EXPIRED = new NotAcceptableFeedback("验证码已过期");
    NotAcceptableFeedback CAPTCHA_IS_EMPTY = new NotAcceptableFeedback("验证码不能为空");
    NotAcceptableFeedback CAPTCHA_MISMATCH = new NotAcceptableFeedback("验证码不匹配");
    NotAcceptableFeedback CAPTCHA_PARAMETER_ILLEGAL = new NotAcceptableFeedback("验证码参数格式错误");
}

