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

package org.dromara.dante.persistence.commons.domain;

import com.google.common.base.MoreObjects;
import org.dromara.dante.core.domain.BaseModel;

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
