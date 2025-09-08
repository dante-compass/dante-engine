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
 * along with this program.  If not, see <https://www.herodotus.vip>.
 */

package cn.herodotus.engine.core.foundation.context;

import cn.herodotus.engine.core.definition.constant.SystemConstants;
import cn.herodotus.engine.core.definition.enums.Protocol;
import cn.herodotus.engine.core.foundation.enums.Architecture;
import cn.herodotus.engine.core.foundation.enums.DataAccessStrategy;
import com.google.common.base.MoreObjects;
import org.springframework.context.ApplicationContext;

/**
 * <p>Description: 服务上下文信息 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/8/25 17:53
 */
class ServiceContext {

    /**
     * 平台架构类型，默认：DISTRIBUTED（分布式架构）
     */
    private Architecture architecture = Architecture.DISTRIBUTED;
    /**
     * 数据访问策略，默认：
     */
    private DataAccessStrategy dataAccessStrategy = DataAccessStrategy.REMOTE;
    /**
     * 协议头类型
     */
    private Protocol protocol = Protocol.HTTP;
    /**
     * 服务端口号
     */
    private String port;
    /**
     * 服务IP地址
     */
    private String ip;
    /**
     * 服务地址，格式：ip:port
     */
    private String address;
    /**
     * 服务Url，格式：http://ip:port
     */
    private String url;
    /**
     * 应用名称，与spring.application.name一致
     */
    private String applicationName;
    /**
     * 留存一份ApplicationContext
     */
    private ApplicationContext applicationContext;

    /**
     * 认证中心服务名称
     */
    private String uaaServiceName;
    /**
     * 用户中心服务名称
     */
    private String upmsServiceName;
    /**
     * 消息服务名称
     */
    private String messageServiceName;
    /**
     * 对象存储服务名称
     */
    private String ossServiceName;
    /**
     * 统一网关服务地址。可以是IP+端口，可以是域名
     */
    private String gatewayServiceUri;

    /**
     * 统一认证中心服务地址
     */
    private String uaaServiceUri;
    /**
     * 统一权限管理服务地址
     */
    private String upmsServiceUri;
    /**
     * 消息服务地址
     */
    private String messageServiceUri;
    /**
     * 对象存储服务地址
     */
    private String ossServiceUri;
    /**
     * OAuth2 Authorization Code 模式认证端点，/oauth2/authorize uri 地址，可修改为自定义地址
     */
    private String authorizationUri;
    /**
     * OAuth2 Authorization Code 模式认证端点，/oauth2/authorize端点地址，可修改为自定义地址
     */
    private String authorizationEndpoint = SystemConstants.OAUTH2_AUTHORIZATION_ENDPOINT;
    /**
     * OAuth2 Pushed Authorization Requests 模式认证端点，/oauth2/par uri 地址，可修改为自定义地址
     */
    private String pushedAuthorizationRequestUri;
    /**
     * OAuth2 Pushed Authorization Requests 模式认证端点，/oauth2/authorize端点地址，可修改为自定义地址
     */
    private String pushedAuthorizationRequestEndpoint = SystemConstants.OAUTH2_PUSHED_AUTHORIZATION_REQUEST_ENDPOINT;

    /**
     * OAuth2 /oauth2/token 申请 Token uri 地址，可修改为自定义地址
     */
    private String accessTokenUri;
    /**
     * OAuth2 /oauth2/token 申请 Token 端点地址，可修改为自定义地址
     */
    private String accessTokenEndpoint = SystemConstants.OAUTH2_TOKEN_ENDPOINT;
    /**
     * OAuth2 /oauth2/jwks uri 地址，可修改为自定义地址
     */
    private String jwkSetUri;
    /**
     * OAuth2 /oauth2/jwks 端点地址，可修改为自定义地址
     */
    private String jwkSetEndpoint = SystemConstants.OAUTH2_JWK_SET_ENDPOINT;
    /**
     * OAuth2 /oauth2/revoke 撤销 Token uri 地址，可修改为自定义地址
     */
    private String tokenRevocationUri;
    /**
     * OAuth2 /oauth2/revoke 撤销 Token 端点地址，可修改为自定义地址
     */
    private String tokenRevocationEndpoint = SystemConstants.OAUTH2_TOKEN_REVOCATION_ENDPOINT;
    /**
     * OAuth2 /oauth2/introspect 查看 Token uri地址，可修改为自定义地址
     */
    private String tokenIntrospectionUri;
    /**
     * OAuth2 /oauth2/introspect 查看 Token 端点地址，可修改为自定义地址
     */
    private String tokenIntrospectionEndpoint = SystemConstants.OAUTH2_TOKEN_INTROSPECTION_ENDPOINT;
    /**
     * OAuth2 /oauth2/device_authorization 设备授权认证 uri地址，可修改为自定义地址
     */
    private String deviceAuthorizationUri;
    /**
     * OAuth2 /oauth2/device_authorization 设备授权认证端点地址，可修改为自定义地址
     */
    private String deviceAuthorizationEndpoint = SystemConstants.OAUTH2_DEVICE_AUTHORIZATION_ENDPOINT;
    /**
     * OAuth2 /oauth2/device_verification 设备授权校验 uri地址，可修改为自定义地址
     */
    private String deviceVerificationUri;
    /**
     * OAuth2 /oauth2/device_verification 设备授权校验端点地址，可修改为自定义地址
     */
    private String deviceVerificationEndpoint = SystemConstants.OAUTH2_DEVICE_VERIFICATION_ENDPOINT;
    /**
     * OAuth2 OIDC /connect/register uri 地址，可修改为自定义地址
     */
    private String oidcClientRegistrationUri;
    /**
     * OAuth2 OIDC /connect/register 端点地址，可修改为自定义地址
     */
    private String oidcClientRegistrationEndpoint = SystemConstants.OIDC_CLIENT_REGISTRATION_ENDPOINT;
    /**
     * OAuth2 OIDC /connect/logout uri 地址，可修改为自定义地址
     */
    private String oidcLogoutUri;
    /**
     * OAuth2 OIDC /connect/logout 端点地址，可修改为自定义地址
     */
    private String oidcLogoutEndpoint = SystemConstants.OIDC_LOGOUT_ENDPOINT;
    /**
     * OAuth2 OIDC /userinfo uri 地址，可修改为自定义地址
     */
    private String oidcUserInfoUri;
    /**
     * OAuth2 OIDC /userinfo 端点地址，可修改为自定义地址
     */
    private String oidcUserInfoEndpoint = SystemConstants.OIDC_USER_INFO_ENDPOINT;
    /**
     * Spring Authorization Server Issuer Url
     */
    private String issuerUri;

    public Architecture getArchitecture() {
        return architecture;
    }

    public void setArchitecture(Architecture architecture) {
        this.architecture = architecture;
    }

    public DataAccessStrategy getDataAccessStrategy() {
        return dataAccessStrategy;
    }

    public void setDataAccessStrategy(DataAccessStrategy dataAccessStrategy) {
        this.dataAccessStrategy = dataAccessStrategy;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public String getUaaServiceName() {
        return uaaServiceName;
    }

    public void setUaaServiceName(String uaaServiceName) {
        this.uaaServiceName = uaaServiceName;
    }

    public String getUpmsServiceName() {
        return upmsServiceName;
    }

    public void setUpmsServiceName(String upmsServiceName) {
        this.upmsServiceName = upmsServiceName;
    }

    public String getMessageServiceName() {
        return messageServiceName;
    }

    public void setMessageServiceName(String messageServiceName) {
        this.messageServiceName = messageServiceName;
    }

    public String getOssServiceName() {
        return ossServiceName;
    }

    public void setOssServiceName(String ossServiceName) {
        this.ossServiceName = ossServiceName;
    }

    public String getGatewayServiceUri() {
        return gatewayServiceUri;
    }

    public void setGatewayServiceUri(String gatewayServiceUri) {
        this.gatewayServiceUri = gatewayServiceUri;
    }

    public String getUaaServiceUri() {
        return uaaServiceUri;
    }

    public void setUaaServiceUri(String uaaServiceUri) {
        this.uaaServiceUri = uaaServiceUri;
    }

    public String getUpmsServiceUri() {
        return upmsServiceUri;
    }

    public void setUpmsServiceUri(String upmsServiceUri) {
        this.upmsServiceUri = upmsServiceUri;
    }

    public String getMessageServiceUri() {
        return messageServiceUri;
    }

    public void setMessageServiceUri(String messageServiceUri) {
        this.messageServiceUri = messageServiceUri;
    }

    public String getOssServiceUri() {
        return ossServiceUri;
    }

    public void setOssServiceUri(String ossServiceUri) {
        this.ossServiceUri = ossServiceUri;
    }

    public String getAuthorizationUri() {
        return authorizationUri;
    }

    public void setAuthorizationUri(String authorizationUri) {
        this.authorizationUri = authorizationUri;
    }

    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    public void setAuthorizationEndpoint(String authorizationEndpoint) {
        this.authorizationEndpoint = authorizationEndpoint;
    }

    public String getPushedAuthorizationRequestUri() {
        return pushedAuthorizationRequestUri;
    }

    public void setPushedAuthorizationRequestUri(String pushedAuthorizationRequestUri) {
        this.pushedAuthorizationRequestUri = pushedAuthorizationRequestUri;
    }

    public String getPushedAuthorizationRequestEndpoint() {
        return pushedAuthorizationRequestEndpoint;
    }

    public void setPushedAuthorizationRequestEndpoint(String pushedAuthorizationRequestEndpoint) {
        this.pushedAuthorizationRequestEndpoint = pushedAuthorizationRequestEndpoint;
    }

    public String getAccessTokenUri() {
        return accessTokenUri;
    }

    public void setAccessTokenUri(String accessTokenUri) {
        this.accessTokenUri = accessTokenUri;
    }

    public String getAccessTokenEndpoint() {
        return accessTokenEndpoint;
    }

    public void setAccessTokenEndpoint(String accessTokenEndpoint) {
        this.accessTokenEndpoint = accessTokenEndpoint;
    }

    public String getJwkSetUri() {
        return jwkSetUri;
    }

    public void setJwkSetUri(String jwkSetUri) {
        this.jwkSetUri = jwkSetUri;
    }

    public String getJwkSetEndpoint() {
        return jwkSetEndpoint;
    }

    public void setJwkSetEndpoint(String jwkSetEndpoint) {
        this.jwkSetEndpoint = jwkSetEndpoint;
    }

    public String getTokenRevocationUri() {
        return tokenRevocationUri;
    }

    public void setTokenRevocationUri(String tokenRevocationUri) {
        this.tokenRevocationUri = tokenRevocationUri;
    }

    public String getTokenRevocationEndpoint() {
        return tokenRevocationEndpoint;
    }

    public void setTokenRevocationEndpoint(String tokenRevocationEndpoint) {
        this.tokenRevocationEndpoint = tokenRevocationEndpoint;
    }

    public String getTokenIntrospectionUri() {
        return tokenIntrospectionUri;
    }

    public void setTokenIntrospectionUri(String tokenIntrospectionUri) {
        this.tokenIntrospectionUri = tokenIntrospectionUri;
    }

    public String getTokenIntrospectionEndpoint() {
        return tokenIntrospectionEndpoint;
    }

    public void setTokenIntrospectionEndpoint(String tokenIntrospectionEndpoint) {
        this.tokenIntrospectionEndpoint = tokenIntrospectionEndpoint;
    }

    public String getDeviceAuthorizationUri() {
        return deviceAuthorizationUri;
    }

    public void setDeviceAuthorizationUri(String deviceAuthorizationUri) {
        this.deviceAuthorizationUri = deviceAuthorizationUri;
    }

    public String getDeviceAuthorizationEndpoint() {
        return deviceAuthorizationEndpoint;
    }

    public void setDeviceAuthorizationEndpoint(String deviceAuthorizationEndpoint) {
        this.deviceAuthorizationEndpoint = deviceAuthorizationEndpoint;
    }

    public String getDeviceVerificationUri() {
        return deviceVerificationUri;
    }

    public void setDeviceVerificationUri(String deviceVerificationUri) {
        this.deviceVerificationUri = deviceVerificationUri;
    }

    public String getDeviceVerificationEndpoint() {
        return deviceVerificationEndpoint;
    }

    public void setDeviceVerificationEndpoint(String deviceVerificationEndpoint) {
        this.deviceVerificationEndpoint = deviceVerificationEndpoint;
    }

    public String getOidcClientRegistrationUri() {
        return oidcClientRegistrationUri;
    }

    public void setOidcClientRegistrationUri(String oidcClientRegistrationUri) {
        this.oidcClientRegistrationUri = oidcClientRegistrationUri;
    }

    public String getOidcClientRegistrationEndpoint() {
        return oidcClientRegistrationEndpoint;
    }

    public void setOidcClientRegistrationEndpoint(String oidcClientRegistrationEndpoint) {
        this.oidcClientRegistrationEndpoint = oidcClientRegistrationEndpoint;
    }

    public String getOidcLogoutUri() {
        return oidcLogoutUri;
    }

    public void setOidcLogoutUri(String oidcLogoutUri) {
        this.oidcLogoutUri = oidcLogoutUri;
    }

    public String getOidcLogoutEndpoint() {
        return oidcLogoutEndpoint;
    }

    public void setOidcLogoutEndpoint(String oidcLogoutEndpoint) {
        this.oidcLogoutEndpoint = oidcLogoutEndpoint;
    }

    public String getOidcUserInfoUri() {
        return oidcUserInfoUri;
    }

    public void setOidcUserInfoUri(String oidcUserInfoUri) {
        this.oidcUserInfoUri = oidcUserInfoUri;
    }

    public String getOidcUserInfoEndpoint() {
        return oidcUserInfoEndpoint;
    }

    public void setOidcUserInfoEndpoint(String oidcUserInfoEndpoint) {
        this.oidcUserInfoEndpoint = oidcUserInfoEndpoint;
    }

    public String getIssuerUri() {
        return issuerUri;
    }

    public void setIssuerUri(String issuerUri) {
        this.issuerUri = issuerUri;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("architecture", architecture)
                .add("dataAccessStrategy", dataAccessStrategy)
                .add("protocol", protocol)
                .add("port", port)
                .add("ip", ip)
                .add("address", address)
                .add("url", url)
                .add("applicationName", applicationName)
                .add("applicationContext", applicationContext)
                .add("uaaServiceName", uaaServiceName)
                .add("upmsServiceName", upmsServiceName)
                .add("messageServiceName", messageServiceName)
                .add("ossServiceName", ossServiceName)
                .add("gatewayServiceUri", gatewayServiceUri)
                .add("uaaServiceUri", uaaServiceUri)
                .add("upmsServiceUri", upmsServiceUri)
                .add("messageServiceUri", messageServiceUri)
                .add("ossServiceUri", ossServiceUri)
                .add("authorizationUri", authorizationUri)
                .add("authorizationEndpoint", authorizationEndpoint)
                .add("pushedAuthorizationRequestUri", pushedAuthorizationRequestUri)
                .add("pushedAuthorizationRequestEndpoint", pushedAuthorizationRequestEndpoint)
                .add("accessTokenUri", accessTokenUri)
                .add("accessTokenEndpoint", accessTokenEndpoint)
                .add("jwkSetUri", jwkSetUri)
                .add("jwkSetEndpoint", jwkSetEndpoint)
                .add("tokenRevocationUri", tokenRevocationUri)
                .add("tokenRevocationEndpoint", tokenRevocationEndpoint)
                .add("tokenIntrospectionUri", tokenIntrospectionUri)
                .add("tokenIntrospectionEndpoint", tokenIntrospectionEndpoint)
                .add("deviceAuthorizationUri", deviceAuthorizationUri)
                .add("deviceAuthorizationEndpoint", deviceAuthorizationEndpoint)
                .add("deviceVerificationUri", deviceVerificationUri)
                .add("deviceVerificationEndpoint", deviceVerificationEndpoint)
                .add("oidcClientRegistrationUri", oidcClientRegistrationUri)
                .add("oidcClientRegistrationEndpoint", oidcClientRegistrationEndpoint)
                .add("oidcLogoutUri", oidcLogoutUri)
                .add("oidcLogoutEndpoint", oidcLogoutEndpoint)
                .add("oidcUserInfoUri", oidcUserInfoUri)
                .add("oidcUserInfoEndpoint", oidcUserInfoEndpoint)
                .add("issuerUri", issuerUri)
                .toString();
    }
}
