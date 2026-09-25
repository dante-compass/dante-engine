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

package cn.herodotus.dante.persistence.commons.metadata;

import cn.herodotus.dante.core.constant.SymbolConstants;
import cn.herodotus.dante.core.enums.Protocol;
import cn.herodotus.dante.persistence.commons.definition.ClientIdMetadataDocumentResolver;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.authorization.OAuth2ClientRegistration;
import org.springframework.security.oauth2.server.authorization.http.converter.OAuth2ClientRegistrationHttpMessageConverter;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.InetAddress;

/**
 * <p>Description: CIMD 解析器 </p>
 *
 * @author : gengwei_zheng
 * @date : 2026/9/4 12:49
 */
class DefaultClientIdMetadataDocumentResolver implements ClientIdMetadataDocumentResolver {

    private final ClientIdMetadataDocumentCache cache;
    private final RestClient restClient;

    private final boolean allowHttpUrlForClientIdentifier;
    private final boolean allowLoopbackHostForClientIdentifier;

    public DefaultClientIdMetadataDocumentResolver(ClientIdMetadataDocumentCache cache, boolean allowHttpUrlForClientIdentifier, boolean allowLoopbackHostForClientIdentifier) {
        this.cache = cache;
        this.allowHttpUrlForClientIdentifier = allowHttpUrlForClientIdentifier;
        this.allowLoopbackHostForClientIdentifier = allowLoopbackHostForClientIdentifier;
        this.restClient = RestClient.builder()
                .configureMessageConverters((messageConverters) ->
                        messageConverters.addCustomConverter(new OAuth2ClientRegistrationHttpMessageConverter())
                )
                .build();
    }

    @Override
    public Result resolve(String clientIdUrl) {
        if (!isClientIdentifierValid(clientIdUrl)) {
            return null;
        }
        return retrieve(clientIdUrl);
    }

    private boolean isClientIdentifierValid(String clientIdUrl) {
        try {
            UriComponents uri = UriComponentsBuilder.fromUriString(clientIdUrl).build();
            if (Strings.CI.equals(Protocol.HTTP.getSchema(), uri.getScheme()) && !this.allowHttpUrlForClientIdentifier) {
                return false;
            }
            if (StringUtils.isBlank(uri.getHost())) {
                return false;
            }
            if (isLoopbackHost(uri.getHost()) && !this.allowLoopbackHostForClientIdentifier) {
                return false;
            }
            if (isPrivateHost(uri.getHost())) {
                return false;
            }
            if (StringUtils.isNotBlank(uri.getUserInfo())) {
                return false;
            }
            if (StringUtils.isNotBlank(uri.getFragment())) {
                return false;
            }

            String path = uri.getPath();
            if (StringUtils.isBlank(path)) {
                return false;
            }

            for (String pathSegment : uri.getPathSegments()) {
                if (Strings.CS.equals(SymbolConstants.PERIOD, pathSegment) || "..".equals(pathSegment)) {
                    return false;
                }
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private static boolean isLoopbackHost(String host) {
        if ("localhost".equalsIgnoreCase(host.trim())) {
            return true;
        }
        try {
            InetAddress address = InetAddress.getByName(host.trim());
            return address.isLoopbackAddress();
        } catch (Exception ex) {
            return false;
        }
    }

    private static boolean isPrivateHost(String host) {
        try {
            InetAddress address = InetAddress.getByName(host.trim());
            return address.isSiteLocalAddress();
        } catch (Exception ex) {
            return false;
        }
    }

    private Result retrieve(String clientIdUrl) {
        try {
            ResponseEntity<OAuth2ClientRegistration> response = this.restClient.get()
                    .uri(clientIdUrl)
                    .retrieve()
                    .toEntity(OAuth2ClientRegistration.class);
            OAuth2ClientRegistration clientRegistration = response.getBody();
            long cacheMaxAgeSeconds = cache.getMaxAgeSeconds(response.getHeaders());
            ResponseAttributes responseAttributes = new ResponseAttributes(cacheMaxAgeSeconds);
            return new Result(clientRegistration, responseAttributes);
        } catch (Exception ex) {
            return null;
        }
    }
}
