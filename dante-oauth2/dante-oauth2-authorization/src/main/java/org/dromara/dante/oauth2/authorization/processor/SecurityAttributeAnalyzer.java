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

package org.dromara.dante.oauth2.authorization.processor;

import org.dromara.dante.core.constant.SymbolConstants;
import org.dromara.dante.oauth2.authorization.servlet.ServletOAuth2ResourceMatcherConfigurer;
import org.dromara.dante.security.domain.AttributeTransmitter;
import org.dromara.dante.security.domain.HerodotusRequest;
import org.dromara.dante.security.domain.HerodotusSecurityAttribute;
import org.dromara.dante.spring.enums.UrlCategory;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * <p>Description: SecurityMetadata异步处理Service </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/8/1 17:43
 */
@Component
public class SecurityAttributeAnalyzer {

    private static final Logger log = LoggerFactory.getLogger(SecurityAttributeAnalyzer.class);

    private final SecurityAttributeStorage securityAttributeStorage;
    private final ServletOAuth2ResourceMatcherConfigurer servletOAuth2ResourceMatcherConfigurer;

    public SecurityAttributeAnalyzer(SecurityAttributeStorage securityAttributeStorage, ServletOAuth2ResourceMatcherConfigurer servletOAuth2ResourceMatcherConfigurer) {
        this.securityAttributeStorage = securityAttributeStorage;
        this.servletOAuth2ResourceMatcherConfigurer = servletOAuth2ResourceMatcherConfigurer;
    }

    /**
     * 直接使用 {@link org.springframework.security.authorization.DefaultAuthorizationManagerFactory} 中的方法
     *
     * @param authority 权限
     * @return 权限表达式
     */
    private String hasAuthority(String authority) {
        return "hasAuthority('" + authority + "')";
    }

    /**
     * 将解析后的数据添加对应的分组中
     *
     * @param container   分组结果数据容器
     * @param urlCategory 分组类别
     * @param resources   权限数据
     */
    private void appendToGroup(Map<UrlCategory, LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>>> container, UrlCategory urlCategory, LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> resources) {
        LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> value = new LinkedHashMap<>();

        if (container.containsKey(urlCategory)) {
            value = container.get(urlCategory);
        }

        value.putAll(resources);

        container.put(urlCategory, value);
    }

    /**
     * 将各个服务配置的静态权限数据分组
     *
     * @param securityMatchers 静态权限数据
     * @return 分组后的权限数据
     */
    private Map<UrlCategory, LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>>> groupSecurityMatchers(Map<HerodotusRequest, List<HerodotusSecurityAttribute>> securityMatchers) {

        Map<UrlCategory, LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>>> group = new LinkedHashMap<>();

        securityMatchers.forEach((key, value) -> {
            LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> resources = new LinkedHashMap<>();
            resources.put(key, value);
            appendToGroup(group, UrlCategory.getCategory(key.getPattern()), resources);
        });

        log.debug("[Herodotus] |- Grouping security matcher by category.");
        return group;
    }

    /**
     * 解析并动态组装所需要的权限。
     * <p>
     * 1. 原 spring-security-oauth
     * Spring Security 基础权限规则，来源于org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.AuthorizedUrl
     * OAuth2 权限规则来源于 org.springframework.security.oauth2.provider.expression.OAuth2SecurityExpressionMethods
     * 2. 新 spring-authorization-server
     * Spring Security 基础权限规则，来源于{@link org.springframework.security.authorization.DefaultAuthorizationManagerFactory}
     * OAuth2 权限规则来源于 目前还没有
     * <p>
     * 具体解析采用的是 Security 的 <code> org.springframework.security.access.AccessDecisionVoter} </code> 方式，而不是采用自定义的 <code> org.springframework.security.access.AccessDecisionManager </code>该方式会与默认的 httpsecurity 配置覆盖。
     * · 基本的权限验证采用的是：<code> org.springframework.security.access.vote.RoleVoter</code>
     * · scope权限采用两种方式：
     * 一种是：Spring Security org.springframework.security.oauth2.provider.vote.ScopeVoter 目前已取消
     * 另一种是：OAuth2 'hasScope'和'hasAnyScope'方式  org.springframework.security.oauth2.provider.expression.OAuth2SecurityExpressionMethods#hasAnyScope(String...)
     * <p>
     * 如果实际应用不满足可以，自己扩展AccessDecisionVoter或者AccessDecisionManager
     *
     * @param attributeTransmitter {@link AttributeTransmitter}
     * @return security权限定义集合
     */
    private List<HerodotusSecurityAttribute> analysis(AttributeTransmitter attributeTransmitter) {

        List<HerodotusSecurityAttribute> attributes = new ArrayList<>();

        if (StringUtils.isNotBlank(attributeTransmitter.getPermissions())) {
            String[] permissions = org.springframework.util.StringUtils.commaDelimitedListToStringArray(attributeTransmitter.getPermissions());
            Arrays.stream(permissions).forEach(item -> attributes.add(new HerodotusSecurityAttribute(hasAuthority(item))));
        }

        if (StringUtils.isNotBlank(attributeTransmitter.getWebExpression())) {
            attributes.add(new HerodotusSecurityAttribute(attributeTransmitter.getWebExpression()));
        }

        return attributes;
    }

    /**
     * 创建请求和权限的映射数据
     *
     * @param url              请求url
     * @param methods          请求method
     * @param version          请求版本
     * @param configAttributes Security权限{@link HerodotusSecurityAttribute}
     * @return 保存请求和权限的映射的Map
     */
    private LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> convert(String url, String methods, String version, List<HerodotusSecurityAttribute> configAttributes) {
        LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> result = new LinkedHashMap<>();
        if (StringUtils.isBlank(methods)) {
            result.put(new HerodotusRequest(url, null, version), configAttributes);
        } else {
            // 如果methods是以逗号分隔的字符串，那么进行拆分处理
            if (Strings.CS.contains(methods, SymbolConstants.COMMA)) {
                String[] multiMethod = StringUtils.split(methods, SymbolConstants.COMMA);
                for (String method : multiMethod) {
                    result.put(new HerodotusRequest(url, method, version), configAttributes);
                }
            } else {
                result.put(new HerodotusRequest(url, methods, version), configAttributes);
            }
        }

        return result;
    }

    /**
     * 将 UPMS 分发的 SecurityAttributes 数据进行权限转换并分组
     *
     * @param attributeTransmitters 权限数据
     * @return 分组后的权限数据
     */
    private Map<UrlCategory, LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>>> groupingSecurityMetadata(List<AttributeTransmitter> attributeTransmitters) {

        Map<UrlCategory, LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>>> group = new LinkedHashMap<>();

        attributeTransmitters.forEach(transmitter -> {
            LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> resources = convert(transmitter.getUrl(), transmitter.getRequestMethod(), transmitter.getVersion(), analysis(transmitter));
            appendToGroup(group, UrlCategory.getCategory(transmitter.getUrl()), resources);
        });

        log.debug("[Herodotus] |- Grouping security metadata by category.");
        return group;
    }

    /**
     * 各个服务静态化配置的权限过滤，通常为通配符型或者全路径型，很少有站位符型。即：大多数情况为 {@code Category.WILDCARD} 和 {@code Category.PLACEHOLDER}，很少有 {@code Category.FULL_PATH}
     * <p>
     * 此处的逻辑是：
     * 1. 先处理各个服务静态化配置的权限，当前假设不会有{@code Category.FULL_PATH}类型的权限。后期如果该种权限较多再补充即可。
     * 同时，静态服务都是开发人员手工配置，假定手工配置时就会对是否冲突进行处理，当然也可能出现冲突，那么这个开发人员得多不负责。
     * 2. 经过考虑，服务本地接口扫描完，就对所有的 RequestMapping 做一遍解析，现在感觉意义不大。
     * 因为，RequestMapping 汇总至 UPMS 后，还会做一次统一的分发。所以当前的设计思路是不对 RequestMapping 进行处理。后续根据需要再补充即可。
     */
    public void processRequestMatchers() {

        log.debug("[Herodotus] |- [R3] Process local configured security metadata.");

        Map<HerodotusRequest, List<HerodotusSecurityAttribute>> requestMatchers = servletOAuth2ResourceMatcherConfigurer.getPermitAllAttributes();
        if (MapUtils.isNotEmpty(requestMatchers)) {
            Map<UrlCategory, LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>>> grouping = groupSecurityMatchers(requestMatchers);

            LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> wildcards = grouping.get(UrlCategory.WILDCARD);
            securityAttributeStorage.addToStorage(wildcards, false);

            LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> fullPaths = grouping.get(UrlCategory.FULL_PATH);
            securityAttributeStorage.addToStorage(fullPaths, true);
        }
    }

    /**
     * 处理分发的 SecurityAttribute，将其转换、解析为表达式权限，并存入本地缓存，用于权限校验
     * <p>
     * 处理过程中，会根据规则对权限类型分组，然后进行去重的操作。
     *
     * @param attributeTransmitters 权限数据
     */
    public void processAttributeTransmitters(List<AttributeTransmitter> attributeTransmitters) {

        // 从缓存中获取全部带有特殊字符的匹配规则
        LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> compatibles = securityAttributeStorage.getCompatible();
        // 创建一个临时的 Matcher 容器
        LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> matchers = new LinkedHashMap<>(compatibles);

        // 对分发的 SecurityAttribute 进行分组
        Map<UrlCategory, LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>>> grouping = groupingSecurityMetadata(attributeTransmitters);

        // 拿到带有通配符的分组数据
        LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> wildcards = grouping.get(UrlCategory.WILDCARD);
        if (MapUtils.isNotEmpty(wildcards)) {
            matchers.putAll(wildcards);
            securityAttributeStorage.addToStorage(wildcards, false);
        }

        // 拿到带有占位符的分组数据，并检测是否存在冲突的匹配规则，然后将结果存入本地存储
        LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> placeholders = grouping.get(UrlCategory.PLACEHOLDER);
        log.debug("[Herodotus] |- Store placeholder type security attributes.");
        securityAttributeStorage.addToStorage(matchers, placeholders, false);

        // 拿到全路径的分组数据，并检测是否存在冲突的匹配规则，然后将结果存入本地存储
        LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> fullPaths = grouping.get(UrlCategory.FULL_PATH);
        log.debug("[Herodotus] |- Store full path type security attributes.");
        securityAttributeStorage.addToStorage(matchers, fullPaths, true);

        log.debug("[Herodotus] |- [R7] Security attributes process has FINISHED!");
    }
}
