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

package com.alibaba.cloud.nacos;

import com.alibaba.cloud.nacos.configdata.NacosConfigRefreshEventListener;
import com.alibaba.cloud.nacos.refresh.SmartConfigurationPropertiesRebinder;
import com.alibaba.cloud.nacos.refresh.condition.ConditionalOnNonDefaultBehavior;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.cloud.context.properties.ConfigurationPropertiesBeans;
import org.springframework.cloud.context.properties.ConfigurationPropertiesRebinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author juven.xuxb
 * @author freeman
 */
@Configuration(proxyBeanMethods = false)
@Conditional(NacosConfigEnabledCondition.class)
public class NacosConfigSpringCloudAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(search = SearchStrategy.CURRENT)
	@ConditionalOnNonDefaultBehavior
	public ConfigurationPropertiesRebinder smartConfigurationPropertiesRebinder(ConfigurationPropertiesBeans beans) {
		// If using default behavior, not use SmartConfigurationPropertiesRebinder.
		// Minimize te possibility of making mistakes.
		return new SmartConfigurationPropertiesRebinder(beans);
	}

	@Bean(name = "nacosConfigSpringCloudRefreshEventListener")
	public NacosConfigRefreshEventListener nacosConfigRefreshEventListener() {
		return new NacosConfigRefreshEventListener();
	}

}
