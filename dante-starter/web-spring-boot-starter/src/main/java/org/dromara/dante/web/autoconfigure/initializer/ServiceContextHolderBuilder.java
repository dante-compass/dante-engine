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

package org.dromara.dante.web.autoconfigure.initializer;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.dante.core.utils.WellFormedUtils;
import org.dromara.dante.spring.context.ServiceContextHolder;
import org.dromara.dante.spring.enums.Architecture;
import org.dromara.dante.web.properties.EndpointProperties;
import org.dromara.dante.web.properties.PlatformProperties;
import org.springframework.boot.web.server.autoconfigure.ServerProperties;

/**
 * <p>Description: ServiceContextHolder 构建器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/10/2 18:41
 */
public class ServiceContextHolderBuilder {

    private PlatformProperties platformProperties;
    private EndpointProperties endpointProperties;
    private ServerProperties serverProperties;

    private ServiceContextHolderBuilder() {

    }

    public static ServiceContextHolderBuilder builder() {
        return new ServiceContextHolderBuilder();
    }

    public ServiceContextHolderBuilder platformProperties(PlatformProperties platformProperties) {
        this.platformProperties = platformProperties;
        return this;
    }

    public ServiceContextHolderBuilder endpointProperties(EndpointProperties endpointProperties) {
        this.endpointProperties = endpointProperties;
        return this;
    }

    public ServiceContextHolderBuilder serverProperties(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
        return this;
    }

    public void build() {
        ServiceContextHolder.setPort(String.valueOf(this.getPort()));
        ServiceContextHolder.setIp(getHostAddress());
        ServiceContextHolder.setContextPath(serverProperties.getServlet().getContextPath());
        setProperties(platformProperties, endpointProperties);
    }

    private String getHostAddress() {
        String address = WellFormedUtils.getHostAddress();
        if (ObjectUtils.isNotEmpty(serverProperties.getAddress())) {
            address = serverProperties.getAddress().getHostAddress();
        }

        if (StringUtils.isNotBlank(address)) {
            return address;
        } else {
            return "localhost";
        }
    }

    private Integer getPort() {
        Integer port = serverProperties.getPort();
        if (ObjectUtils.isNotEmpty(port)) {
            return port;
        } else {
            return 8080;
        }
    }

    private void setProperties(PlatformProperties platformProperties, EndpointProperties endpointProperties) {
        ServiceContextHolder.setArchitecture(platformProperties.getArchitecture());
        ServiceContextHolder.setDataAccessStrategy(platformProperties.getDataAccessStrategy());
        ServiceContextHolder.setProtocol(platformProperties.getProtocol());

        String issuerUri = endpointProperties.getIssuerUri();

        if (StringUtils.isNotBlank(issuerUri)) {
            if (platformProperties.getArchitecture() == Architecture.MONOCOQUE) {
                ServiceContextHolder.setGatewayServiceUri(issuerUri);
                ServiceContextHolder.setUaaServiceUri(issuerUri);
                ServiceContextHolder.setUpmsServiceUri(issuerUri);
                ServiceContextHolder.setMessageServiceUri(issuerUri);
                ServiceContextHolder.setOssServiceUri(issuerUri);
            } else {
                ServiceContextHolder.setUaaServiceName(endpointProperties.getUaaServiceName());
                ServiceContextHolder.setUpmsServiceName(endpointProperties.getUpmsServiceName());
                ServiceContextHolder.setMessageServiceName(endpointProperties.getMessageServiceName());
                ServiceContextHolder.setOssServiceName(endpointProperties.getOssServiceName());
                ServiceContextHolder.setGatewayServiceUri(endpointProperties.getGatewayServiceUri());
                ServiceContextHolder.setUaaServiceUri(endpointProperties.getUaaServiceUri());
                ServiceContextHolder.setUpmsServiceUri(endpointProperties.getUpmsServiceUri());
                ServiceContextHolder.setMessageServiceUri(endpointProperties.getMessageServiceUri());
                ServiceContextHolder.setOssServiceUri(endpointProperties.getOssServiceUri());
            }

            ServiceContextHolder.setAuthorizationUri(endpointProperties.getAuthorizationUri());
            ServiceContextHolder.setAuthorizationEndpoint(endpointProperties.getAuthorizationEndpoint());
            ServiceContextHolder.setPushedAuthorizationRequestUri(endpointProperties.getPushedAuthorizationRequestUri());
            ServiceContextHolder.setPushedAuthorizationRequestEndpoint(endpointProperties.getPushedAuthorizationRequestEndpoint());
            ServiceContextHolder.setAccessTokenUri(endpointProperties.getAccessTokenUri());
            ServiceContextHolder.setAccessTokenEndpoint(endpointProperties.getAccessTokenEndpoint());
            ServiceContextHolder.setJwkSetUri(endpointProperties.getJwkSetUri());
            ServiceContextHolder.setJwkSetEndpoint(endpointProperties.getJwkSetEndpoint());
            ServiceContextHolder.setTokenRevocationUri(endpointProperties.getTokenRevocationUri());
            ServiceContextHolder.setTokenRevocationEndpoint(endpointProperties.getTokenRevocationEndpoint());
            ServiceContextHolder.setTokenIntrospectionUri(endpointProperties.getTokenIntrospectionUri());
            ServiceContextHolder.setTokenIntrospectionEndpoint(endpointProperties.getTokenIntrospectionEndpoint());
            ServiceContextHolder.setDeviceAuthorizationUri(endpointProperties.getDeviceAuthorizationUri());
            ServiceContextHolder.setDeviceAuthorizationEndpoint(endpointProperties.getDeviceAuthorizationEndpoint());
            ServiceContextHolder.setDeviceVerificationUri(endpointProperties.getDeviceVerificationUri());
            ServiceContextHolder.setDeviceVerificationEndpoint(endpointProperties.getDeviceVerificationEndpoint());
            ServiceContextHolder.setOidcClientRegistrationUri(endpointProperties.getOidcClientRegistrationUri());
            ServiceContextHolder.setOidcClientRegistrationEndpoint(endpointProperties.getOidcClientRegistrationEndpoint());
            ServiceContextHolder.setOidcLogoutUri(endpointProperties.getOidcLogoutUri());
            ServiceContextHolder.setOidcLogoutEndpoint(endpointProperties.getOidcLogoutEndpoint());
            ServiceContextHolder.setOidcUserInfoUri(endpointProperties.getOidcUserInfoUri());
            ServiceContextHolder.setOidcUserInfoEndpoint(endpointProperties.getOidcUserInfoEndpoint());
            ServiceContextHolder.setIssuerUri(issuerUri);
        }
    }
}
