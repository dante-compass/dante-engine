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

package cn.herodotus.engine.web.service.initializer;

import cn.herodotus.engine.core.definition.constant.SymbolConstants;
import cn.herodotus.engine.core.definition.utils.WellFormedUtils;
import cn.herodotus.engine.message.core.domain.RestMapping;
import cn.herodotus.engine.message.core.definition.strategy.RestMappingScanEventManager;
import cn.herodotus.engine.web.core.support.WebPropertyFinder;
import cn.herodotus.engine.web.service.properties.ServiceProperties;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Description: RequestMapping 扫描器抽象定义 </p>
 * <p>
 * 提取 RequestMapping 扫描共性内容，方便维护
 *
 * @author : gengwei.zheng
 * @date : 2024/1/31 23:38
 */
public abstract class AbstractRestMappingScanner implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger log = LoggerFactory.getLogger(AbstractRestMappingScanner.class);

    private final ServiceProperties.Scan scan;
    private final RestMappingScanEventManager restMappingScanEventManager;

    protected AbstractRestMappingScanner(ServiceProperties.Scan scan, RestMappingScanEventManager restMappingScanEventManager) {
        this.scan = scan;
        this.restMappingScanEventManager = restMappingScanEventManager;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        ApplicationContext applicationContext = event.getApplicationContext();

        log.debug("[Herodotus] |- [R1] Application is READY, start to scan request mapping!");

        onApplicationEvent(applicationContext);
    }

    protected abstract void onApplicationEvent(ApplicationContext applicationContext);

    /**
     * 检测RequestMapping是否需要被排除
     *
     * @param handlerMethod HandlerMethod
     * @return boolean
     */
    protected boolean isExcludedRequestMapping(HandlerMethod handlerMethod) {
        if (!isSpringAnnotationMatched(handlerMethod)) {
            return true;
        }

        return !isSwaggerAnnotationMatched(handlerMethod);
    }

    /**
     * 如果开启isJustScanRestController，那么就只扫描RestController
     *
     * @param handlerMethod HandlerMethod
     * @return boolean
     */
    protected boolean isSpringAnnotationMatched(HandlerMethod handlerMethod) {
        if (scan.getJustScanRestController()) {
            return handlerMethod.getMethod().getDeclaringClass().getAnnotation(RestController.class) != null;
        }

        return true;
    }

    /**
     * 有ApiIgnore注解的方法不扫描, 没有ApiOperation注解不扫描
     *
     * @param handlerMethod HandlerMethod
     * @return boolean
     */
    protected boolean isSwaggerAnnotationMatched(HandlerMethod handlerMethod) {
        if (handlerMethod.getMethodAnnotation(Hidden.class) != null) {
            return false;
        }

        Operation operation = handlerMethod.getMethodAnnotation(Operation.class);
        return ObjectUtils.isNotEmpty(operation) && !operation.hidden();
    }

    /**
     * 如果当前class的groupId在GroupId列表中，那么就进行扫描，否则就排除
     *
     * @param className 当前扫描的controller类名
     * @return Boolean
     */
    protected boolean isLegalGroup(String className) {
        if (StringUtils.isNotEmpty(className)) {
            List<String> groupIds = scan.getScanGroupIds();
            List<String> result = groupIds.stream().filter(groupId -> Strings.CS.contains(className, groupId)).collect(Collectors.toList());
            return CollectionUtils.sizeIsEmpty(result);
        } else {
            return true;
        }
    }

    /**
     * 根据 url 和 method 生成与当前 url 对应的 code。
     * <p>
     * 例如：
     * 1. POST /element 生成为 post:element
     * 2. /open/identity/session 生成为 open:identity:session
     *
     * @param url            请求 url
     * @param requestMethods 请求 method。
     * @return url 对应的 code
     */
    protected String createCode(String url, String requestMethods) {
        String[] search = new String[]{SymbolConstants.OPEN_CURLY_BRACE, SymbolConstants.CLOSE_CURLY_BRACE, SymbolConstants.FORWARD_SLASH};
        String[] replacement = new String[]{SymbolConstants.BLANK, SymbolConstants.BLANK, SymbolConstants.COLON};
        String code = StringUtils.replaceEach(url, search, replacement);

        String result = StringUtils.isNotBlank(requestMethods) ? StringUtils.lowerCase(requestMethods) + code : Strings.CS.removeStart(code, SymbolConstants.COLON);
        log.trace("[Herodotus] |- Create code [{}] for Request [{}] : [{}]", result, requestMethods, url);
        return result;
    }

    /**
     * 判断当前环境是否符合扫描的条件设定
     *
     * @return 是否执行扫描
     */
    protected boolean notExecuteScanning() {
        return !restMappingScanEventManager.isPerformScan();
    }

    /**
     * 扫描完成操作
     *
     * @param serviceId 服务ID
     * @param resources 扫描到的资源
     */
    protected void complete(String serviceId, List<RestMapping> resources) {
        if (CollectionUtils.isNotEmpty(resources)) {
            log.debug("[Herodotus] |- [R2] Request mapping scan found [{}] resources in service [{}], go to next stage!", serviceId, resources.size());
            restMappingScanEventManager.postProcess(resources);
        } else {
            log.debug("[Herodotus] |- [R2] Request mapping scan can not find any resources in service [{}]!", serviceId);
        }

        log.info("[Herodotus] |- Request Mapping Scan for Service: [{}] FINISHED!", serviceId);
    }

    /**
     * 从 {@link ApplicationContext} 中读取 Context Path
     *
     * @param applicationContext 应用上下文 {@link ApplicationContext}
     * @return 如果有 Context Path 就返回实际值，如果没有或者为 '/' 则返回空串。
     */
    protected String getContextPath(ApplicationContext applicationContext) {
        String contextPath = WebPropertyFinder.getContextPath(applicationContext);
        if (StringUtils.isNotBlank(contextPath) && !Strings.CS.equals(contextPath, SymbolConstants.FORWARD_SLASH)) {
            return WellFormedUtils.robustness(contextPath, SymbolConstants.FORWARD_SLASH, false, false);
        } else {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 拼接实际的 URL。
     *
     * @param contextPath 上下文路径
     * @param url         实际的 Controller 地址。
     * @return 实际的 URL。
     */
    protected String toInterface(String contextPath, String url) {
        if (StringUtils.isBlank(contextPath)) {
            return url;
        } else {
            return contextPath + url;
        }
    }
}
