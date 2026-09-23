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

package cn.herodotus.dante.oauth2.authorization.customizer;

import cn.herodotus.dante.cache.utils.JetCacheUtils;
import cn.herodotus.dante.oauth2.commons.constant.OAuth2Constants;
import cn.herodotus.dante.security.definition.OAuth2ProtectedResourceMetadataStorage;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;

import java.util.List;

/**
 * <p>Description: OAuth 2.0 Protected Resource Metadata Repository 默认实现 </p>
 *
 * @author : gengwei_zheng
 * @date : 2026/9/21 20:36
 */
public class DefaultOAuth2ProtectedResourceMetadataStorage implements OAuth2ProtectedResourceMetadataStorage {

    private static final String KEY_COMPATIBLE = "COMPATIBLE";
    /**
     * 直接索引权限缓存，主要存储全路径权限
     * 该种权限，直接通过 Map Key 进行获取
     */
    private final Cache<String, List<String>> compatible;

    public DefaultOAuth2ProtectedResourceMetadataStorage() {
        this.compatible = JetCacheUtils.create(OAuth2Constants.CACHE_NAME__PROTECTED_RESOURCE_METADATA_COMPATIBLE, CacheType.BOTH, null, true);
    }


    /**
     * 从 compatible 缓存中读取数据
     *
     * @return PRM Scopes
     */
    private List<String> readFromIndexable() {
        return this.compatible.get(KEY_COMPATIBLE);
    }

    /**
     * 向 compatible 缓存中添加数据。
     *
     * @param scopes PRM Scopes
     */
    private void writeToCompatible(List<String> scopes) {
        this.compatible.put(KEY_COMPATIBLE, scopes);
    }

    @Override
    public void storeSupportedScopes(List<String> scopes) {
        writeToCompatible(scopes);
    }

    @Override
    public List<String> getSupportedScopes() {
        return readFromIndexable();
    }
}
