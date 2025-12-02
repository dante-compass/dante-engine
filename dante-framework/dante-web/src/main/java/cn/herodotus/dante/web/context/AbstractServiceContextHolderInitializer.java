/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2020-2030 郑庚伟 ZHENGGENGWEI (码匠君), <herodotus@aliyun.com> Licensed under the AGPL License
 *
 * This file is part of Herodotus Stirrup.
 *
 * Herodotus Stirrup is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Herodotus Stirrup is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.herodotus.cn>.
 */

package cn.herodotus.dante.web.context;

import cn.herodotus.dante.core.utils.WellFormedUtils;
import cn.herodotus.dante.spring.context.ServiceContextHolder;
import cn.herodotus.dante.spring.enums.Architecture;
import cn.herodotus.dante.web.properties.EndpointProperties;
import cn.herodotus.dante.web.properties.PlatformProperties;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;

/**
 * <p>Description: ServiceContextHolder 初始化器抽象定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/12/2 15:15
 */
public abstract class AbstractServiceContextHolderInitializer {

    private final PlatformProperties platformProperties;
    private final EndpointProperties endpointProperties;
    private final InetAddress inetAddress;
    private final Integer port;
    private final String contextPath;

    protected AbstractServiceContextHolderInitializer(PlatformProperties platformProperties, EndpointProperties endpointProperties, InetAddress inetAddress, Integer port) {
        this(platformProperties, endpointProperties, inetAddress, port, null);
    }

    protected AbstractServiceContextHolderInitializer(PlatformProperties platformProperties, EndpointProperties endpointProperties, InetAddress inetAddress, Integer port, String contextPath) {
        this.platformProperties = platformProperties;
        this.endpointProperties = endpointProperties;
        this.inetAddress = inetAddress;
        this.port = port;
        this.contextPath = contextPath;
        init();
    }

    private void init() {
        ServiceContextHolder.setPort(String.valueOf(this.getPort()));
        ServiceContextHolder.setIp(getHostAddress());
        if (StringUtils.isNotBlank(this.contextPath)) {
            ServiceContextHolder.setContextPath(this.contextPath);
        }
        setProperties(platformProperties, endpointProperties);
    }

    private String getHostAddress() {
        String address = WellFormedUtils.getHostAddress();
        if (ObjectUtils.isNotEmpty(this.inetAddress)) {
            address = this.inetAddress.getHostAddress();
        }

        if (StringUtils.isNotBlank(address)) {
            return address;
        } else {
            return "localhost";
        }
    }

    private Integer getPort() {
        Integer port = this.port;
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
