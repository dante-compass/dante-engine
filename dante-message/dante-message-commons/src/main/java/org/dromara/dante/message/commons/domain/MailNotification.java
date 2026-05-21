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
import org.dromara.dante.message.commons.definition.enums.MailNotificationAction;

import java.io.Serializable;

/**
 * <p>Description: 邮件通知数据实体 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/10/28 21:16
 */
public class MailNotification implements Serializable {

    /**
     * 电子邮件通知类型。不同类型需要的参数不同。
     */
    private MailNotificationAction action;
    /**
     * 电子邮件发送到的地址
     */
    private String to;
    /**
     * 电子邮件发送给谁？
     */
    private String username;
    /**
     * IP 地址定位到的位置。主要用与登录IP异常提示。
     */
    private String location;
    /**
     * 实际的 IP 地址。主要用与登录IP异常提示。
     */
    private String ip;

    public MailNotificationAction getAction() {
        return action;
    }

    public void setAction(MailNotificationAction action) {
        this.action = action;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("action", action)
                .add("to", to)
                .add("username", username)
                .add("location", location)
                .add("ip", ip)
                .toString();
    }
}
