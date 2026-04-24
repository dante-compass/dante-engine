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

package org.dromara.dante.persistence.sys.jpa.definition;

import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.dromara.dante.data.jpa.entity.AbstractAuditEntity;

/**
 * <p>Description: OAuth2 安全防护通用属性 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/12/12 14:40
 */
@MappedSuperclass
public abstract class AbstractOAuth2AuditRecord extends AbstractAuditEntity {

    @Schema(name = "用户信息标识")
    @Column(name = "principal_name", length = 128)
    private String principalName;

    @Schema(name = "客户端ID")
    @Column(name = "client_id", length = 100)
    private String clientId;

    @Schema(name = "IP地址", description = "使用 128 位兼容 IPV6")
    @Column(name = "ip_address", length = 50)
    private String ip;

    @Schema(name = "是否为移动端")
    @Column(name = "is_mobile")
    private Boolean mobile = Boolean.FALSE;

    @Schema(name = "浏览器名称")
    @Column(name = "browser_name", length = 50)
    private String browserName;

    @Schema(name = "是否为移动端浏览器")
    @Column(name = "is_mobile_browser")
    private Boolean mobileBrowser = Boolean.FALSE;

    @Schema(name = "浏览器版本")
    @Column(name = "browser_version", length = 256)
    private String browserVersion;

    @Schema(name = "平台名称")
    @Column(name = "platform_name", length = 50)
    private String platformName;

    @Schema(name = "操作系统名称")
    @Column(name = "os_name", length = 100)
    private String osName;

    @Schema(name = "操作系统版本")
    @Column(name = "os_version", length = 256)
    private String osVersion;

    @Schema(name = "浏览器引擎")
    @Column(name = "browser_engine_name", length = 50)
    private String browserEngineName;

    @Schema(name = "操作系统版本")
    @Column(name = "browser_engine_version", length = 256)
    private String browserEngineVersion;

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
                .toString();
    }
}
