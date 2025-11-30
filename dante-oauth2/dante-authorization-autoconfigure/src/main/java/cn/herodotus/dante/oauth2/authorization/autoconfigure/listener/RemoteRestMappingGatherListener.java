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

package cn.herodotus.dante.oauth2.authorization.autoconfigure.listener;

import cn.herodotus.dante.core.utils.JacksonUtils;
import cn.herodotus.dante.message.core.domain.RestMapping;
import cn.herodotus.dante.oauth2.authorization.autoconfigure.bus.RemoteRestMappingGatherEvent;
import cn.herodotus.dante.oauth2.authorization.autoconfigure.processor.AttributeTransmitterDistributeProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * <p>Description: SecurityMetadata远程变更事件监听 </p>
 * <p>
 * 所有服务启动完成，扫描 Rest 到所有接口之后，会通过发送 {@link RemoteRestMappingGatherEvent} 事件，将扫描到的接口数据发送到 UPMS
 * <p>
 * {@link RemoteRestMappingGatherListener} 监听到 {@link RemoteRestMappingGatherEvent} 事件后，进行后续的处理。
 *
 * @author : gengwei.zheng
 * @date : 2021/8/5 16:16
 */
@Component
public class RemoteRestMappingGatherListener implements ApplicationListener<RemoteRestMappingGatherEvent> {

    private static final Logger log = LoggerFactory.getLogger(RemoteRestMappingGatherListener.class);

    private final AttributeTransmitterDistributeProcessor attributeTransmitterDistributeProcessor;

    public RemoteRestMappingGatherListener(AttributeTransmitterDistributeProcessor attributeTransmitterDistributeProcessor) {
        this.attributeTransmitterDistributeProcessor = attributeTransmitterDistributeProcessor;
    }

    @Override
    public void onApplicationEvent(RemoteRestMappingGatherEvent event) {

        log.info("[Herodotus] |- Request mapping gather REMOTE listener, response service [{}] event!", event.getOriginService());

        String requestMapping = event.getData();

        log.debug("[Herodotus] |- [R4] Request mapping process BEGIN!");

        Optional.ofNullable(requestMapping)
                .flatMap(value -> Optional.ofNullable(JacksonUtils.toList(value, RestMapping.class)))
                .ifPresent(attributeTransmitterDistributeProcessor::postRestMappings);
    }
}
