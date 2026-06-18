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

package org.dromara.dante.web.autoconfigure.support;

import cn.herodotus.dante.core.constant.SystemConstants;
import cn.herodotus.dante.core.domain.SignatureValidationResult;
import cn.herodotus.dante.core.enums.SignatureMethod;
import cn.herodotus.dante.core.utils.SignatureUtils;
import org.dromara.dante.web.autoconfigure.stamp.SignatureRandomStampManager;
import org.dromara.dante.web.definition.SignatureValidator;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

/**
 * <p>Description: 默认的签名校验器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/9/25 9:30
 */
public class DefaultSignatureValidator implements SignatureValidator {

    private final SignatureRandomStampManager signatureRandomStampManager;

    public DefaultSignatureValidator(SignatureRandomStampManager signatureRandomStampManager) {
        this.signatureRandomStampManager = signatureRandomStampManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SignatureValidationResult validate(String key, SignatureMethod method, Map<String, String> content, String signature) {

        if (content.containsKey(SystemConstants.KEY__TIMESTAMP)) {
            Long timestamp = Long.parseLong(content.get(SystemConstants.KEY__TIMESTAMP));
            boolean isWithinRange = validateTimestamp(timestamp);
            if (!isWithinRange) {
                return SignatureValidationResult.invalid("Timestamp validation failed");
            }
        }

        if (content.containsKey(SystemConstants.KEY__RANDOM)) {
            String random = content.get(SystemConstants.KEY__RANDOM);
            boolean isRandomUsed = signatureRandomStampManager.exist(random);
            if (!isRandomUsed) {
                signatureRandomStampManager.create(random);
            } else {
                return SignatureValidationResult.invalid("Random validation failed");
            }
        }

        return SignatureUtils.validate(key, method, content, signature);
    }

    /**
     * 校验 timestamp 值是否在指定时间范围内。
     * <p>
     * <p>
     * 假设客户端时间戳是 1719392065000（2024年6月25日 10:54:25 UTC），服务器时间戳是 1719392965000（2024年6月25日 11:09:25 UTC），时间差为15分钟（在允许范围内）
     *
     * @param timestamp 时间戳参数
     * @return 是否在时间范围内，在时间范围内返回 ture，否则返回 false
     */
    private boolean validateTimestamp(Long timestamp) {
        Duration range = signatureRandomStampManager.getExpire();
        Instant currentTimestamp = Instant.ofEpochMilli(System.currentTimeMillis());
        Instant signatureTimestamp = Instant.ofEpochMilli(timestamp);

        // 因为是时间范围，between() 的结果可以为正也可以为负
        // abs() 确保我们得到的是两个时间点之间的绝对时间间隔，而不考虑哪个时间在前。
        Duration actualDuration = Duration.between(currentTimestamp, signatureTimestamp).abs();
        return !range.minus(actualDuration).isNegative();
    }
}
