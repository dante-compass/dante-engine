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

package org.dromara.dante.message.commons.domain;

import com.google.common.base.MoreObjects;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: 发送邮件消息实体 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/6/14 22:49
 */
public class MailMessage implements Serializable {

    /**
     * 发件人
     */
    private String from;
    /**
     * 发送给
     */
    private String[] to;
    /**
     * 主题
     */
    private String subject;
    /**
     * 发送内容
     */
    private String text;
    /**
     * 发送日期
     */
    private Date sentDate;
    /**
     * 回复给
     */
    private String replyTo;
    /**
     * 抄送给
     */
    private String[] cc;
    /**
     * 密送给
     */
    private String[] bcc;
    /**
     * 优先级。数字为 1 (最高) 至 5 (最低)
     */
    private Integer priority;
    /**
     * 模版名称
     */
    private String template;
    /**
     * 模版参数
     */
    private Map<String, Object> context;
    /**
     * 附件
     */
    private Map<String, String> attachments;
    /**
     * 内联资源
     */
    private Map<String, String> inlines;

    public MailMessage() {
    }

    public Map<String, String> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments;
    }

    public String[] getBcc() {
        return bcc;
    }

    public void setBcc(String[] bcc) {
        this.bcc = bcc;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Map<String, String> getInlines() {
        return inlines;
    }

    public void setInlines(Map<String, String> inlines) {
        this.inlines = inlines;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("from", from)
                .add("subject", subject)
                .add("text", text)
                .add("sentDate", sentDate)
                .add("replyTo", replyTo)
                .add("priority", priority)
                .add("template", template)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements Serializable {
        private String from;
        private String[] to;
        private String subject;
        private String text;
        private Date sentDate = new Date();
        private String replyTo;
        private String[] cc;
        private String[] bcc;
        private Integer priority;
        private String template;
        private final Map<String, Object> context = new HashMap<>();
        private final Map<String, String> attachments = new HashMap<>();
        private final Map<String, String> inlines = new HashMap<>();

        protected Builder() {

        }

        public Builder attachment(String contentId, String location) {
            this.attachments.put(contentId, location);
            return this;
        }

        public Builder bcc(String... bcc) {
            this.bcc = bcc;
            return this;
        }

        public Builder cc(String... cc) {
            this.cc = cc;
            return this;
        }

        public Builder context(String variable, Object value) {
            this.context.put(variable, value);
            return this;
        }

        public Builder from(String from) {
            this.from = from;
            return this;
        }

        public Builder inline(String contentId, String location) {
            this.inlines.put(contentId, location);
            return this;
        }

        public Builder priority(Integer priority) {
            this.priority = priority;
            return this;
        }

        public Builder replyTo(String replyTo) {
            this.replyTo = replyTo;
            return this;
        }

        public Builder sentDate(Date sentDate) {
            this.sentDate = sentDate;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder template(String template) {
            this.template = template;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder to(String... to) {
            this.to = to;
            return this;
        }

        public MailMessage build() {
            Assert.hasText(this.from, "from must not be empty");
            Assert.hasText(this.subject, "subject must not be empty");
            Assert.notEmpty(this.to, "text must not be empty");
            validate();
            return create();
        }

        private MailMessage create() {
            MailMessage message = new MailMessage();

            message.setAttachments(this.attachments);
            message.setBcc(this.bcc);
            message.setCc(this.cc);
            message.setContext(this.context);
            message.setFrom(this.from);
            message.setInlines(this.inlines);
            message.setPriority(this.priority);
            message.setReplyTo(this.replyTo);
            message.setSentDate(this.sentDate);
            message.setSubject(this.subject);
            message.setTemplate(this.template);
            message.setText(this.text);
            message.setTo(this.to);

            return message;
        }

        private void validate() {
            if (MapUtils.isEmpty(this.context) && StringUtils.isBlank(this.template)) {
                Assert.hasText(this.text, "text must not be empty");
            } else {
                if (MapUtils.isNotEmpty(this.context)) {
                    Assert.hasText(this.template, "template must not be empty");
                }

                if (StringUtils.isNotBlank(this.template)) {
                    Assert.notEmpty(this.context, "context must not be empty");
                }
            }
        }
    }
}
