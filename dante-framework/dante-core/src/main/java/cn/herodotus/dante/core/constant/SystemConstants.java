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

package cn.herodotus.dante.core.constant;

import cn.hutool.v7.core.date.DateFormatPool;

/**
 * <p>Description: 默认常量合集 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/5/30 10:27
 */
public interface SystemConstants {

    String NONE = "none";
    /**
     * 默认租户ID
     */
    String TENANT_ID = "public";
    /**
     * 默认树形结构根节点
     */
    String TREE_ROOT_ID = SymbolConstants.ZERO;
    /**
     * 系统扫描接口默认包名
     */
    String PACKAGE_NAME = "org.dromara";
    /**
     * 默认的时间日期格式
     */
    String PATTERN__DATE = DateFormatPool.NORM_DATE_PATTERN;
    String PATTERN__DATE_TIME = DateFormatPool.NORM_DATETIME_PATTERN;
    /**
     * 数据字典数据类型
     */
    String DATA_TYPE__STRING = "STRING";
    String DATA_TYPE__NUMBER = "NUMBER";

    /* ---------- 系统信息 ---------- */

    String DN_OU = "Herodotus Cloud";
    String WEBSITE = "https://www.herodotus.cn";
    String COPYRIGHT_DETAILS = "Apache-2.0 Licensed | Copyright © 2020-2030 码 匠 君";
    String SYSTEM_NAME = DN_OU;
    String OPEN_API_SECURITY_SCHEME_BEARER_NAME = "HERODOTUS_AUTH";


    /* ---------- OAuth2 相关常量 ---------- */

    String BEARER_TYPE = "Bearer";
    String BEARER_TOKEN = BEARER_TYPE + SymbolConstants.SPACE;
    String BASIC_TYPE = "Basic";
    String BASIC_TOKEN = BASIC_TYPE + SymbolConstants.SPACE;
    /**
     * OAuth2 Default Endpoint
     */
    String OAUTH2_AUTHORIZATION_ENDPOINT = "/oauth2/authorize";
    String OAUTH2_PUSHED_AUTHORIZATION_REQUEST_ENDPOINT = "/oauth2/par";
    String OAUTH2_TOKEN_ENDPOINT = "/oauth2/token";
    String OAUTH2_TOKEN_REVOCATION_ENDPOINT = "/oauth2/revoke";
    String OAUTH2_TOKEN_INTROSPECTION_ENDPOINT = "/oauth2/introspect";
    String OAUTH2_CLIENT_REGISTRATION_ENDPOINT = "/oauth2/register";
    String OAUTH2_DEVICE_AUTHORIZATION_ENDPOINT = "/oauth2/device_authorization";
    String OAUTH2_DEVICE_VERIFICATION_ENDPOINT = "/oauth2/device_verification";
    String OAUTH2_JWK_SET_ENDPOINT = "/oauth2/jwks";
    String OIDC_CLIENT_REGISTRATION_ENDPOINT = "/connect/register";
    String OIDC_LOGOUT_ENDPOINT = "/connect/logout";
    String OIDC_USER_INFO_ENDPOINT = "/userinfo";
    /**
     * OAuth2 Custom Endpoint
     */
    String OAUTH2_AUTHORIZATION_CONSENT_URI = "/oauth2/consent";
    String OAUTH2_DEVICE_ACTIVATION_URI = "/oauth2/device_activation";
    String OAUTH2_DEVICE_VERIFICATION_SUCCESS_URI = "/device_activated";
    String OAUTH2_DEVICE_VERIFICATION_FAILURE_URI = "/device_activation_failure";
    /**
     * Oauth2 模式类型
     */
    String USERNAME = "username";
    String PASSWORD = "password";
    String SOCIAL_CREDENTIALS = "social_credentials";
    /**
     * OAuth2 Token Custom Attribute
     */
    String CODE = "code";
    String ROLES = "roles";
    String AUTHORITIES = "authorities";
    String EMPLOYEE_ID = "employeeId";
    String AVATAR = "avatar";
    String PRINCIPAL = "principal";
    String SOURCE = "source";
    String LICENSE = "license";
    String COPYRIGHT = "copyright";
    /**
     * 从 OidcScope 中拷贝的默认 Scope 方便以后使用
     */
    String SCOPE_OPENID = "openid";
    String SCOPE_EMAIL = "email";
    String SCOPE_PROFILE = "profile";
    String SCOPE_ADDRESS = "address";
    String SCOPE_PHONE = "phone";
    /**
     * for OpenID Connect 1.0 Dynamic Client Registration
     */
    String SCOPE_CLIENT_CREATE = "client.create";
    /**
     * for OpenID Connect 1.0 Dynamic Client Configuration
     */
    String SCOPE_CLIENT_READ = "client.read";
    /**
     * for Dynamic Client Configuration
     */
    String TOKEN_FORMAT = "token_format";

    /* ---------- Security 相关常量 ---------- */

    /**
     * 静态资源指定模式
     */
    String MATCHER__STATIC = "/static/**";
    String MATCHER__WEBJARS = "/webjars/**";
    /**
     * 默认用户 Session 属性
     */
    String SESSION__USER_PRINCIPAL = "USER_PRINCIPAL";
    /**
     * 签名算法属性
     */
    String KEY__TIMESTAMP = "timestamp";
    String KEY__RANDOM = "random";

    /* ---------- OSS 相关常量 ---------- */

    /**
     * 默认的存储桶名称
     */
    String DEFAULT_BUCKET_NAME = "herodotus-cloud";
    /**
     * 默认的证书存储目录
     */
    String DEFAULT_CERTIFICATE_DIRECTORY = "certificate";
    /**
     * 默认的 JSON Schema 存储目录
     */
    String DEFAULT_JSON_SCHEMA_DIRECTORY = "jsonschema";

    /* ---------- IOT 相关常量 ---------- */

    /**
     * 物联网自定义属性
     */
    String PARAMETER__PRODUCT_KEY = "product_key";
    String EMQX_WEBHOOK_URI = "/open/emqx/webhook";

    /* ---------- 测试相关常量 ---------- */

    /**
     * 大部分测试的测试资源，放入本地的 resources 目录即可满足。
     * 但还是有部分测试资源，还是需要外部环境，才能验证具体的可用性。
     * <p>
     * 定义测试用途的通用目录，外部资源的统一使用和管控。
     */
    String TESTING__WINDOWS_DEFAULT_FOLDER = "D:/workspaces/Testing";

}
