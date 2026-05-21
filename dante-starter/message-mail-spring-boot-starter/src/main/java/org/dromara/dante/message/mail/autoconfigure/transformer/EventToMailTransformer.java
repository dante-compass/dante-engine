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

package org.dromara.dante.message.mail.autoconfigure.transformer;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.dromara.dante.message.commons.domain.MailMessage;
import org.dromara.dante.message.commons.exception.IntegrationMessageException;
import org.dromara.dante.spring.utils.ResourceResolverUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * <p>Description: Message 转 MimeMailMessage transformer</p>
 *
 * @author : gengwei.zheng
 * @date : 2024/6/15 23:38
 */
public class EventToMailTransformer implements GenericTransformer<MailMessage, MimeMailMessage> {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine springTemplateEngine;

    public EventToMailTransformer(JavaMailSender mailSender, SpringTemplateEngine springTemplateEngine) {
        this.mailSender = mailSender;
        this.springTemplateEngine = springTemplateEngine;
    }


    @Override
    public MimeMailMessage transform(MailMessage mailMessage) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, isMultipart(mailMessage));

            helper.setFrom(mailMessage.getFrom());
            helper.setTo(mailMessage.getTo());
            helper.setSubject(mailMessage.getSubject());
            helper.setSentDate(mailMessage.getSentDate());

            if (StringUtils.isNotBlank(mailMessage.getReplyTo())) {
                helper.setReplyTo(mailMessage.getReplyTo());
            }

            if (ArrayUtils.isNotEmpty(mailMessage.getCc())) {
                helper.setCc(mailMessage.getCc());
            }

            if (ArrayUtils.isNotEmpty(mailMessage.getBcc())) {
                helper.setBcc(mailMessage.getBcc());
            }

            if (ObjectUtils.isNotEmpty(mailMessage.getPriority())) {
                helper.setPriority(mailMessage.getPriority());
            }

            // setText 方法要在 addInline 方法之前设置，否则图片内容无法显示。
            if (isHtml(mailMessage)) {
                Context context = new Context();
                context.setVariables(mailMessage.getContext());
                String content = springTemplateEngine.process(mailMessage.getTemplate(), context);
                helper.setText(content, true);
            } else {
                helper.setText(mailMessage.getText());
            }

            if (MapUtils.isNotEmpty(mailMessage.getInlines())) {
                for (Map.Entry<String, String> entry : mailMessage.getInlines().entrySet()) {
                    if (Strings.CS.startsWith(entry.getValue(), ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX)) {
                        Resource[] resources = ResourceResolverUtils.getResources(entry.getValue());
                        if (ArrayUtils.isNotEmpty(resources)) {
                            helper.addInline(entry.getKey(), resources[0]);
                        }
                    } else {
                        helper.addInline(entry.getKey(), new File(entry.getValue()));
                    }
                }
            }

            if (MapUtils.isNotEmpty(mailMessage.getAttachments())) {
                for (Map.Entry<String, String> entry : mailMessage.getAttachments().entrySet()) {
                    if (Strings.CS.startsWith(entry.getValue(), ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX)) {
                        Resource[] resources = ResourceResolverUtils.getResources(entry.getValue());
                        if (ArrayUtils.isNotEmpty(resources)) {
                            helper.addAttachment(entry.getKey(), resources[0]);
                        }
                    } else {
                        helper.addAttachment(entry.getKey(), new File(entry.getValue()));
                    }
                }
            }

            return new MimeMailMessage(helper.getMimeMessage());

        } catch (MessagingException e) {
            throw new IntegrationMessageException("MailMessage transform to MimeMailMessage failed", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isMultipart(MailMessage mailMessage) {
        return MapUtils.isNotEmpty(mailMessage.getInlines()) || MapUtils.isNotEmpty(mailMessage.getAttachments());
    }

    private boolean isHtml(MailMessage mailMessage) {
        return MapUtils.isNotEmpty(mailMessage.getContext()) && StringUtils.isNotBlank(mailMessage.getTemplate());
    }
}
