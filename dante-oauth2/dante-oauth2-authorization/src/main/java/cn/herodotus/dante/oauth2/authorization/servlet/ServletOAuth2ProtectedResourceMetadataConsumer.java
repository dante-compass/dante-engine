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

package cn.herodotus.dante.oauth2.authorization.servlet;

import cn.herodotus.dante.security.definition.OAuth2ProtectedResourceMetadataStorage;
import cn.herodotus.dante.spring.context.ServiceContextHolder;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.oauth2.server.resource.OAuth2ProtectedResourceMetadata;

import java.util.List;
import java.util.function.Consumer;

/**
 * <p>Description: 自定义 OAuth2 Protected Resource Metadata </p>
 * <p>
 * MCP 规范中需要使用到该配置
 *
 * @author : gengwei_zheng
 * @date : 2026/9/2 15:41
 */
public class ServletOAuth2ProtectedResourceMetadataConsumer implements Consumer<OAuth2ProtectedResourceMetadata.Builder> {

    private final OAuth2ProtectedResourceMetadataStorage oauth2ProtectedResourceMetadataStorage;

    public ServletOAuth2ProtectedResourceMetadataConsumer(OAuth2ProtectedResourceMetadataStorage oauth2ProtectedResourceMetadataStorage) {
        this.oauth2ProtectedResourceMetadataStorage = oauth2ProtectedResourceMetadataStorage;
    }

    @Override
    public void accept(OAuth2ProtectedResourceMetadata.Builder builder) {
        builder.authorizationServer(ServiceContextHolder.getUaaServiceUri());

        List<String> scopes = oauth2ProtectedResourceMetadataStorage.getSupportedScopes();
        if (CollectionUtils.isNotEmpty(scopes)) {
            scopes.forEach(builder::scope);
        }
    }
}
