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

package org.dromara.dante.persistence.commons.definition;

import cn.hutool.v7.http.useragent.UserAgent;
import cn.hutool.v7.http.useragent.UserAgentUtil;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.ObjectUtils;
import org.dromara.dante.core.domain.BaseModel;

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
