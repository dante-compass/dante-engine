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

package org.dromara.dante.message.emqx.enums;

/**
 * <p>Description: Emqx 监控指标类型 </p>
 * <p>
 * 目前仅是根据 Emqx 官方文档进行了整理，如有缺少的内容后续再进行补充。
 * <a herf="https://www.emqx.io/docs/zh/latest/observability/mqtt-system-topics.html">文档地址</a>
 *
 * @author : gengwei.zheng
 * @date : 2023/11/13 20:28
 */
public enum SystemTopicCategory {
    /**
     * 仅代表Emqx集群状态信息中的 $SYS/brokers
     */
    CLUSTER,
    /**
     * Emqx集群状态信息中，除 $SYS/brokers以外的所有
     */
    INFO,
    /**
     * Emqx 客户端事件，包括：客户端上下线事件，客户端订阅与取消订阅事件
     */
    CLIENTS,
    /**
     * Emqx 系统统计类指标，包括：客户端统计，订阅统计，主题统计，路由统计
     */
    STATS,
    /**
     * Emqx 收发流量、报文、消息统计。包括：收发流量统计，MQTT 报文收发统计，MQTT 消息收发统计
     */
    METRICS,
    /**
     * Emqx 系统告警类指标
     */
    ALARMS,
    /**
     * Emqx 系统监控类指标
     */
    SYSMON;
}
