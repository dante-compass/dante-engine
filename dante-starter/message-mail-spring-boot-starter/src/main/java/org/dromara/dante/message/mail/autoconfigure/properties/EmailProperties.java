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

package org.dromara.dante.message.mail.autoconfigure.properties;

import com.google.common.base.MoreObjects;
import org.dromara.dante.message.commons.constant.MessageConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * <p>Description: Email 配置属性 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/10/28 18:18
 */
@ConfigurationProperties(prefix = MessageConstants.PROPERTY_MESSAGE_MAIL)
public class EmailProperties {

    /**
     * 邮件验证码有效时长，默认5分钟
     */
    private Duration expire = Duration.ofMinutes(5);
    /**
     * 邮件验证码长度，默认为6位数
     */
    private int length = 6;
    /**
     * 系统服务通知统一Email账号
     */
    private String serviceAccount;
    /**
     * 验证电子邮件跳转 Url
     */
    private String verifyEmailUrl;
    /**
     * 修改密码跳转 Url
     */
    private String modifyPasswordUrl;

    public Duration getExpire() {
        return expire;
    }

    public void setExpire(Duration expire) {
        this.expire = expire;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getServiceAccount() {
        return serviceAccount;
    }

    public void setServiceAccount(String serviceAccount) {
        this.serviceAccount = serviceAccount;
    }

    public String getVerifyEmailUrl() {
        return verifyEmailUrl;
    }

    public void setVerifyEmailUrl(String verifyEmailUrl) {
        this.verifyEmailUrl = verifyEmailUrl;
    }

    public String getModifyPasswordUrl() {
        return modifyPasswordUrl;
    }

    public void setModifyPasswordUrl(String modifyPasswordUrl) {
        this.modifyPasswordUrl = modifyPasswordUrl;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("expire", expire)
                .add("length", length)
                .add("serviceAccount", serviceAccount)
                .add("verifyEmailUrl", verifyEmailUrl)
                .add("modifyPasswordUrl", modifyPasswordUrl)
                .toString();
    }
}
