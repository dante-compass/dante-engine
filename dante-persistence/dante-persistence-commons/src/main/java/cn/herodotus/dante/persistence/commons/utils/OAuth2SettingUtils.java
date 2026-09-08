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
import cn.herodotus.dante.security.definition.ExtendedClientSettingsDetails;
import cn.herodotus.dante.security.domain.OAuth2ApplicationType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>Description: OAuth2 Setting 相关工具类 </p>
 *
 * @author : gengwei_zheng
 * @date : 2026/9/4 17:06
 */
public class OAuth2SettingUtils {

    /**
     * 从 {@link ClientSettings} 中读取 resource_ids
     *
     * @param clientSettings OAuth2 客户端设置 {@link ClientSettings}
     * @return resource_ids 对应值
     */
    public static List<String> getResourceIds(ClientSettings clientSettings) {
        // 取值时设置为 List，方便使用 Spring StringUtils 相关工具类转换
        return clientSettings.getSetting(SystemConstants.CLIENT_SETTINGS_NAMESPACE__RESOURCE_IDS);
    }

    /**
     * 向 {@link ClientSettings} 中设置 resource_ids
     *
     * @param clientSettingsBuilder {@link ClientSettings} 构建器
     * @param resourceIds           字符串类型 resource_ids 值
     */
    public static void setResourceIds(ClientSettings.Builder clientSettingsBuilder, String resourceIds) {
        String[] resourceIdArray = org.springframework.util.StringUtils.commaDelimitedListToStringArray(resourceIds);
        if (ArrayUtils.isNotEmpty(resourceIdArray)) {
            List<String> ids = new ArrayList<>(Arrays.asList(resourceIdArray));
            clientSettingsBuilder.setting(SystemConstants.CLIENT_SETTINGS_NAMESPACE__RESOURCE_IDS, ids);
        }
    }

    /**
     * 向 {@link ClientSettings} 中设置 resource_ids
     *
     * @param clientSettingsBuilder {@link ClientSettings} 构建器
     * @param resourceIds           字符串类型 resource_ids 值
     */
    public static void setResourceIds(ClientSettings.Builder clientSettingsBuilder, Object resourceIds) {
        if (ObjectUtils.isEmpty(resourceIds)) {
            setResourceIds(clientSettingsBuilder, (String) resourceIds);
        }
    }

    /**
     * 检查 {@link ClientSettings} 中是否含有 application_type key。
     *
     * @param clientSettings {@link ClientSettings}
     * @return true Key 存在；false Key 不存在。
     */
    public static boolean containApplicationType(ClientSettings clientSettings) {
        return clientSettings.getSettings().containsKey(SystemConstants.CLIENT_SETTINGS_NAMESPACE__APPLICATION_TYPE);
    }

    /**
     * 向 {@link ClientSettings} 中设置 application_type
     *
     * @param clientSettingsBuilder {@link ClientSettings} 构建器
     * @param clientType            客户端类型 {@link OAuth2ApplicationType}
     */
    public static void setApplicationType(ClientSettings.Builder clientSettingsBuilder, OAuth2ApplicationType clientType) {
        clientSettingsBuilder.setting(SystemConstants.CLIENT_SETTINGS_NAMESPACE__APPLICATION_TYPE, ObjectUtils.isNotEmpty(clientType) ? clientType : OAuth2ApplicationType.WEB);
    }

    /**
     * 向 {@link ClientSettings} 中设置 application_type
     *
     * @param clientSettingsBuilder {@link ClientSettings} 构建器
     * @param clientType            客户端类型 String
     */
    public static void setApplicationType(ClientSettings.Builder clientSettingsBuilder, String clientType) {
        setApplicationType(clientSettingsBuilder, StringUtils.isNotBlank(clientType) ? new OAuth2ApplicationType(clientType) : OAuth2ApplicationType.WEB);
    }

    /**
     * 向 {@link ClientSettings} 中设置 application_type
     *
     * @param clientSettingsBuilder {@link ClientSettings} 构建器
     * @param clientType            客户端类型 Object
     */
    public static void setApplicationType(ClientSettings.Builder clientSettingsBuilder, String claim, Object clientType) {
        if (Strings.CS.equals(claim, SystemConstants.PARAMETER__APPLICATION_TYPE)) {
            clientSettingsBuilder.setting(SystemConstants.CLIENT_SETTINGS_NAMESPACE__APPLICATION_TYPE, ObjectUtils.isNotEmpty(clientType) ? clientType : OAuth2ApplicationType.WEB);
        }
    }

    /**
     * 从 {@link ClientSettings} 中读取 application_type
     *
     * @param clientSettings OAuth2 客户端设置 {@link ClientSettings}
     * @return application_type 值 {@link OAuth2ApplicationType}
     */
    public static OAuth2ApplicationType getApplicationType(ClientSettings clientSettings) {
        return clientSettings.getSetting(SystemConstants.CLIENT_SETTINGS_NAMESPACE__APPLICATION_TYPE);
    }

    /**
     * 检查 {@link ClientSettings} 中是否含有 product_key key。
     *
     * @param clientSettings {@link ClientSettings}
     * @return true Key 存在；false Key 不存在。
     */
    public static boolean containProductKey(ClientSettings clientSettings) {
        return clientSettings.getSettings().containsKey(SystemConstants.CLIENT_SETTINGS_NAMESPACE__PRODUCT_KEY);
    }

    /**
     * 向 {@link ClientSettings} 中设置 product_key
     *
     * @param clientSettingsBuilder {@link ClientSettings} 构建器
     * @param productKey            客户端类型 String
     */
    public static void setProductKey(ClientSettings.Builder clientSettingsBuilder, String productKey) {
        clientSettingsBuilder.setting(SystemConstants.CLIENT_SETTINGS_NAMESPACE__PRODUCT_KEY, productKey);
    }

    /**
     * 从 {@link ClientSettings} 中读取 product_key
     *
     * @param clientSettings {@link ClientSettings}
     * @return product_key 值 {@link OAuth2ApplicationType}
     */
    public static String getProductKey(ClientSettings clientSettings) {
        return clientSettings.getSetting(SystemConstants.CLIENT_SETTINGS_NAMESPACE__PRODUCT_KEY);
    }

    /**
     * 判断是否为客户端动态注册。主要用于物联网产品中，区分是客户端动态注册还是认证动态开启和关闭。
     *
     * @param extensions         扩展信息实现类
     * @param withParentClientId 是否包含 ParentClientId。该参数用于物联网区分是否为动态注册
     * @param <T>                {@link ExtendedClientSettingsDetails} 扩展信息实现类型
     * @return true 是动态注册，false 不是动态注册
     */
    public static <T extends ExtendedClientSettingsDetails> boolean isDynamicClientRegistration(T extensions, boolean withParentClientId) {
        return withParentClientId && StringUtils.isNotBlank(extensions.getParentClientId());
    }

    /**
     * 设置 {@link ClientSettings} 扩展信息方法。
     *
     * @param clientSettingsBuilder {@link ClientSettings} 构建器
     * @param extensions            扩展信息实现类
     * @param withParentClientId    是否包含 ParentClientId。该参数用于物联网区分是否为动态注册
     * @param <T>                   {@link ExtendedClientSettingsDetails} 扩展信息实现类型
     */
    public static <T extends ExtendedClientSettingsDetails> void setClientSettingsExtensions(ClientSettings.Builder clientSettingsBuilder, T extensions, boolean withParentClientId) {
        // 支持 OAuth2 Resource Indicator 所需配置
        OAuth2SettingUtils.setResourceIds(clientSettingsBuilder, extensions.getResourceIds());

        // 支持客户端动态注册指定注册来源。默认指定为 'web'
        OAuth2SettingUtils.setApplicationType(clientSettingsBuilder, extensions.getApplicationType());

        if (isDynamicClientRegistration(extensions, withParentClientId)) {
            setProductKey(clientSettingsBuilder, extensions.getParentClientId());
        }
    }

    /**
     * 设置 {@link ClientSettings} 扩展信息方法。不管是不是客户端动态注册，设置完整扩展信息。
     *
     * @param clientSettingsBuilder {@link ClientSettings} 构建器
     * @param extensions            扩展信息实现类
     * @param <T>                   {@link ClientSettings} 扩展信息实现类型
     */
    public static <T extends ExtendedClientSettingsDetails> void setClientSettingsExtensions(ClientSettings.Builder clientSettingsBuilder, T extensions) {
        setClientSettingsExtensions(clientSettingsBuilder, extensions, true);
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
