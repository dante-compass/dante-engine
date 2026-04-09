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

package org.dromara.dante.security.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>Description: RegisteredClient 属性定义 </p>
 * <p>
 * 目前系统中，涉及到与 oauth2_registered_client 相关的对象，其各个字段与 RegisteredClient 对应关系如下：
 * <pre>
 *     <table>
 *         <thead>
 *             <tr>
 *                 <th>SAS</th>
 *                 <th>OAuth2Application</th>
 *                 <th>IotProduct</th>
 *                 <th>IotDevice</th>
 *             </tr>
 *         </thead>
 *         <tbody>
 *             <tr>
 *                 <td>id</td>
 *                 <td>applicationId</td>
 *                 <td>productId</td>
 *                 <td>deviceId</td>
 *             </tr>
 *             <tr>
 *                 <td>clientId</td>
 *                 <td>clientId</td>
 *                 <td>productKey</td>
 *                 <td>clientId(格式：{ProductKey}.{DeviceName})</td>
 *             </tr>
 *             <tr>
 *                 <td>clientSecret</td>
 *                 <td>clientSecret</td>
 *                 <td>productSecret</td>
 *                 <td>deviceSecret</td>
 *             </tr>
 *             <tr>
 *                 <td>clientName</td>
 *                 <td>applicationName</td>
 *                 <td>productName</td>
 *                 <td>deviceName</td>
 *             </tr>
 *         </tbody>
 *     </table>
 * </pre>
 *
 * @author : gengwei.zheng
 * @date : 2023/5/12 23:10
 */
public interface RegisteredClientDetails extends Serializable {

    /**
     * 数据ID，即存入数据库的主键
     *
     * @return 数据ID
     */
    String getId();

    /**
     * 客户端ID。OAuth2 中的 clientId。
     *
     * @return 客户端ID
     */
    String getClientId();

    /**
     * 客户端密钥。OAuth2 中的 clientSecret
     *
     * @return 客户端密钥
     */
    default String getClientSecret() {
        return null;
    }

    /**
     * 客户端名称。OAuth2 中的 clientName。对应物联网 Product，就是 ProductName；对应物联网 Device，就是 DeviceName。
     *
     * @return 客户端名称
     */
    String getClientName();

    /**
     * 重定向地址。OAuth2 中的 redirectUris。
     *
     * @return 重定向地址。
     */
    default String getRedirectUris() {
        return null;
    }

    default LocalDateTime getClientIdIssuedAt() {
        return null;
    }

    default LocalDateTime getClientSecretExpiresAt() {
        return null;
    }

    default String getClientAuthenticationMethods() {
        return null;
    }

    default String getAuthorizationGrantTypes() {
        return null;
    }

    default String getPostLogoutRedirectUris() {
        return null;
    }

    /**
     * 用于 OAuth2 确认页面显示的应用Logo
     *
     * @return 应用 Logo
     */
    default String getLogo() {
        return null;
    }

    /**
     * 上级客户端ID。主要用于客户端动态注册场景。
     * <p>
     * 标准的客户端动态注册，不需要上级客户端的任何信息，仅需要通过上级客户端生成 "Initial" Access Token 即可。
     * <p>
     * 在物联网场景下，Product 是 Device 的上级客户端，一方面需要将 Product 与 Device 进行关联，另一方面 Device 需要用到 ProductKey 信息。
     * 所以将 ProductKey 信息作为一项必要的认证数据进行传输，即将其作为 Device 的 Parent Client ID，来实现信息的关联。
     * <p>
     * 目前，系统中除了物联网设备动态注册外，暂时还没有其它功能需求会使用到 Parent Client ID
     *
     * @return 上级客户端ID
     */
    default String getParentClientId() {
        return null;
    }

    /**
     * 是否为客户端动态注册
     * <p>
     * OIDC 动态注册时，需要一个 "initial" access token。顾名思义就是需要指定另外一个 OAuth2 Client 来预先生成一个 access token，利用该 access token 才能进行动态注册。
     * <p>
     * 在客户端动态注册时，可以指定额外的自定义字段信息。在物联网应用场景下，客户端注册时指定的自定义字段就是 ProductKey，（即：IotDevice 对应 IotProduct 的 ClientId）。就是 Parent Client ID {@link #getParentClientId()} 的值
     * 因为当前的设定，动态注册客户端的 clientId 的值为 {ProductKey}.{DeviceName}，这个值肯定不能与 productKey 相同。所以利用这个方式就可以判断是否为客户端动态注册。
     *
     * @return 是否为初始客户端
     */
    default boolean isRegistrationClient() {
        return (StringUtils.isNotBlank(getParentClientId()) && StringUtils.isNotBlank(getClientId())) && !Strings.CS.equals(getParentClientId(), getClientId());
    }
}
