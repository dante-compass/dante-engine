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

package cn.herodotus.dante.logic.upms.handler;

import cn.herodotus.dante.logic.upms.entity.oauth2.OAuth2SupportedScope;
import cn.herodotus.dante.logic.upms.service.oauth2.OAuth2SupportedScopeService;
import cn.herodotus.dante.messaging.strategy.OAuth2SupportedScopeDistributionEventManager;
import cn.herodotus.dante.spring.initializer.ApplicationReadyProcessor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>Description: Protected Resource Metadata 分发处理器 </p>
 *
 * @author : gengwei_zheng
 * @date : 2026/9/22 18:07
 */
public class OAuth2SupportedScopeDistributionHandler implements ApplicationReadyProcessor {

    private static final Logger log = LoggerFactory.getLogger(OAuth2SupportedScopeDistributionHandler.class);

    private final OAuth2SupportedScopeService oauth2SupportedScopeService;
    private final OAuth2SupportedScopeDistributionEventManager oauth2SupportedScopeDistributionEventManager;

    public OAuth2SupportedScopeDistributionHandler(OAuth2SupportedScopeService oauth2SupportedScopeService, OAuth2SupportedScopeDistributionEventManager oauth2SupportedScopeDistributionEventManager) {
        this.oauth2SupportedScopeService = oauth2SupportedScopeService;
        this.oauth2SupportedScopeDistributionEventManager = oauth2SupportedScopeDistributionEventManager;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        log.debug("[Herodotus] |- [PRM1] Application is READY, start to distribute Protected Resource Metadata!");

        List<OAuth2SupportedScope> metadata = oauth2SupportedScopeService.findAll();
        if (CollectionUtils.isNotEmpty(metadata)) {

            Map<String, List<String>> data = metadata.stream()
                    .collect(Collectors.groupingBy(
                            item -> item.getResource().getResourceCode(),
                            Collectors.mapping(OAuth2SupportedScope::getScopeCode, Collectors.toList())
                    ));

            if (MapUtils.isNotEmpty(data)) {
                data.forEach(oauth2SupportedScopeDistributionEventManager::postProcess);
            }
        }

        log.info("[Herodotus] |- Distribute Protected Resource Metadata FINISHED!");
    }
}
