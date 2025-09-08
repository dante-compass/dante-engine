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

package cn.herodotus.engine.web.service.properties;

import cn.herodotus.engine.core.definition.constant.SystemConstants;
import cn.herodotus.engine.web.core.constant.WebConstants;
import com.google.common.base.MoreObjects;
import org.apache.commons.collections4.CollectionUtils;
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
