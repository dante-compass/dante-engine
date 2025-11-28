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

package cn.herodotus.engine.logic.message.constant;

import cn.herodotus.dante.core.constant.BaseConstants;

/**
 * <p>Description: 消息业务逻辑模块常量 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/2/19 16:36
 */
public interface LogicMessageConstants extends BaseConstants {

    String MESSAGE_AREA_PREFIX = AREA_PREFIX + "message:";

    String REGION_MESSAGE_ANNOUNCEMENT = MESSAGE_AREA_PREFIX + "system_announcement";
    String REGION_MESSAGE_DIALOGUE_CONTACT = MESSAGE_AREA_PREFIX + "personal:contact";
    String REGION_MESSAGE_DIALOGUE = MESSAGE_AREA_PREFIX + "personal:dialogue";
    String REGION_MESSAGE_DIALOGUE_DETAIL = MESSAGE_AREA_PREFIX + "personal:dialogue:detail";
    String REGION_MESSAGE_NOTIFICATION = MESSAGE_AREA_PREFIX + "notification_queue";
    String REGION_MESSAGE_PULL_STAMP = MESSAGE_AREA_PREFIX + "pull_stamp";
}
