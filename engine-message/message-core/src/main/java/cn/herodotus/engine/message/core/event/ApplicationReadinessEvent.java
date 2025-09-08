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

package cn.herodotus.engine.message.core.event;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * <p>Description: 应用准就绪 Event </p>
 * <p>
 * Spring Boot 生命周期事件中，在启动过程最后一个事件就是 {@link ApplicationReadyEvent}。在大多数场景下，需要在服务启动之后做一些操作，使用这个事件就足够了。
 * 结合 Herodotus Cloud 自身需求。有些场景的操作需要在 RequestMapping Scan 完成，并且已经正确存储了数据之后，再进行。所以 {@link ApplicationReadyEvent} 就无法满足需求。
 * <p>
 * 定义 {@link ApplicationReadinessEvent} 事件。这个事件是在 RequestMapping 扫描完成，并且 <code>cn.herodotus.engine.oauth2.authorization.autoconfigure.processor.AttributeTransmitterDistributeProcessor</code> 已经正确存储了数据后才会发出。
 * <p>
 * 如果是在微服务环境下，不同服务发送 RequestMapping 数据时机不同，会出现发送多个 {@link ApplicationReadinessEvent} 情况。所以目前仅在单体环境下开启。
 *
 * @author : gengwei.zheng
 * @date : 2025/3/2 22:35
 */
public class ApplicationReadinessEvent extends ApplicationEvent {

    public ApplicationReadinessEvent(Object source) {
        super(source);
    }

    public ApplicationReadinessEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
