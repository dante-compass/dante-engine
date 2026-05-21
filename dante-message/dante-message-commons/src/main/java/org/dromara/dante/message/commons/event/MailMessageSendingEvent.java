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

package org.dromara.dante.message.commons.event;

import org.dromara.dante.message.commons.constant.MqttConstants;
import org.dromara.dante.message.commons.domain.MailMessage;
import org.springframework.context.ApplicationEvent;
import org.springframework.messaging.support.GenericMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: 邮件消息发送事件 </p>
 * <p>
 * 在代码中发送该事件，可以实现Email信息。
 *
 * @author : gengwei.zheng
 * @date : 2024/6/14 23:03
 */
public class MailMessageSendingEvent extends ApplicationEvent {

    public MailMessageSendingEvent(MailMessage mailMessage) {
        super(MailMessageSendingEvent.convert(mailMessage));
    }

    private static GenericMessage<MailMessage> convert(MailMessage mailMessage) {
        Map<String, Object> headers = new HashMap<>();
        headers.put(MqttConstants.MESSAGE_HEADER__HERODOTUS_EVENT_ROUTER, MqttConstants.MESSAGE_ROUTER_TO_MAIL);
        return new GenericMessage<>(mailMessage, headers);
    }
}
