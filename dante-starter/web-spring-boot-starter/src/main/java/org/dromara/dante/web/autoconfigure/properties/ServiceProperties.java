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

package org.dromara.dante.web.autoconfigure.properties;

import com.google.common.base.MoreObjects;
import org.apache.commons.collections4.CollectionUtils;
import org.dromara.dante.core.constant.SystemConstants;
import org.dromara.dante.web.constant.WebConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * <p>Description: 服务级配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/10/11 21:59
 */
@ConfigurationProperties(prefix = WebConstants.PROPERTY_PREFIX_SERVICE)
public class ServiceProperties {

    /**
     * 服务接口扫描配置
     */
    private Scan scan = new Scan();

    public Scan getScan() {
        return scan;
    }

    public void setScan(Scan scan) {
        this.scan = scan;
    }

    public static class Scan {

        /**
         * 是否开启接口扫描
         */
        private Boolean enabled = true;
        /**
         * 指定扫描的命名空间。未指定的命名空间中，即使包含RequestMapping，也不会被添加进来。
         */
        private List<String> scanGroupIds;
        /**
         * Spring 中会包含 Controller和 RestController，
         * 如果该配置设置为True，那么就只扫描RestController
         * 如果该配置设置为False，那么Controller和 RestController斗扫描。
         */
        private Boolean justScanRestController = false;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public List<String> getScanGroupIds() {
            List<String> defaultGroupIds = Stream.of(SystemConstants.PACKAGE_NAME, "org.dromara").toList();

            if (CollectionUtils.isEmpty(this.scanGroupIds)) {
                this.scanGroupIds = new ArrayList<>();
            }

            this.scanGroupIds.addAll(defaultGroupIds);
            return scanGroupIds;
        }

        public void setScanGroupIds(List<String> scanGroupIds) {
            this.scanGroupIds = scanGroupIds;
        }

        public Boolean getJustScanRestController() {
            return justScanRestController;
        }

        public void setJustScanRestController(Boolean justScanRestController) {
            this.justScanRestController = justScanRestController;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("enabled", enabled)
                    .add("justScanRestController", justScanRestController)
                    .toString();
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("scan", scan)
                .toString();
    }
}
