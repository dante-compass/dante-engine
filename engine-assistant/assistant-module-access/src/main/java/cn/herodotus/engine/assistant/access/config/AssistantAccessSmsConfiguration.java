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

package cn.herodotus.engine.assistant.access.config;

import cn.herodotus.engine.assistant.access.condition.ConditionalOnSms;
import cn.herodotus.engine.assistant.access.processor.PhoneNumberAccessHandler;
import cn.herodotus.engine.assistant.access.properties.SmsProperties;
import cn.herodotus.engine.assistant.access.stamp.VerificationCodeStampManager;
import cn.herodotus.engine.core.identity.enums.AccountCategory;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: 发送短信统一配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/5/25 12:03
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnSms
@EnableConfigurationProperties({SmsProperties.class})
public class AssistantAccessSmsConfiguration {

    private static final Logger log = LoggerFactory.getLogger(AssistantAccessSmsConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- Module [Assistant Access SMS] Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    public VerificationCodeStampManager verificationCodeStampManager(SmsProperties smsProperties) {
        VerificationCodeStampManager verificationCodeStampManager = new VerificationCodeStampManager();
        verificationCodeStampManager.setSmsProperties(smsProperties);
        log.trace("[Herodotus] |- Bean [Verification Code Stamp Manager] Auto Configure.");
        return verificationCodeStampManager;
    }

    @Bean(AccountCategory.PHONE_NUMBER_HANDLER)
    public PhoneNumberAccessHandler phoneNumberAccessHandler(VerificationCodeStampManager verificationCodeStampManager) {
        PhoneNumberAccessHandler phoneNumberAuthenticationHandler = new PhoneNumberAccessHandler(verificationCodeStampManager);
        log.trace("[Herodotus] |- Bean [Phone Number SignIn Handler] Auto Configure.");
        return phoneNumberAuthenticationHandler;
    }
}
