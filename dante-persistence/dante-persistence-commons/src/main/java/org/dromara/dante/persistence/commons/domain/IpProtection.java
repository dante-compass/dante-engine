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

package org.dromara.dante.persistence.commons.domain;

import org.dromara.dante.core.domain.BaseModel;
import com.google.common.base.MoreObjects;

import java.util.Date;

/**
 * <p>Description: IP 地址保护信息实体 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/6/30 23:46
 */
public class IpProtection implements BaseModel {

    /**
     * 用户名
     */
    private String principal;
    /**
     * 客户端ID。可以用此字段代表不同类型终端设备
     */
    private String clientId;
    /**
     * 上次登录IP地址
     */
    private String lastIpAddress;
    /**
     * 上次登录位置
     */
    private String lastLocation;
    /**
     * 上次登录时间
     */
    private Date lastSignInTime;
    /**
     * 本次登录IP地址
     */
    private String currentIpAddress;
    /**
     * 本次登录位置
     */
    private String currentLocation;
    /**
     * 本次登录时间
     */
    private Date currentSignInTime;
    /**
     * 是否为常规登录地址
     */
    private Boolean regular;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCurrentIpAddress() {
        return currentIpAddress;
    }

    public void setCurrentIpAddress(String currentIpAddress) {
        this.currentIpAddress = currentIpAddress;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Date getCurrentSignInTime() {
        return currentSignInTime;
    }

    public void setCurrentSignInTime(Date currentSignInTime) {
        this.currentSignInTime = currentSignInTime;
    }

    public String getLastIpAddress() {
        return lastIpAddress;
    }

    public void setLastIpAddress(String lastIpAddress) {
        this.lastIpAddress = lastIpAddress;
    }

    public String getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }

    public Date getLastSignInTime() {
        return lastSignInTime;
    }

    public void setLastSignInTime(Date lastSignInTime) {
        this.lastSignInTime = lastSignInTime;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public Boolean getRegular() {
        return regular;
    }

    public void setRegular(Boolean regular) {
        this.regular = regular;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("clientId", clientId)
                .add("principal", principal)
                .add("lastIpAddress", lastIpAddress)
                .add("lastLocation", lastLocation)
                .add("lastSignInTime", lastSignInTime)
                .add("currentIpAddress", currentIpAddress)
                .add("currentLocation", currentLocation)
                .add("currentSignInTime", currentSignInTime)
                .add("regular", regular)
                .toString();
    }
}
