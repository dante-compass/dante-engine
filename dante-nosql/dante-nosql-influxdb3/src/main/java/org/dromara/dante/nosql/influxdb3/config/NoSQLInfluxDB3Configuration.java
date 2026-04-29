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

package org.dromara.dante.nosql.influxdb3.config;

import jakarta.annotation.PostConstruct;
import org.dromara.dante.nosql.influxdb3.condition.ConditionalOnInfluxDB3;
import org.dromara.dante.nosql.influxdb3.pool.InfluxDB3ClientObjectPool;
import org.dromara.dante.nosql.influxdb3.pool.InfluxDB3ClientPooledObjectFactory;
import org.dromara.dante.nosql.influxdb3.properties.InfluxDB3Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: InfluxDB NoSQL 配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/9/29 15:41
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnInfluxDB3
@EnableConfigurationProperties(InfluxDB3Properties.class)
public class NoSQLInfluxDB3Configuration {

    private static final Logger log = LoggerFactory.getLogger(NoSQLInfluxDB3Configuration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- Module [NoSQL InfluxDB2] Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    public InfluxDB3ClientObjectPool influxdb2ClientObjectPool(InfluxDB3Properties influxdb3Properties) {
        InfluxDB3ClientPooledObjectFactory factory = new InfluxDB3ClientPooledObjectFactory(influxdb3Properties);
        InfluxDB3ClientObjectPool pool = new InfluxDB3ClientObjectPool(factory, influxdb3Properties);
        log.trace("[Herodotus] |- Bean [InfluxDB2 Client Object Pool] Configure.");
        return pool;
    }
}
