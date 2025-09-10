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

package cn.herodotus.engine.core.definition.constant;

/**
 * <p>Description: 基础共用常量值常量 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/13 21:18
 */
public interface BaseConstants {

    /* ---------- 配置属性通用常量 ---------- */
    String PROPERTY_NAME_ENABLED = "enabled";
    String PROPERTY_ENABLED = SymbolConstants.PERIOD + PROPERTY_NAME_ENABLED;
    String PROPERTY_PREFIX_SERVER = "server";
    String PROPERTY_PREFIX_SPRING = "spring";
    String PROPERTY_PREFIX_HERODOTUS = "herodotus";
    String PROPERTY_PREFIX_REACTIVE = PROPERTY_PREFIX_HERODOTUS + ".reactive";

    String PROPERTY_SERVER_SERVLET = PROPERTY_PREFIX_SERVER + ".servlet";
    String PROPERTY_SPRING_DATA = PROPERTY_PREFIX_SPRING + ".data";
    String PROPERTY_SPRING_MAIL = PROPERTY_PREFIX_SPRING + ".mail";
    String PLACEHOLDER_PREFIX = "${";
    String PLACEHOLDER_SUFFIX = "}";

    /* ---------- Herodotus 自定义配置属性 ---------- */
    String PROPERTY_PREFIX_ASSISTANT = PROPERTY_PREFIX_HERODOTUS + ".assistant";
    String PROPERTY_PREFIX_CACHE = PROPERTY_PREFIX_HERODOTUS + ".cache";
    String PROPERTY_PREFIX_CRYPTO = PROPERTY_PREFIX_HERODOTUS + ".crypto";
    String PROPERTY_PREFIX_DATA = PROPERTY_PREFIX_HERODOTUS + ".data";
    String PROPERTY_PREFIX_ENDPOINT = PROPERTY_PREFIX_HERODOTUS + ".endpoint";
    String PROPERTY_PREFIX_LOG = PROPERTY_PREFIX_HERODOTUS + ".log";
    String PROPERTY_PREFIX_MESSAGE = PROPERTY_PREFIX_HERODOTUS + ".message";
    String PROPERTY_PREFIX_OAUTH2 = PROPERTY_PREFIX_HERODOTUS + ".oauth2";
    String PROPERTY_PREFIX_OSS = PROPERTY_PREFIX_HERODOTUS + ".oss";
    String PROPERTY_PREFIX_PLATFORM = PROPERTY_PREFIX_HERODOTUS + ".platform";
    String PROPERTY_PREFIX_SECURE = PROPERTY_PREFIX_HERODOTUS + ".secure";
    String PROPERTY_PREFIX_SERVICE = PROPERTY_PREFIX_HERODOTUS + ".service";

    String PROPERTY_LOG_LOGSTASH = PROPERTY_PREFIX_LOG + ".logstash";
    String PROPERTY_OAUTH2_AUTHENTICATION = PROPERTY_PREFIX_OAUTH2 + ".authentication";
    String PROPERTY_OAUTH2_AUTHORIZATION = PROPERTY_PREFIX_OAUTH2 + ".authorization";
    String PROPERTY_REACTIVE_WEBCLIENT = PROPERTY_PREFIX_REACTIVE + ".web-client";

    /* ---------- Spring 家族配置属性 ---------- */

    String ITEM_SERVLET_CONTEXT_PATH = PROPERTY_SERVER_SERVLET + ".context-path";
    String ITEM_SPRING_APPLICATION_NAME = PROPERTY_PREFIX_SPRING + ".application.name";
    String ITEM_SPRING_SQL_INIT_PLATFORM = PROPERTY_PREFIX_SPRING + ".sql.init.platform";
    String ITEM_PLATFORM_DATA_ACCESS_STRATEGY = PROPERTY_PREFIX_PLATFORM + ".data-access-strategy";
    String ITEM_PLATFORM_ARCHITECTURE = PROPERTY_PREFIX_PLATFORM + ".architecture";
    String ITEM_AUTHORIZATION_TOKEN_FORMAT = PROPERTY_OAUTH2_AUTHORIZATION + ".token-format";
    String ITEM_CRYPTO_STRATEGY = PROPERTY_PREFIX_CRYPTO + ".strategy";

    String ANNOTATION_SQL_INIT_PLATFORM = PLACEHOLDER_PREFIX + ITEM_SPRING_SQL_INIT_PLATFORM + PLACEHOLDER_SUFFIX;

    /* ---------- 通用缓存常量 ---------- */

    String CACHE_PREFIX = "cache:";
    String CACHE_SIMPLE_BASE_PREFIX = CACHE_PREFIX + "simple:";
    String CACHE_TOKEN_BASE_PREFIX = CACHE_PREFIX + "token:";

    String AREA_PREFIX = "data:";

}
