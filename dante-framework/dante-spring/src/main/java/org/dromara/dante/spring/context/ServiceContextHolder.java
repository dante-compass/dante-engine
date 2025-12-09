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

package org.dromara.dante.spring.context;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.dromara.dante.core.constant.SymbolConstants;
import org.dromara.dante.core.enums.Protocol;
import org.dromara.dante.core.utils.WellFormedUtils;
import org.dromara.dante.spring.enums.Architecture;
import org.dromara.dante.spring.enums.DataAccessStrategy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

/**
 * <p>Description: 服务上下文信息工具类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/14 17:28
 */
public class ServiceContextHolder {

    private static volatile ServiceContextHolder instance;

    private final ServiceContext serviceContext;

    private ServiceContextHolder() {
        this.serviceContext = new ServiceContext();
    }

    public static ServiceContextHolder getInstance() {
        if (ObjectUtils.isEmpty(instance)) {
            synchronized (ServiceContextHolder.class) {
                if (ObjectUtils.isEmpty(instance)) {
                    instance = new ServiceContextHolder();
                }
            }
        }

        return instance;
    }

    private ServiceContext serviceContext() {
        return serviceContext;
    }

    private static ServiceContext getServiceContext() {
        return getInstance().serviceContext();
    }

    public static Architecture getArchitecture() {
        return getServiceContext().getArchitecture();
    }

    public static void setArchitecture(Architecture architecture) {
        getServiceContext().setArchitecture(architecture);
    }

    public static DataAccessStrategy getDataAccessStrategy() {
        return getServiceContext().getDataAccessStrategy();
    }

    public static void setDataAccessStrategy(DataAccessStrategy dataAccessStrategy) {
        getServiceContext().setDataAccessStrategy(dataAccessStrategy);
    }

    public static Protocol getProtocol() {
        return getServiceContext().getProtocol();
    }

    public static void setProtocol(Protocol protocol) {
        getServiceContext().setProtocol(protocol);
    }

    public static String getPort() {
        return getServiceContext().getPort();
    }

    public static void setPort(String port) {
        getServiceContext().setPort(port);
    }

    public static String getIp() {
        return getServiceContext().getIp();
    }

    public static void setIp(String ip) {
        getServiceContext().setIp(ip);
    }

    public static String getAddress() {
        if (isDistributedArchitecture()) {
            return getGatewayServiceUri() + SymbolConstants.FORWARD_SLASH + getApplicationName();
        } else {
            if (StringUtils.isNotBlank(getIp()) && StringUtils.isNotBlank(getPort())) {
                return getIp() + SymbolConstants.COLON + getPort();
            }
        }
        return getServiceContext().getAddress();
    }

    public static void setAddress(String address) {
        getServiceContext().setAddress(address);
    }

    public static String getUrl() {
        if (StringUtils.isBlank(getServiceContext().getUrl())) {
            String address = getAddress();
            if (StringUtils.isNotBlank(address)) {
                return WellFormedUtils.addressToUri(address, getProtocol(), false);
            }
        }
        return getServiceContext().getUrl();
    }

    public static void setUrl(String url) {
        getServiceContext().setUrl(url);
    }

    public static String getApplicationName() {
        return getServiceContext().getApplicationName();
    }

    public static void setApplicationName(String applicationName) {
        getServiceContext().setApplicationName(applicationName);
    }

    public static ApplicationContext getApplicationContext() {
        return getServiceContext().getApplicationContext();
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        getServiceContext().setApplicationContext(applicationContext);
    }

    public static String getContextPath() {
        return getServiceContext().getContextPath();
    }

    public static void setContextPath(String contextPath) {
        getServiceContext().setContextPath(contextPath);
    }

    public static boolean hasContextPath() {
        return StringUtils.isNotBlank(getServiceContext().getContextPath());
    }

    public static String getUaaServiceName() {
        return getServiceContext().getUaaServiceName();
    }

    public static void setUaaServiceName(String uaaServiceName) {
        getServiceContext().setUaaServiceName(uaaServiceName);
    }

    public static String getUpmsServiceName() {
        return getServiceContext().getUpmsServiceName();
    }

    public static void setUpmsServiceName(String upmsServiceName) {
        getServiceContext().setUpmsServiceName(upmsServiceName);
    }

    public static String getMessageServiceName() {
        return getServiceContext().getMessageServiceName();
    }

    public static void setMessageServiceName(String messageServiceName) {
        getServiceContext().setMessageServiceName(messageServiceName);
    }

    public static String getOssServiceName() {
        return getServiceContext().getOssServiceName();
    }

    public static void setOssServiceName(String ossServiceName) {
        getServiceContext().setOssServiceName(ossServiceName);
    }

    public static String getGatewayServiceUri() {
        return getServiceContext().getGatewayServiceUri();
    }

    public static void setGatewayServiceUri(String gatewayServiceUri) {
        getServiceContext().setGatewayServiceUri(gatewayServiceUri);
    }

    public static String getUaaServiceUri() {
        return getServiceContext().getUaaServiceUri();
    }

    public static void setUaaServiceUri(String uaaServiceUri) {
        getServiceContext().setUaaServiceUri(uaaServiceUri);
    }

    public static String getUpmsServiceUri() {
        return getServiceContext().getUpmsServiceUri();
    }

    public static void setUpmsServiceUri(String upmsServiceUri) {
        getServiceContext().setUpmsServiceUri(upmsServiceUri);
    }

    public static String getMessageServiceUri() {
        return getServiceContext().getMessageServiceUri();
    }

    public static void setMessageServiceUri(String messageServiceUri) {
        getServiceContext().setMessageServiceUri(messageServiceUri);
    }

    public static String getOssServiceUri() {
        return getServiceContext().getOssServiceUri();
    }

    public static void setOssServiceUri(String ossServiceUri) {
        getServiceContext().setOssServiceUri(ossServiceUri);
    }

    public static String getAuthorizationUri() {
        return getServiceContext().getAuthorizationUri();
    }

    public static void setAuthorizationUri(String authorizationUri) {
        getServiceContext().setAuthorizationUri(authorizationUri);
    }

    public static String getAuthorizationEndpoint() {
        return getServiceContext().getAuthorizationEndpoint();
    }

    public static void setAuthorizationEndpoint(String authorizationEndpoint) {
        getServiceContext().setAuthorizationEndpoint(authorizationEndpoint);
    }

    public static String getPushedAuthorizationRequestUri() {
        return getServiceContext().getPushedAuthorizationRequestUri();
    }

    public static void setPushedAuthorizationRequestUri(String pushedAuthorizationRequestUri) {
        getServiceContext().setPushedAuthorizationRequestUri(pushedAuthorizationRequestUri);
    }

    public static String getPushedAuthorizationRequestEndpoint() {
        return getServiceContext().getPushedAuthorizationRequestEndpoint();
    }

    public static void setPushedAuthorizationRequestEndpoint(String pushedAuthorizationRequestEndpoint) {
        getServiceContext().setPushedAuthorizationRequestEndpoint(pushedAuthorizationRequestEndpoint);
    }

    public static String getAccessTokenUri() {
        return getServiceContext().getAccessTokenUri();
    }

    public static void setAccessTokenUri(String accessTokenUri) {
        getServiceContext().setAccessTokenUri(accessTokenUri);
    }

    public static String getAccessTokenEndpoint() {
        return getServiceContext().getAccessTokenEndpoint();
    }

    public static void setAccessTokenEndpoint(String accessTokenEndpoint) {
        getServiceContext().setAccessTokenEndpoint(accessTokenEndpoint);
    }

    public static String getJwkSetUri() {
        return getServiceContext().getJwkSetUri();
    }

    public static void setJwkSetUri(String jwkSetUri) {
        getServiceContext().setJwkSetUri(jwkSetUri);
    }

    public static String getJwkSetEndpoint() {
        return getServiceContext().getJwkSetEndpoint();
    }

    public static void setJwkSetEndpoint(String jwkSetEndpoint) {
        getServiceContext().setJwkSetEndpoint(jwkSetEndpoint);
    }

    public static String getTokenRevocationUri() {
        return getServiceContext().getTokenRevocationUri();
    }

    public static void setTokenRevocationUri(String tokenRevocationUri) {
        getServiceContext().setTokenRevocationUri(tokenRevocationUri);
    }

    public static String getTokenRevocationEndpoint() {
        return getServiceContext().getTokenRevocationEndpoint();
    }

    public static void setTokenRevocationEndpoint(String tokenRevocationEndpoint) {
        getServiceContext().setTokenRevocationEndpoint(tokenRevocationEndpoint);
    }

    public static String getTokenIntrospectionUri() {
        return getServiceContext().getTokenIntrospectionUri();
    }

    public static void setTokenIntrospectionUri(String tokenIntrospectionUri) {
        getServiceContext().setTokenIntrospectionUri(tokenIntrospectionUri);
    }

    public static String getTokenIntrospectionEndpoint() {
        return getServiceContext().getTokenIntrospectionEndpoint();
    }

    public static void setTokenIntrospectionEndpoint(String tokenIntrospectionEndpoint) {
        getServiceContext().setTokenIntrospectionEndpoint(tokenIntrospectionEndpoint);
    }

    public static String getDeviceAuthorizationUri() {
        return getServiceContext().getDeviceAuthorizationUri();
    }

    public static void setDeviceAuthorizationUri(String deviceAuthorizationUri) {
        getServiceContext().setDeviceAuthorizationUri(deviceAuthorizationUri);
    }

    public static String getDeviceAuthorizationEndpoint() {
        return getServiceContext().getDeviceAuthorizationEndpoint();
    }

    public static void setDeviceAuthorizationEndpoint(String deviceAuthorizationEndpoint) {
        getServiceContext().setDeviceAuthorizationEndpoint(deviceAuthorizationEndpoint);
    }

    public static String getDeviceVerificationUri() {
        return getServiceContext().getDeviceVerificationUri();
    }

    public static void setDeviceVerificationUri(String deviceVerificationUri) {
        getServiceContext().setDeviceVerificationUri(deviceVerificationUri);
    }

    public static String getDeviceVerificationEndpoint() {
        return getServiceContext().getDeviceVerificationEndpoint();
    }

    public static void setDeviceVerificationEndpoint(String deviceVerificationEndpoint) {
        getServiceContext().setDeviceVerificationEndpoint(deviceVerificationEndpoint);
    }

    public static String getOidcClientRegistrationUri() {
        return getServiceContext().getOidcClientRegistrationUri();
    }

    public static void setOidcClientRegistrationUri(String oidcClientRegistrationUri) {
        getServiceContext().setOidcClientRegistrationUri(oidcClientRegistrationUri);
    }

    public static String getOidcClientRegistrationEndpoint() {
        return getServiceContext().getOidcClientRegistrationEndpoint();
    }

    public static void setOidcClientRegistrationEndpoint(String oidcClientRegistrationEndpoint) {
        getServiceContext().setOidcClientRegistrationEndpoint(oidcClientRegistrationEndpoint);
    }

    public static String getOidcLogoutUri() {
        return getServiceContext().getOidcLogoutUri();
    }

    public static void setOidcLogoutUri(String oidcLogoutUri) {
        getServiceContext().setOidcLogoutUri(oidcLogoutUri);
    }

    public static String getOidcLogoutEndpoint() {
        return getServiceContext().getOidcLogoutEndpoint();
    }

    public static void setOidcLogoutEndpoint(String oidcLogoutEndpoint) {
        getServiceContext().setOidcLogoutEndpoint(oidcLogoutEndpoint);
    }

    public static String getOidcUserInfoUri() {
        return getServiceContext().getOidcUserInfoUri();
    }

    public static void setOidcUserInfoUri(String oidcUserInfoUri) {
        getServiceContext().setOidcUserInfoUri(oidcUserInfoUri);
    }

    public static String getOidcUserInfoEndpoint() {
        return getServiceContext().getOidcUserInfoEndpoint();
    }

    public static void setOidcUserInfoEndpoint(String oidcUserInfoEndpoint) {
        getServiceContext().setOidcUserInfoEndpoint(oidcUserInfoEndpoint);
    }

    public static String getIssuerUri() {
        return getServiceContext().getIssuerUri();
    }

    public static void setIssuerUri(String issuerUri) {
        getServiceContext().setIssuerUri(issuerUri);
    }

    public static boolean isDistributedArchitecture() {
        return getServiceContext().getArchitecture() == Architecture.DISTRIBUTED;
    }

    /**
     * 用于 Spring Cloud BUS 发送信息使用。
     * <p>
     * Spring Cloud BUS 会使用 OriginService 作为 AntPathMatcher 的 pattern 来校验服务，所以这里使用了 “**”
     * <p>
     * 参见：
     * <code>org.springframework.cloud.bus.RemoteApplicationEventListener</code>
     * <code>org.springframework.cloud.bus.PathServiceMatcher#isFromSelf</code>
     *
     * @return 原始服务信息
     */
    public static String getOriginService() {
        return getApplicationName() + SymbolConstants.COLON + SymbolConstants.DOUBLE_STAR;
    }

    public static void publishEvent(ApplicationEvent applicationEvent) {
        getApplicationContext().publishEvent(applicationEvent);
    }

    public static String getId() {
        return getApplicationName() + SymbolConstants.COLON + getPort();
    }

    /**
     * 通过给定的 ServiceId 判断是否来自于自身。
     * <p>
     * 主要用于解决在消息队列场景，服务自身既是某个主题的生产者又是该主题消费者。那么在该服务多实例的情况下，很难判断“主从”关系。那么通过这个方法来判断。
     *
     * @param serviceId 格式为 spring.application.name : service.port
     * @return true 来自于服务自己，false 来自于其它服务
     */
    public static boolean isFromSelf(String serviceId) {
        if (Strings.CS.contains(serviceId, SymbolConstants.COLON)) {
            return Strings.CS.equals(serviceId, getId());
        }

        return false;
    }
}
