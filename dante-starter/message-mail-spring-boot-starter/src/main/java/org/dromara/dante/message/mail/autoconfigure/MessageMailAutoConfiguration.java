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

package org.dromara.dante.message.mail.autoconfigure;

import jakarta.annotation.PostConstruct;
import org.dromara.dante.message.autoconfigure.mail.IntegrationMailAutoConfiguration;
import org.dromara.dante.message.commons.constant.Channels;
import org.dromara.dante.message.mail.autoconfigure.listener.MailNotificationListener;
import org.dromara.dante.message.mail.autoconfigure.properties.EmailProperties;
import org.dromara.dante.message.mail.autoconfigure.stamp.EmailVerificationCodeStampManager;
import org.dromara.dante.message.mail.autoconfigure.transformer.EventToMailTransformer;
import org.dromara.dante.spring.condition.ConditionalOnSpringMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * <p>Description: Mail Message 自动配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/6/16 10:46
 */
@AutoConfiguration(after = IntegrationMailAutoConfiguration.class)
@ConditionalOnSpringMail
@EnableConfigurationProperties(EmailProperties.class)
public class MessageMailAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MessageMailAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Starter [Message Mail] Configure.");
    }

    @Bean
    public EventToMailTransformer eventToMailTransformer(JavaMailSender javaMailSender, SpringTemplateEngine springTemplateEngine) {
        EventToMailTransformer transformer = new EventToMailTransformer(javaMailSender, springTemplateEngine);
        log.trace("[Herodotus] |- Bean [Event To Thymeleaf Mail Transformer] Configure.");
        return transformer;
    }

    @Bean
    public IntegrationFlow eventToMailTransformerFlow(EventToMailTransformer eventToMailTransformer) {
        return IntegrationFlow
                .from(Channels.MAIL__EVENT_INBOUND_CHANNEL)
                .transform(eventToMailTransformer)
                .channel(Channels.MAIL__DEFAULT_SENDING_CHANNEL)
                .get();
    }

    @Bean
    @ConditionalOnMissingBean
    public EmailVerificationCodeStampManager emailVerificationCodeStampManager(EmailProperties emailProperties) {
        EmailVerificationCodeStampManager manager = new EmailVerificationCodeStampManager(emailProperties);
        log.trace("[Herodotus] |- Bean [Email Verification Code Stamp Manager] Configure.");
        return manager;
    }

    @Bean
    @ConditionalOnMissingBean
    public MailNotificationListener mailNotificationListener(EmailVerificationCodeStampManager emailVerificationCodeStampManager, EmailProperties emailProperties) {
        MailNotificationListener listener = new MailNotificationListener(emailVerificationCodeStampManager, emailProperties);
        log.trace("[Herodotus] |- Bean [Mail Notification Listener] Configure.");
        return listener;
    }
}
