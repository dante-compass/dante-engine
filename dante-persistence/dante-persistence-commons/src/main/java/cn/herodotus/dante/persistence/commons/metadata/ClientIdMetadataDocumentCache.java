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

package cn.herodotus.dante.persistence.commons.metadata;

import cn.herodotus.dante.cache.enhance.caffeine.CaffeineNeverExpire;
import cn.herodotus.dante.core.constant.RegexPool;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Description: Client Id Metadata Document 数据缓存 </p>
 *
 * @author : gengwei_zheng
 * @date : 2026/9/4 11:06
 */
class ClientIdMetadataDocumentCache {

    private static final Pattern MAX_AGE_PATTERN = Pattern.compile(RegexPool.MAX_AGE, Pattern.CASE_INSENSITIVE);

    private final long cacheMaxAge;
    private final Cache<String, CacheEntry> idToEntry;
    private final Cache<String, CacheEntry> clientIdToEntry;

    public ClientIdMetadataDocumentCache(Duration cacheMaxAge) {
        this.idToEntry = Caffeine.newBuilder().expireAfter(new CaffeineNeverExpire<>()).build();
        this.clientIdToEntry = Caffeine.newBuilder().expireAfter(new CaffeineNeverExpire<>()).build();
        this.cacheMaxAge = cacheMaxAge.getSeconds();
    }

    @Nullable
    public RegisteredClient getById(String id) {
        Assert.hasText(id, "id cannot be empty");
        CacheEntry cacheEntry = this.idToEntry.getIfPresent(id);
        if (ObjectUtils.isEmpty(cacheEntry)) {
            return null;
        }

        if (cacheEntry.isExpired()) {
            evict(cacheEntry.registeredClient);
            return null;
        }
        return cacheEntry.registeredClient;
    }

    @Nullable
    public RegisteredClient getByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");
        CacheEntry cacheEntry = this.clientIdToEntry.getIfPresent(clientId);
        if (ObjectUtils.isEmpty(cacheEntry)) {
            return null;
        }
        if (cacheEntry.isExpired()) {
            evict(cacheEntry.registeredClient);
            return null;
        }
        return cacheEntry.registeredClient;
    }

    public void put(RegisteredClient registeredClient, long expiryMillis) {
        CacheEntry cacheEntry = new CacheEntry(registeredClient, expiryMillis);
        this.clientIdToEntry.put(registeredClient.getClientId(), cacheEntry);
        this.idToEntry.put(registeredClient.getId(), cacheEntry);
    }

    private void evict(RegisteredClient registeredClient) {
        this.clientIdToEntry.invalidate(registeredClient.getClientId());
        this.idToEntry.invalidate(registeredClient.getId());
    }

    public long getMaxAgeSeconds(HttpHeaders headers) {
        String cacheControl = headers.getFirst(HttpHeaders.CACHE_CONTROL);
        if (StringUtils.isNotBlank(cacheControl)) {
            if (cacheControl.toLowerCase(Locale.ROOT).contains("no-store")) {
                return -1;
            }
            Matcher matcher = MAX_AGE_PATTERN.matcher(cacheControl);
            if (matcher.find()) {
                long maxAge = Long.parseLong(matcher.group(1));
                if (maxAge <= 0) {
                    return -1;
                }
                return Math.min(maxAge, cacheMaxAge);
            }
        }
        return cacheMaxAge;
    }

    private record CacheEntry(RegisteredClient registeredClient, long expiryMillis) {
        boolean isExpired() {
            return System.currentTimeMillis() > this.expiryMillis;
        }
    }

}
