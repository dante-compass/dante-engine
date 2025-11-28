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

package cn.herodotus.dante.assistant.access.stamp;

import cn.herodotus.dante.assistant.access.constant.AccessConstants;
import cn.herodotus.dante.assistant.access.properties.SmsProperties;
import cn.herodotus.engine.cache.jetcache.stamp.AbstractStampManager;
import cn.hutool.v7.core.util.RandomUtil;

/**
 * <p>Description: 手机短信验证码签章 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/8/26 17:44
 */
public class VerificationCodeStampManager extends AbstractStampManager<String, String> {

    private SmsProperties smsProperties;

    public VerificationCodeStampManager(SmsProperties smsProperties) {
        super(AccessConstants.CACHE_NAME_TOKEN_VERIFICATION_CODE, smsProperties.getExpire());
    }

    public void setSmsProperties(SmsProperties smsProperties) {
        this.smsProperties = smsProperties;
    }

    @Override
    public String nextStamp(String key) {
        if (smsProperties.getSandbox()) {
            return smsProperties.getTestCode();
        } else {
            return RandomUtil.randomNumbers(smsProperties.getLength());
        }
    }

    public Boolean getSandbox() {
        return smsProperties.getSandbox();
    }

    public String getVerificationCodeTemplateId() {
        return smsProperties.getVerificationCodeTemplateId();
    }
}
