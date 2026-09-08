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

package cn.herodotus.dante.security.definition;

import cn.herodotus.dante.security.domain.OAuth2ApplicationType;

import java.io.Serializable;

/**
 * <p>Description: 扩展的 ClientSettings 参数 </p>
 * <p>
 * Spring Authorization Server 客户端中，ClientSettings 支持扩展属性。所有自定义的扩展属性，在此接口中定义方便使用维护。
 *
 * @author : gengwei_zheng
 * @date : 2026/9/6 14:42
 */
public interface ExtendedClientSettingsDetails extends Serializable {

    /**
     * 上级客户端ID。主要用于客户端动态注册场景。
     * <p>
     * 标准的客户端动态注册，不需要上级客户端的任何信息，仅需要通过上级客户端生成 "Initial" Access Token 即可。
     * <p>
     * 在物联网场景下，Product 是 Device 的上级客户端，一方面需要将 Product 与 Device 进行关联，另一方面 Device 需要用到 ProductKey 信息。
     * 所以将 ProductKey 信息作为一项必要的认证数据进行传输，即将其作为 Device 的 Parent Client ID，来实现信息的关联。
     * <p>
     * 目前，系统中除了物联网设备动态注册外，暂时还没有其它功能需求会使用到 Parent Client ID
     *
     * @return 上级客户端ID
     */
    default String getParentClientId() {
        return null;
    }

    /**
     * 客户端类别，用于区分不同途径的客户端动态注册。需求来源为 MCP 规范
     *
     * @return 客户端类别
     */
    default String getApplicationType() {
        return OAuth2ApplicationType.WEB.getValue();
    }

    /**
     * 支持 OAuth2 Resource Indicator
     *
     * @return Resource Ids
     */
    default String getResourceIds() {
        return null;
    }
}
