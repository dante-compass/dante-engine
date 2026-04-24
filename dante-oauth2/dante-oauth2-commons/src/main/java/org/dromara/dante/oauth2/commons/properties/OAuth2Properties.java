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

package org.dromara.dante.oauth2.commons.properties;

import org.dromara.dante.core.constant.BaseConstants;
import org.dromara.dante.security.condition.TokenFormat;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>Description: OAuth2 通用配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/12/14 0:36
 */
@ConfigurationProperties(prefix = BaseConstants.PROPERTY_PREFIX_OAUTH2)
public class OAuth2Properties {

    /**
     * Token 校验是采用远程方式还是本地方式。
     */
    private TokenFormat tokenFormat = TokenFormat.OPAQUE;

    public TokenFormat getTokenFormat() {
        return tokenFormat;
    }

    public void setTokenFormat(TokenFormat tokenFormat) {
        this.tokenFormat = tokenFormat;
    }

    public boolean isRemoteValidate() {
        return this.getTokenFormat() == TokenFormat.OPAQUE;
    }
}
