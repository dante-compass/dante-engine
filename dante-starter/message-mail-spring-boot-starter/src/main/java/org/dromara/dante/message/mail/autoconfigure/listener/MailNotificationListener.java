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

package org.dromara.dante.message.mail.autoconfigure.listener;

import cn.hutool.v7.crypto.SecureUtil;
import org.apache.commons.lang3.StringUtils;
import org.dromara.dante.message.commons.definition.enums.MailNotificationAction;
import org.dromara.dante.message.commons.domain.MailMessage;
import org.dromara.dante.message.commons.domain.MailNotification;
import org.dromara.dante.message.commons.event.MailMessageSendingEvent;
import org.dromara.dante.message.commons.event.MailNotificationSendingEvent;
import org.dromara.dante.message.mail.autoconfigure.properties.EmailProperties;
import org.dromara.dante.message.mail.autoconfigure.stamp.EmailVerificationCodeStampManager;
import org.dromara.dante.spring.context.ServiceContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

/**
 * <p>Description: 内置 Mail 通知监听器</p>
 *
 * @author : gengwei.zheng
 * @date : 2024/10/28 22:09
 */
public class MailNotificationListener implements ApplicationListener<MailNotificationSendingEvent> {

    private static final Logger log = LoggerFactory.getLogger(MailNotificationListener.class);

    private static final String LOGO_LOCATION_PATTERN = "classpath*:/static/*.png";

    private final EmailVerificationCodeStampManager emailVerificationCodeStampManager;
    private final EmailProperties emailProperties;

    public MailNotificationListener(EmailVerificationCodeStampManager emailVerificationCodeStampManager, EmailProperties emailProperties) {
        this.emailVerificationCodeStampManager = emailVerificationCodeStampManager;
        this.emailProperties = emailProperties;
    }

    @Override
    public void onApplicationEvent(MailNotificationSendingEvent event) {

        log.info("[Herodotus] |- Mail notification listener, response event!");

        MailNotification mailNotification = event.getData();

        if (hasServiceAccount()) {
            MailMessage mailMessage = switch (mailNotification.getAction()) {
                case MailNotificationAction.EMAIL_VERIFICATION_CODE -> createVerificationCodeMessage(mailNotification);
                case MailNotificationAction.EMAIL_LOCATION_ABNORMALITY ->
                        createLocationAbnormalityMessage(mailNotification);
                default -> createConfirmMessage(mailNotification);
            };

            ServiceContextHolder.publishEvent(new MailMessageSendingEvent(mailMessage));
        }
    }

    private String getServiceAccount() {
        return emailProperties.getServiceAccount();
    }

    private boolean hasServiceAccount() {
        return StringUtils.isNotBlank(getServiceAccount());
    }

    private MailMessage createConfirmMessage(MailNotification mailNotification) {

        return MailMessage.builder()
                .from(getServiceAccount())
                .to(mailNotification.getTo())
                .subject("验证电子邮箱地址")
                .context("name", mailNotification.getUsername())
                .context("email", mailNotification.getUsername())
                .context("url", StringUtils.isNotBlank(emailProperties.getVerifyEmailUrl()) ? emailProperties.getVerifyEmailUrl() : "https://www.herodotus.cn")
                .template("email-confirm.html")
                .inline("herodotusLogo", LOGO_LOCATION_PATTERN)
                .build();
    }

    private MailMessage createVerificationCodeMessage(MailNotification mailNotification) {

        return MailMessage.builder()
                .from(getServiceAccount())
                .to(mailNotification.getTo())
                .subject("邮箱验证码")
                .context("name", mailNotification.getUsername())
                .context("code", emailVerificationCodeStampManager.nextStamp(SecureUtil.md5(mailNotification.getUsername())))
                .context("expire", emailVerificationCodeStampManager.getExpire().toMinutes())
                .template("email-verification-code.html")
                .inline("herodotusLogo", LOGO_LOCATION_PATTERN)
                .build();

    }

    public MailMessage createLocationAbnormalityMessage(MailNotification mailNotification) {

        return MailMessage.builder()
                .from(getServiceAccount())
                .to(mailNotification.getTo())
                .subject("登录位置异常")
                .context("name", mailNotification.getUsername())
                .context("location", mailNotification.getLocation())
                .context("ip", mailNotification.getIp())
                .context("url", StringUtils.isNotBlank(emailProperties.getModifyPasswordUrl()) ? emailProperties.getModifyPasswordUrl() : "https://www.herodotus.cn")
                .template("email-location-abnormality.html")
                .inline("herodotusLogo", LOGO_LOCATION_PATTERN)
                .build();

    }
}