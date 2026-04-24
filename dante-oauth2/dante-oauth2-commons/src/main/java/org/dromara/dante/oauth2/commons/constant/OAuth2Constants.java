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

package org.dromara.dante.oauth2.commons.constant;

import org.dromara.dante.core.constant.BaseConstants;
import org.dromara.dante.core.constant.SymbolConstants;
import org.dromara.dante.core.constant.SystemConstants;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;

/**
 * <p>Description: OAuth2 模块通用常量 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/2/25 9:44
 */
public interface OAuth2Constants extends BaseConstants {

    String PROPERTY_SIGN_IN_FAILURE_LIMITED = PROPERTY_OAUTH2_AUTHENTICATION + ".sign-in-failure-limited";
    String PROPERTY_PERSISTENCE = PROPERTY_OAUTH2_AUTHENTICATION + ".persistence";
    String PROPERTY_FORMLOGIN = PROPERTY_OAUTH2_AUTHENTICATION + ".form-login";

    String ITEM_SIGN_IN_FAILURE_LIMITED_AUTO_UNLOCK = PROPERTY_SIGN_IN_FAILURE_LIMITED + ".auto-unlock";
    String ITEM_PERSISTENCE_SAS = PROPERTY_PERSISTENCE + ".sas";
    String ITEM_PERSISTENCE_SYS = PROPERTY_PERSISTENCE + ".sys";

    String ITEM_FORMLOGIN_LOGINPAGEURL = PROPERTY_FORMLOGIN + ".login-page-url";
    String ITEM_AUTHORIZATION_CONSENT_URI = PROPERTY_OAUTH2_AUTHENTICATION + ".authorization-consent-uri";
    String ITEM_DEVICE_VERIFICATION_SUCCESS_URI = PROPERTY_OAUTH2_AUTHENTICATION + ".device-verification-success-uri";
    String ITEM_DEVICE_VERIFICATION_FAILURE_URI = PROPERTY_OAUTH2_AUTHENTICATION + ".device-verification-failure-uri";

    /**
     * ${herodotus.oauth2.authentication.authorization-consent-uri:/oauth2/consent}
     */
    String ANNOTATION_AUTHORIZATION_CONSENT_URI = PLACEHOLDER_PREFIX + ITEM_AUTHORIZATION_CONSENT_URI + SymbolConstants.COLON + SystemConstants.OAUTH2_AUTHORIZATION_CONSENT_URI + PLACEHOLDER_SUFFIX;
    /**
     * ${herodotus.oauth2.authentication.device-verification-success-uri:/device_activation_success}
     */
    String ANNOTATION_DEVICE_VERIFICATION_SUCCESS_URI = PLACEHOLDER_PREFIX + ITEM_DEVICE_VERIFICATION_SUCCESS_URI + SymbolConstants.COLON + SystemConstants.OAUTH2_DEVICE_VERIFICATION_SUCCESS_URI + PLACEHOLDER_SUFFIX;
    /**
     * ${herodotus.oauth2.authentication.device-verification-failure-uri:/device_activation_failure}
     */
    String ANNOTATION_DEVICE_VERIFICATION_FAILURE_URI = PLACEHOLDER_PREFIX + ITEM_DEVICE_VERIFICATION_FAILURE_URI + SymbolConstants.COLON + SystemConstants.OAUTH2_DEVICE_VERIFICATION_FAILURE_URI + PLACEHOLDER_SUFFIX;
    /**
     * ${herodotus.oauth2.authentication.form-login.login-page-url:/login}
     */
    String ANNOTATION_FORMLOGIN_LOGINPAGEURL = PLACEHOLDER_PREFIX + ITEM_FORMLOGIN_LOGINPAGEURL + SymbolConstants.COLON + DefaultLoginPageGeneratingFilter.DEFAULT_LOGIN_PAGE_URL + PLACEHOLDER_SUFFIX;

    String REGION_OAUTH2_APPLICATION = AREA_PREFIX + "oauth2:application";
    String REGION_OAUTH2_COMPLIANCE = AREA_PREFIX + "oauth2:compliance";
    String REGION_OAUTH2_PERMISSION = AREA_PREFIX + "oauth2:permission";
    String REGION_OAUTH2_SCOPE = AREA_PREFIX + "oauth2:scope";
    String REGION_OAUTH2_APPLICATION_SCOPE = AREA_PREFIX + "oauth2:application:scope";

    String CACHE_NAME_TOKEN_SIGN_IN_FAILURE_LIMITED = CACHE_TOKEN_BASE_PREFIX + "sign_in:failure_limited:";
    String CACHE_NAME_TOKEN_LOCKED_USER_DETAIL = CACHE_TOKEN_BASE_PREFIX + "locked:user_details:";

    String CACHE_SECURITY_PREFIX = CACHE_PREFIX + "security:";
    String CACHE_SECURITY_METADATA_PREFIX = CACHE_SECURITY_PREFIX + "metadata:";

    String CACHE_NAME_SECURITY_METADATA_INDEXABLE = CACHE_SECURITY_METADATA_PREFIX + "indexable:";
    String CACHE_NAME_SECURITY_METADATA_COMPATIBLE = CACHE_SECURITY_METADATA_PREFIX + "compatible:";
}
