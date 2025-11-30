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

import cn.herodotus.dante.core.builder.EnumDictionaryBuilder;
import cn.herodotus.dante.core.domain.Dictionary;
import cn.herodotus.dante.message.core.definition.strategy.EnumDictionaryGatherEventManager;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.util.List;

/**
 * <p>Description: 枚举数据字典收集器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/8/23 17:09
 */
public class EnumDictionaryGather implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger log = LoggerFactory.getLogger(EnumDictionaryGather.class);

    private final EnumDictionaryBuilder enumDictionaryBuilder;
    private final EnumDictionaryGatherEventManager enumDictionaryGatherEventManager;

    public EnumDictionaryGather(EnumDictionaryBuilder enumDictionaryBuilder, EnumDictionaryGatherEventManager enumDictionaryGatherEventManager) {
        this.enumDictionaryBuilder = enumDictionaryBuilder;
        this.enumDictionaryGatherEventManager = enumDictionaryGatherEventManager;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        log.debug("[Herodotus] |- [E1] Application is READY, start to scan enum dictionary!");

        List<Dictionary> dictionaries = enumDictionaryBuilder.getDictionaries();

        if (CollectionUtils.isNotEmpty(dictionaries)) {
            log.debug("[Herodotus] |- [E2] Enum dictionary scan found [{}] resources in current service, go to next stage!", dictionaries.size());
            enumDictionaryGatherEventManager.postProcess(dictionaries);
        } else {
            log.debug("[Herodotus] |- [E2] Enum dictionary scan can not find any resources in current service!");
        }

        log.info("[Herodotus] |- Enum dictionary scan for current service FINISHED!");
    }
}
