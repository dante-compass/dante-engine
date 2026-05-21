/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2020-2030 郑庚伟 ZHENGGENGWEI (码匠君), <herodotus@aliyun.com> Licensed under the AGPL License
 *
 * This file is part of Herodotus Stirrup.
 *
 * Herodotus Stirrup is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Herodotus Stirrup is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.herodotus.cn>.
 */

package org.dromara.dante.message.autoconfigure.mail;

import jakarta.annotation.PostConstruct;
import org.dromara.dante.message.commons.constant.Channels;
import org.dromara.dante.spring.condition.ConditionalOnSpringMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.mail.autoconfigure.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mail.outbound.MailSendingMessageHandler;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * <p>Description: Mail Integration 自动配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/6/16 0:03
 */
@AutoConfiguration(after = MailSenderAutoConfiguration.class)
@ConditionalOnClass({MailSendingMessageHandler.class})
@ConditionalOnSpringMail
public class IntegrationMailAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(IntegrationMailAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [Integration Mail] Configure.");
    }


    @Bean
    public MailSendingMessageHandler mailSendingMessageHandler(JavaMailSender javaMailSender) {
        MailSendingMessageHandler handler = new MailSendingMessageHandler(javaMailSender);
        log.trace("[Herodotus] |- Bean [Mail Sending Message Handler] Configure.");
        return handler;
    }

    @Bean
    public IntegrationFlow sendMailFlow(MailSendingMessageHandler mailSendingMessageHandler) {
        return IntegrationFlow
                .from(Channels.MAIL__DEFAULT_SENDING_CHANNEL)
                .handle(mailSendingMessageHandler)
                .get();
    }
}
