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

package org.dromara.dante.oauth2.authorization.autoconfigure.strategy;

import org.dromara.dante.spring.context.ServiceContextHolder;
import org.dromara.dante.message.core.definition.strategy.RestMappingScanEventManager;
import org.dromara.dante.message.core.domain.RestMapping;
import org.dromara.dante.message.core.event.RestMappingGatherEvent;
import org.dromara.dante.oauth2.authorization.autoconfigure.bus.RemoteRestMappingGatherEvent;
import org.dromara.dante.oauth2.authorization.processor.SecurityAttributeAnalyzer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * <p>Description: 自定义 RequestMappingScanManager</p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/17 0:08
 */
@Component
public class DefaultRestMappingScanEventManager implements RestMappingScanEventManager {

    private final SecurityAttributeAnalyzer securityAttributeAnalyzer;

    public DefaultRestMappingScanEventManager(SecurityAttributeAnalyzer securityAttributeAnalyzer) {
        this.securityAttributeAnalyzer = securityAttributeAnalyzer;
    }

    @Override
    public Class<? extends Annotation> getScanAnnotationClass() {
        return EnableWebSecurity.class;
    }

    @Override
    public String getDestinationServiceName() {
        return ServiceContextHolder.getUpmsServiceName();
    }

    @Override
    public void postLocalStorage(List<RestMapping> restMappings) {
        securityAttributeAnalyzer.processRequestMatchers();
    }

    @Override
    public void postLocalProcess(List<RestMapping> data) {
        publishEvent(new RestMappingGatherEvent(data));
    }

    @Override
    public void postRemoteProcess(String data, String originService, String destinationService) {
        publishEvent(new RemoteRestMappingGatherEvent(data, originService, destinationService));
    }
}
