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

package org.dromara.dante.nosql.influxdb3.properties;

import com.google.common.base.MoreObjects;
import org.dromara.dante.core.constant.SystemConstants;
import org.dromara.dante.core.domain.Pool;
import org.dromara.dante.nosql.influxdb3.constant.InfluxDB3Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>Description: InfluxDB 3 配置属性 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/11/6 11:41
 */
@ConfigurationProperties(prefix = InfluxDB3Constants.PROPERTY_NOSQL_INFLUXDB3)
public class InfluxDB3Properties {

    /**
     * InfluxDB 3 连接访问地址
     */
    private String host;
    /**
     * InfluxDB 3 Token。InfluxDB 3 使用 Token 访问服务端
     */
    private String token;
    /**
     * InfluxDB 3 默认数据库。
     */
    private String database = SystemConstants.DEFAULT_BUCKET_NAME;
    /**
     * 对象池设置
     */
    private Pool pool = new Pool();

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("host", host)
                .add("token", token)
                .add("database", database)
                .add("pool", pool)
                .toString();
    }
}
