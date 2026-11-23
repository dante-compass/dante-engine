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

package cn.herodotus.dante.servlet.container.autoconfigure.oauth2;

import cn.herodotus.dante.security.definition.AbstractOpaqueTokenIntrospector;
import cn.herodotus.dante.spring.constant.TypeReference;
import cn.herodotus.dante.spring.context.ServiceContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.OAuth2ResourceServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.RestClientOpaqueTokenIntrospector;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * <p>Description: 自定义 OpaqueTokenIntrospector </p>
 * <p>
 * 解决默认的 OpaqueTokenIntrospector 使用 Scope 作为权限的问题。
 *
 * @author : gengwei.zheng
 * @date : 2022/10/10 14:34
 */
public class HerodotusServletOpaqueTokenIntrospector extends AbstractOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private static final Logger log = LoggerFactory.getLogger(HerodotusServletOpaqueTokenIntrospector.class);

    private final RestClient restClient;
    private final String introspectionUri;

    private Consumer<RestClient.RequestBodySpec> requestBodyCustomizer = (b) -> {
    };

    public HerodotusServletOpaqueTokenIntrospector(OAuth2ResourceServerProperties resourceServerProperties, RestClient restClient) {
        this(getIntrospectionUri(resourceServerProperties),
                buildRestClient(restClient.mutate(), resourceServerProperties.getOpaquetoken().getClientId(), resourceServerProperties.getOpaquetoken().getClientSecret())
        );
    }

    /**
     * Creates a {@code OpaqueTokenAuthenticationProvider} with the provided parameters
     * The given {@link RestClient} should perform its own client authentication against
     * the introspection endpoint.
     *
     * @param introspectionUri The introspection endpoint uri
     * @param restClient       The client for performing the introspection request
     */
    public HerodotusServletOpaqueTokenIntrospector(String introspectionUri, RestClient restClient) {
        Assert.notNull(introspectionUri, "introspectionUri cannot be null");
        Assert.notNull(restClient, "restClient cannot be null");
        this.introspectionUri = introspectionUri;
        this.restClient = restClient;
    }

    private static String getIntrospectionUri(OAuth2ResourceServerProperties resourceServerProperties) {
        String introspectionUri = ServiceContextHolder.getTokenIntrospectionUri();
        String configIntrospectionUri = resourceServerProperties.getOpaquetoken().getIntrospectionUri();
        if (StringUtils.isNotBlank(configIntrospectionUri)) {
            introspectionUri = configIntrospectionUri;
        }
        return introspectionUri;
    }

    private static RestClient buildRestClient(RestClient.Builder builder, String clientId, String clientSecret) {
        if (StringUtils.isNoneBlank(clientId, clientSecret)) {
            builder.defaultHeaders((headers) -> headers.setBasicAuth(clientId, clientSecret));
        }
        return builder.build();
    }

    private MultiValueMap<String, String> requestBody(String token) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", token);
        return body;
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        ResponseEntity<Map<String, Object>> responseEntity = makeRequest(token);
        Map<String, Object> claims = adaptToNimbusResponse(responseEntity);
        return convertClaimsSet(claims);
    }

    private ResponseEntity<Map<String, Object>> makeRequest(String token) {
        try {
            RestClient.RequestBodySpec spec = this.restClient.post()
                    .uri(this.introspectionUri)
                    .headers((h) -> h.setAccept(List.of(MediaType.APPLICATION_JSON)))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(requestBody(token));
            this.requestBodyCustomizer.accept(spec);
            return spec.retrieve().toEntity(TypeReference.STRING_OBJECT_MAP);
        } catch (Exception ex) {
            throw new OAuth2IntrospectionException((ex.getMessage() != null) ? ex.getMessage() : "Invalid token", ex);
        }
    }

    private Map<String, Object> adaptToNimbusResponse(ResponseEntity<Map<String, Object>> responseEntity) {
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new OAuth2IntrospectionException(
                    "Introspection endpoint responded with " + responseEntity.getStatusCode());
        }
        Map<String, Object> claims = responseEntity.getBody();
        // relying solely on the authorization server to validate this token (not checking
        // 'exp', for example)
        if (claims == null) {
            return Collections.emptyMap();
        }

        boolean active = (boolean) claims.compute(OAuth2TokenIntrospectionClaimNames.ACTIVE, (k, v) -> {
            if (v instanceof String) {
                return Boolean.parseBoolean((String) v);
            }
            if (v instanceof Boolean) {
                return v;
            }
            return false;
        });
        if (!active) {
            log.trace("Did not validate token since it is inactive");
            throw new BadOpaqueTokenException("Provided token isn't active");
        }
        return claims;
    }

    /**
     * Sets the {@link Consumer} used to customize the introspection request after the
     * defaults have been applied. By default, the request is sent as
     * {@code application/x-www-form-urlencoded}, as required by
     * <a href="https://datatracker.ietf.org/doc/html/rfc7662#section-2.1">RFC 7662</a>.
     * <p>
     * Use if you need a different {@code Content-Type} or additional request settings.
     * The customizer is applied after the request is prepared and before it is sent, so
     * request material set by the customizer takes precedence over the default and over
     * any set on the {@link RestClient}.
     * </p>
     *
     * @param requestBodyCustomizer the {@link Consumer} to apply to the request
     * @since 7.2
     */
    public void setRequestBodyCustomizer(Consumer<RestClient.RequestBodySpec> requestBodyCustomizer) {
        Assert.notNull(requestBodyCustomizer, "requestBodyCustomizer cannot be null");
        this.requestBodyCustomizer = requestBodyCustomizer;
    }

    /**
     * Creates a {@code Builder} with the given
     * introspection endpoint uri.
     *
     * @param introspectionUri The introspection endpoint uri
     * @return the {@link Builder}
     */
    public static Builder withIntrospectionUri(String introspectionUri) {
        Assert.notNull(introspectionUri, "introspectionUri cannot be null");
        return new Builder(introspectionUri);
    }


    /**
     * Used to build {@link RestClientOpaqueTokenIntrospector}.
     *
     * @author Andrey Litvitski
     * @since 7.1
     */
    public static final class Builder {

        private final String introspectionUri;

        private @Nullable String clientId;

        private @Nullable String clientSecret;

        private final List<Consumer<RestClientOpaqueTokenIntrospector>> postProcessors = new ArrayList<>();

        private Builder(String introspectionUri) {
            this.introspectionUri = introspectionUri;
        }

        /**
         * The builder will {@link URLEncoder encode} the client id that you provide, so
         * please give the unencoded value.
         *
         * @param clientId The unencoded client id
         * @return the {@link Builder}
         */
        public Builder clientId(String clientId) {
            Assert.notNull(clientId, "clientId cannot be null");
            this.clientId = URLEncoder.encode(clientId, StandardCharsets.UTF_8);
            return this;
        }

        /**
         * The builder will {@link URLEncoder encode} the client secret that you provide,
         * so please give the unencoded value.
         *
         * @param clientSecret The unencoded client secret
         * @return the {@link Builder}
         */
        public Builder clientSecret(String clientSecret) {
            Assert.notNull(clientSecret, "clientSecret cannot be null");
            this.clientSecret = URLEncoder.encode(clientSecret, StandardCharsets.UTF_8);
            return this;
        }

        /**
         * Adds a {@link Consumer} to customize the
         * {@link RestClientOpaqueTokenIntrospector} after it is built. This allows for
         * additional configuration that cannot be expressed through the builder methods.
         *
         * @param postProcessor the {@link Consumer} to customize the introspector
         * @return the {@link Builder}
         */
        public Builder postProcessor(Consumer<RestClientOpaqueTokenIntrospector> postProcessor) {
            Assert.notNull(postProcessor, "postProcessor cannot be null");
            this.postProcessors.add(postProcessor);
            return this;
        }

        /**
         * Creates a {@code RestClientOpaqueTokenIntrospector}.
         *
         * @return the {@link RestClientOpaqueTokenIntrospector}
         */
        public RestClientOpaqueTokenIntrospector build() {
            RestClient restClient = buildRestClient(RestClient.builder(), this.clientId, this.clientSecret);
            RestClientOpaqueTokenIntrospector introspector = new RestClientOpaqueTokenIntrospector(
                    this.introspectionUri, restClient);
            this.postProcessors.forEach((postProcessor) -> postProcessor.accept(introspector));
            return introspector;
        }
    }
}
