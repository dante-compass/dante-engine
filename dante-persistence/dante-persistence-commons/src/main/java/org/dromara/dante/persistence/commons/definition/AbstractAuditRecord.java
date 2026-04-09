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

package org.dromara.dante.persistence.commons.definition;

import org.dromara.dante.core.domain.BaseModel;
import cn.hutool.v7.http.useragent.UserAgent;
import cn.hutool.v7.http.useragent.UserAgentUtil;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Date;

/**
 * <p>Description: 审计记录抽象定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/1/3 15:02
 */
public abstract class AbstractAuditRecord implements BaseModel {

    private String principalName;

    private String clientId;

    private String ip;

    private Boolean mobile = Boolean.FALSE;

    private String browserName;

    private Boolean mobileBrowser = Boolean.FALSE;

    private String browserVersion;

    private String platformName;

    private String osName;

    private String osVersion;

    private String browserEngineName;

    private String browserEngineVersion;

    private Date createTime = new Date();

    private Date updateTime = new Date();

    protected AbstractAuditRecord() {
    }

    protected AbstractAuditRecord(String userAgent) {
        withUserAgent(userAgent);
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Boolean getMobile() {
        return mobile;
    }

    public void setMobile(Boolean mobile) {
        this.mobile = mobile;
    }

    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public Boolean getMobileBrowser() {
        return mobileBrowser;
    }

    public void setMobileBrowser(Boolean mobileBrowser) {
        this.mobileBrowser = mobileBrowser;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getBrowserEngineName() {
        return browserEngineName;
    }

    public void setBrowserEngineName(String browserEngineName) {
        this.browserEngineName = browserEngineName;
    }

    public String getBrowserEngineVersion() {
        return browserEngineVersion;
    }

    public void setBrowserEngineVersion(String browserEngineVersion) {
        this.browserEngineVersion = browserEngineVersion;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    private void withUserAgent(String content) {
        UserAgent userAgent = UserAgentUtil.parse(content);
        if (ObjectUtils.isNotEmpty(userAgent)) {
            setMobile(userAgent.isMobile());
            setBrowserName(userAgent.getBrowser().getName());
            setMobileBrowser(userAgent.getBrowser().isMobile());
            setBrowserVersion(userAgent.getVersion());
            setPlatformName(userAgent.getPlatform().getName());
            setOsName(userAgent.getOs().getName());
            setOsVersion(userAgent.getOsVersion());
            setBrowserEngineName(userAgent.getEngine().getName());
            setBrowserEngineVersion(userAgent.getEngineVersion());
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("principalName", principalName)
                .add("clientId", clientId)
                .add("ip", ip)
                .add("mobile", mobile)
                .add("browserName", browserName)
                .add("mobileBrowser", mobileBrowser)
                .add("browserVersion", browserVersion)
                .add("platformName", platformName)
                .add("osName", osName)
                .add("osVersion", osVersion)
                .add("browserEngineName", browserEngineName)
                .add("browserEngineVersion", browserEngineVersion)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}
