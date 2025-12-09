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

package org.dromara.dante.web.autoconfigure.secure;

import org.dromara.dante.web.annotation.Idempotent;
import org.dromara.dante.web.exception.RepeatSubmissionException;
import org.dromara.dante.web.autoconfigure.stamp.IdempotentStampManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.format.DateTimeParseException;

/**
 * <p>Description: 幂等处理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/4/30 19:41
 */
public class IdempotentHandler {

    private static final Logger log = LoggerFactory.getLogger(AccessLimitedHandler.class);

    public static boolean handle(String key, Idempotent idempotent, String url, IdempotentStampManager idempotentStampManager) {
        // 幂等性校验, 根据缓存中是否存在Token进行校验。
        // 如果缓存中没有Token，通过放行, 同时在缓存中存入Token。
        // 如果缓存中有Token，意味着同一个操作反复操作，认为失败则抛出异常, 并通过统一异常处理返回友好提示
        if (StringUtils.isNotBlank(key)) {
            String token = idempotentStampManager.get(key);
            if (StringUtils.isBlank(token)) {
                Duration configuredDuration = Duration.ZERO;
                String annotationExpire = idempotent.expire();
                if (StringUtils.isNotBlank(annotationExpire)) {
                    try {
                        configuredDuration = Duration.parse(annotationExpire);
                    } catch (DateTimeParseException e) {
                        log.warn("[Herodotus] |- Idempotent duration value is incorrect, on api [{}].", url);
                    }
                }

                if (!configuredDuration.isZero()) {
                    idempotentStampManager.create(key, configuredDuration);
                } else {
                    idempotentStampManager.create(key);
                }

                return true;
            } else {
                throw new RepeatSubmissionException("Don't Repeat Submission");
            }
        }
        return true;
    }
}
