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

package cn.herodotus.engine.web.servlet.initializer;

import cn.herodotus.engine.core.definition.constant.SymbolConstants;
import cn.herodotus.engine.message.core.definition.strategy.RestMappingScanEventManager;
import cn.herodotus.engine.message.core.domain.RestMapping;
import cn.herodotus.engine.web.core.support.WebPropertyFinder;
import cn.herodotus.engine.web.service.initializer.AbstractRestMappingScanner;
import cn.herodotus.engine.web.service.properties.ServiceProperties;
import cn.hutool.v7.crypto.SecureUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>Description: RequestMapping扫描器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/6/2 19:52
 */
public class RestMappingScanner extends AbstractRestMappingScanner {

    private static final Logger log = LoggerFactory.getLogger(RestMappingScanner.class);

    public RestMappingScanner(ServiceProperties.Scan scan, RestMappingScanEventManager restMappingScanEventManager) {
        super(scan, restMappingScanEventManager);
    }

    public void onApplicationEvent(ApplicationContext applicationContext) {

        String contextPath = getContextPath(applicationContext);

        // 1、获取服务ID：该服务ID对于微服务是必需的。
        String serviceId = WebPropertyFinder.getApplicationName(applicationContext);

        // 2、只针对有EnableResourceServer注解的微服务进行扫描。如果变为单体架构目前不会用到EnableResourceServer所以增加的了一个Architecture判断
        if (notExecuteScanning()) {
            // 只扫描资源服务器
            log.warn("[Herodotus] |- Can not found scan annotation in Service [{}], Skip!", serviceId);
            return;
        }

        // 3、获取所有接口映射
        Map<String, RequestMappingHandlerMapping> mappings = applicationContext.getBeansOfType(RequestMappingHandlerMapping.class);

        // 4、 获取url与类和方法的对应信息
        List<RestMapping> resources = new ArrayList<>();
        for (RequestMappingHandlerMapping mapping : mappings.values()) {
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
            if (MapUtils.isNotEmpty(handlerMethods)) {
                for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
                    RequestMappingInfo requestMappingInfo = entry.getKey();
                    HandlerMethod handlerMethod = entry.getValue();

                    // 4.1、如果是被排除的requestMapping，那么就进行不扫描
                    if (isExcludedRequestMapping(handlerMethod)) {
                        continue;
                    }

                    // 4.2、拼装扫描信息
                    RestMapping restMapping = createRestMapping(serviceId, requestMappingInfo, handlerMethod, contextPath);
                    if (ObjectUtils.isEmpty(restMapping)) {
                        continue;
                    }

                    resources.add(restMapping);
                }
            }
        }

        complete(serviceId, resources);
    }

    private RestMapping createRestMapping(String serviceId, RequestMappingInfo info, HandlerMethod method, String contextPath) {
        // 4.2.1、获取类名
        // method.getMethod().getDeclaringClass().getName() 取到的是注解实际所在类的名字，比如注解在父类叫BaseController，那么拿到的就是BaseController
        // method.getBeanType().getName() 取到的是注解实际Bean的名字，比如注解在在父类叫BaseController，而实际类是SysUserController，那么拿到的就是SysUserController
        String className = method.getBeanType().getName();

        // 4.2.2、检测该类是否在GroupIds列表中
        if (isLegalGroup(className)) {
            return null;
        }

        // 5.2.3、获取不包含包路径的类名
        String classSimpleName = method.getBeanType().getSimpleName();

        // 4.2.4、获取RequestMapping注解对应的方法名
        String methodName = method.getMethod().getName();

        // 5.2.5、获取注解对应的请求类型
        RequestMethodsRequestCondition requestMethodsRequestCondition = info.getMethodsCondition();
        String requestMethods = StringUtils.join(requestMethodsRequestCondition.getMethods(), SymbolConstants.COMMA);

        // 5.2.6、获取主机对应的请求路径
        PathPatternsRequestCondition pathPatternsCondition = info.getPathPatternsCondition();
        Set<String> patternValues = pathPatternsCondition.getPatternValues();
        if (CollectionUtils.isEmpty(patternValues)) {
            return null;
        }

        String urls = patternValues.stream()
                .map(url -> toInterface(contextPath, url))
                .collect(Collectors.joining(SymbolConstants.COMMA));

        // 5.2.8、根据serviceId, requestMethods, urls生成的MD5值，作为自定义主键
        String flag = serviceId + SymbolConstants.DASH + requestMethods + SymbolConstants.DASH + urls;
        String id = SecureUtil.md5(flag);

        // 5.2.9、组装对象
        RestMapping restMapping = new RestMapping();
        restMapping.setMappingId(id);
        restMapping.setMappingCode(createCode(urls, requestMethods));
        restMapping.setServiceId(serviceId);
        Operation apiOperation = method.getMethodAnnotation(Operation.class);
        if (ObjectUtils.isNotEmpty(apiOperation)) {
            restMapping.setDescription(apiOperation.summary());
        }
        restMapping.setRequestMethod(requestMethods);
        restMapping.setUrl(urls);
        restMapping.setClassName(className);
        restMapping.setMethodName(methodName);
        return restMapping;
    }
}
