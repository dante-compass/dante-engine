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

package org.dromara.dante.web.definition;

import cn.herodotus.dante.core.domain.SignatureValidationResult;
import cn.herodotus.dante.core.enums.SignatureMethod;

import java.util.Map;

/**
 * <p>Description: 签名校验器定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/9/25 10:10
 */
public interface SignatureValidator {

    /**
     * 验证签名。
     * <p>
     * 增加了 timestamp 和 random 参数的校验，主要为了抵御重放攻击。
     * 重放攻击：攻击者窃听到一次有效的通信数据包（包括签名），然后原封不动地重新发送它，以冒充合法设备或重复执行某个操作。
     * 无论是 timestamp 还是 random，都是为了确保每个请求都是新鲜、唯一的，使得攻击者无法重复使用旧的请求数据
     * <p>
     * Timestamp：时效性。服务器使用客户端传来的 timestamp 计算签名，确保了签名输入数据与客户端完全一致，即使服务器和客户端时间有微小偏差（在允许范围内），签名也能匹配。
     * 问题：
     * 1. 依赖时间同步：如果设备时间与服务器时间偏差过大，合法请求会被拒绝。
     * 2. 时间窗口内仍可重放：在允许的±15分钟内，攻击者理论上可以重放数据包。
     * Random：唯一性：客户端生成一个足够长且不可预测的随机数，服务器检查该随机数是否最近未曾出现过（例如，在缓存或数据库中查询）。
     * 1. 服务器需要维护状态：服务器需要存储或缓存已使用过的随机数，直到其超时。
     * 2. 存储开销：在高并发场景下，需要维护一个较大的“已使用随机数集合”
     * 所以，使用 timestamp 和 random 组合方式兼顾了效率和极致的安全性。random 存入服务器缓存中，增加 ttl 有效时间，定时清除可以减少存储问题。
     *
     * @param key       签名密钥
     * @param method    签名算法 {@link SignatureMethod}
     * @param content   参数 {@link Map}
     * @param signature 待验证签名
     * @return 验证结果 {@link SignatureValidationResult}
     */
    SignatureValidationResult validate(String key, SignatureMethod method, Map<String, String> content, String signature);
}
