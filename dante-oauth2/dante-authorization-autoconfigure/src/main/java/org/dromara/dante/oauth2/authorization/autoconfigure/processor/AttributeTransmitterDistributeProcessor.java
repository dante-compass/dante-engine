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

package org.dromara.dante.oauth2.authorization.autoconfigure.processor;

import org.dromara.dante.spring.context.ServiceContextHolder;
import org.dromara.dante.spring.founction.ListConverter;
import org.dromara.dante.security.domain.AttributeTransmitter;
import org.dromara.dante.logic.upms.converter.SysAttributeToAttributeTransmitterConverter;
import org.dromara.dante.logic.upms.converter.SysInterfacesToSysAttributesConverter;
import org.dromara.dante.logic.upms.entity.security.SysAttribute;
import org.dromara.dante.logic.upms.entity.security.SysInterface;
import org.dromara.dante.logic.upms.service.security.SysAttributeService;
import org.dromara.dante.logic.upms.service.security.SysInterfaceService;
import org.dromara.dante.message.core.definition.strategy.StrategyEventManager;
import org.dromara.dante.message.core.domain.RestMapping;
import org.dromara.dante.message.core.event.ApplicationReadinessEvent;
import org.dromara.dante.oauth2.authorization.autoconfigure.bus.RemoteAttributeTransmitterSyncEvent;
import org.dromara.dante.oauth2.authorization.processor.SecurityAttributeAnalyzer;
import com.google.common.collect.ImmutableList;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>Description: SecurityMetadata数据处理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/8/8 14:00
 */
@Component
public class AttributeTransmitterDistributeProcessor implements StrategyEventManager<List<AttributeTransmitter>> {

    private static final Logger log = LoggerFactory.getLogger(AttributeTransmitterDistributeProcessor.class);

    private final ListConverter<SysInterface, SysAttribute> toSysAttributes;
    private final ListConverter<SysAttribute, AttributeTransmitter> toTransmitters;

    private final SysAttributeService sysAttributeService;
    private final SysInterfaceService sysInterfaceService;
    private final SecurityAttributeAnalyzer securityAttributeAnalyzer;

    public AttributeTransmitterDistributeProcessor(SysAttributeService sysAttributeService, SysInterfaceService sysInterfaceService, SecurityAttributeAnalyzer securityAttributeAnalyzer) {
        this.sysAttributeService = sysAttributeService;
        this.sysInterfaceService = sysInterfaceService;
        this.securityAttributeAnalyzer = securityAttributeAnalyzer;
        this.toSysAttributes = new SysInterfacesToSysAttributesConverter();
        this.toTransmitters = new SysAttributeToAttributeTransmitterConverter();
    }

    /**
     * UPMS 服务既要处理各个服务权限数据的分发，也要处理自身服务权限数据
     *
     * @param data 事件携带数据
     */
    @Override
    public void postLocalProcess(List<AttributeTransmitter> data) {
        securityAttributeAnalyzer.processAttributeTransmitters(data);
    }

    @Override
    public void postRemoteProcess(String data, String originService, String destinationService) {
        publishEvent(new RemoteAttributeTransmitterSyncEvent(data, originService, destinationService));
    }

    /**
     * 将SysAuthority表中存在，但是SysSecurityAttribute中不存在的数据同步至SysSecurityAttribute，保证两侧数据一致
     */
    @Transactional(rollbackFor = Exception.class)
    public void postRestMappings(List<RestMapping> restMappings) {

        // 将各个服务发送回来的 requestMappings 存储到 SysInterface 中
        List<SysInterface> storedInterfaces = sysInterfaceService.storeRequestMappings(restMappings);

        if (CollectionUtils.isNotEmpty(storedInterfaces)) {
            log.debug("[Herodotus] |- [R5] Request mapping store success, start to merge security metadata!");

            // 查询将新增的 SysInterface，将其转存到 SysAttribute 中
            List<SysInterface> sysInterfaces = sysInterfaceService.findAllocatable();
            if (CollectionUtils.isNotEmpty(sysInterfaces)) {
                List<SysAttribute> elements = toSysAttributes.convert(sysInterfaces);
                List<SysAttribute> result = sysAttributeService.saveAllAndFlush(elements);
                if (CollectionUtils.isNotEmpty(result)) {
                    log.debug("[Herodotus] |- Merge security attribute SUCCESS and FINISHED!");
                } else {
                    log.error("[Herodotus] |- Merge Security attribute failed!, Please Check!");
                }
            } else {
                log.debug("[Herodotus] |- No security attribute requires merge, SKIP!");
            }

            // 执行权限数据分发
            distributeServiceSecurityAttributes(storedInterfaces);

            if (!ServiceContextHolder.isDistributedArchitecture()) {
                publishEvent(new ApplicationReadinessEvent("Attribute Transmitter Distribute Success"));
            }
//
//            List<SysAttribute> sysAttributes = sysAttributeService.findAll();
//            this.postGroupProcess(sysAttributes);
        }
    }

    private void distributeServiceSecurityAttributes(List<SysInterface> storedInterfaces) {
        // 每次处理都是只针对一个服务，所以该组数据 serviceId 肯定都相同
        storedInterfaces.stream()
                .findAny()
                .map(SysInterface::getServiceId)
                .ifPresent(this::distributionToService);
    }

    public void distributionToService(String serviceId) {
        List<SysAttribute> sysAttributes = sysAttributeService.findAllByServiceId(serviceId);
        if (CollectionUtils.isNotEmpty(sysAttributes)) {
            List<AttributeTransmitter> attributeTransmitters = toTransmitters.convert(sysAttributes);
            log.debug("[Herodotus] |- [R6] Synchronization permissions to service [{}]", serviceId);
            this.postProcess(serviceId, attributeTransmitters);
        }
    }

    public void distributeChangedSecurityAttribute(SysAttribute sysAttribute) {
        AttributeTransmitter attributeTransmitter = toTransmitters.from(sysAttribute);
        postProcess(attributeTransmitter.getServiceId(), ImmutableList.of(attributeTransmitter));
    }
}
