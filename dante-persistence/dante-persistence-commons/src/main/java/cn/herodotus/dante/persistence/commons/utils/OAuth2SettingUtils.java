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

package cn.herodotus.dante.persistence.commons.utils;

import cn.herodotus.dante.core.constant.SystemConstants;
import cn.herodotus.dante.security.domain.OAuth2ClientType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>Description: OAuth2 Setting 相关工具类 </p>
 *
 * @author : gengwei_zheng
 * @date : 2026/9/4 17:06
 */
public class OAuth2SettingUtils {

    /**
     * 从 ClientSettings 中读取 resource_ids
     *
     * @param clientSettings OAuth2 客户端设置 {@link ClientSettings}
     * @return Resource Ids
     */
    public static List<String> getResourceIds(ClientSettings clientSettings) {
        return clientSettings.getSetting(SystemConstants.CLIENT_SETTINGS_NAMESPACE__RESOURCE_IDS);
    }

    /**
     * 向 ClientSettings 中设置 resource_ids
     *
     * @param clientSettingsBuilder ClientSettings 构建器
     * @param resourceIds           字符串类型 Resource Ids
     */
    public static void setResourceIds(ClientSettings.Builder clientSettingsBuilder, Collection<String> resourceIds) {
        if (CollectionUtils.isNotEmpty(resourceIds)) {
            clientSettingsBuilder.setting(SystemConstants.CLIENT_SETTINGS_NAMESPACE__RESOURCE_IDS, resourceIds);
        }
    }

    /**
     * 向 ClientSettings 中设置 resource_ids
     *
     * @param clientSettingsBuilder ClientSettings 构建器
     * @param resourceIds           字符串类型 Resource Ids
     */
    public static void setResourceIds(ClientSettings.Builder clientSettingsBuilder, String resourceIds) {
        Set<String> resourceIdSet = org.springframework.util.StringUtils.commaDelimitedListToSet(resourceIds);
        setResourceIds(clientSettingsBuilder, resourceIdSet);
    }

    /**
     * 向 ClientSettings 中设置 resource_ids
     *
     * @param clientSettingsBuilder ClientSettings 构建器
     * @param resourceIds           字符串类型 Resource Ids
     */
    public static void setResourceIds(ClientSettings.Builder clientSettingsBuilder, Object resourceIds) {
        if (ObjectUtils.isEmpty(resourceIds)) {
            setResourceIds(clientSettingsBuilder, (String) resourceIds);
        }
    }

    /**
     * 向 ClientSettings 中设置 resource_ids
     *
     * @param clientSettingsBuilder ClientSettings 构建器
     * @param clientType            客户端类型 {@link OAuth2ClientType}
     */
    public static void setClientType(ClientSettings.Builder clientSettingsBuilder, OAuth2ClientType clientType) {
        clientSettingsBuilder.setting(SystemConstants.PARAMETER__APPLICATION_TYPE, ObjectUtils.isNotEmpty(clientType) ? clientType : OAuth2ClientType.WEB);
    }

    /**
     * 向 ClientSettings 中设置 resource_ids
     *
     * @param clientSettingsBuilder ClientSettings 构建器
     * @param clientType            客户端类型
     */
    public static void setClientType(ClientSettings.Builder clientSettingsBuilder, String clientType) {
        clientSettingsBuilder.setting(SystemConstants.PARAMETER__APPLICATION_TYPE, StringUtils.isNotBlank(clientType) ? new OAuth2ClientType(clientType) : OAuth2ClientType.WEB);
    }

    /**
     * 向 ClientSettings 中设置 resource_ids
     *
     * @param clientSettingsBuilder ClientSettings 构建器
     * @param clientType            客户端类型
     */
    public static void setClientType(ClientSettings.Builder clientSettingsBuilder, String claim, Object clientType) {
        if (Strings.CS.equals(claim, SystemConstants.PARAMETER__APPLICATION_TYPE)) {
            clientSettingsBuilder.setting(SystemConstants.PARAMETER__APPLICATION_TYPE, ObjectUtils.isNotEmpty(clientType) ? clientType : OAuth2ClientType.WEB);
        }
    }

    /**
     * 判断 resource 名称是否可用
     *
     * @param clientSettings OAuth2 客户端设置 {@link ClientSettings}
     * @param resource       Resource
     * @return true Resource 不可用；false Resource 可用。
     */
    public static boolean unavailable(ClientSettings clientSettings, String resource) {
        // Get registered resource ID's (REQUIRED)
        List<String> resourceIds = getResourceIds(clientSettings);

        // Compare resource parameter against registered resource ID's
        return CollectionUtils.isEmpty(resourceIds) || StringUtils.isBlank(resource) || !resourceIds.contains(resource);
    }

    /**
     * 判断 resource 名称是否可用
     *
     * @param authorizationRequestResource 认证请求中的 Resource
     * @param tokenRequestResource         Token 中的 Resource
     * @return true Resource 不可用；false Resource 可用。
     */
    public static boolean unavailable(String authorizationRequestResource, String tokenRequestResource) {
        // Compare resource parameter from authorization request against resource parameter from access token request
        return StringUtils.isEmpty(tokenRequestResource) || !Strings.CI.equals(tokenRequestResource, authorizationRequestResource);
    }
}
