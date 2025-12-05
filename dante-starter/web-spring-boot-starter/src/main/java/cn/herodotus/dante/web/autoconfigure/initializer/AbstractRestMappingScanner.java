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

package cn.herodotus.dante.web.autoconfigure.initializer;

import cn.herodotus.dante.core.constant.SymbolConstants;
import cn.herodotus.dante.core.utils.WellFormedUtils;
import cn.herodotus.dante.message.core.definition.strategy.RestMappingScanEventManager;
import cn.herodotus.dante.message.core.domain.RestMapping;
import cn.herodotus.dante.web.support.WebPropertyFinder;
import cn.herodotus.dante.web.autoconfigure.properties.ServiceProperties;
import cn.hutool.v7.crypto.SecureUtil;
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
    private boolean isLegalGroup(String className) {
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
     * 生成 RestMapping Id。根据服务ID、请求方法，URL 以及版本生成 ID
     * @param serviceId 服务ID
     * @param requestMethods 请求方法
     * @param urls 请求 URL
     * @param version 请求版本
     * @return id
     */
    private String createId(String serviceId, String requestMethods, String urls, String version) {
        String flag = serviceId + SymbolConstants.DASH + requestMethods + SymbolConstants.DASH + urls;
        if (StringUtils.isNotEmpty(version)) {
            flag = flag + SymbolConstants.DASH + version;
        }

        return SecureUtil.md5(flag);
    }

    /**
     * 将接口相关信息，转换为系统统一定义的 {@link RestMapping} 对象
     * @param serviceId 服务ID
     * @param requestMethods 请求方法
     * @param urls 请求 URL
     * @param version 请求版本
     * @param method 接口对应的方法对象 {@link HandlerMethod}
     * @return 封装好的对象 {@link RestMapping}
     */
    protected RestMapping buildRestMapping(String serviceId, String requestMethods, String urls, String version, HandlerMethod method) {

        // 1. 获取类名
        // method.getMethod().getDeclaringClass().getName() 取到的是注解实际所在类的名字，比如注解在父类叫BaseController，那么拿到的就是BaseController
        // method.getBeanType().getName() 取到的是注解实际Bean的名字，比如注解在在父类叫BaseController，而实际类是SysUserController，那么拿到的就是SysUserController
        String className = method.getBeanType().getName();

        // 2. 检测该类是否在GroupIds列表中
        if (isLegalGroup(className)) {
            return null;
        }

        // 3. 获取RequestMapping注解对应的方法名
        String methodName = method.getMethod().getName();

        // 4. 生成 ID
        String id = createId(serviceId, requestMethods, urls, version);

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
        restMapping.setVersion(version);
        return restMapping;
    }
}
