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

package cn.herodotus.dante.data.hibernate.cache.spi;

import cn.herodotus.dante.core.domain.cache.HiberanteQueryKeyWrapper;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.cache.spi.QueryKey;
import org.hibernate.cache.spi.support.DomainDataStorageAccess;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

/**
 * <p>Description: 自定义Hibernate二级缓存DomainDataStorageAccess </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/7/12 22:06
 */
public class HerodotusDomainDataStorageAccess implements DomainDataStorageAccess {

    private static final Logger log = LoggerFactory.getLogger(HerodotusDomainDataStorageAccess.class);

    private Cache cache;

    public HerodotusDomainDataStorageAccess() {
    }

    public HerodotusDomainDataStorageAccess(Cache cache) {
        this.cache = cache;
    }

    private Object wrapper(Object key) {
        if (key instanceof QueryKey queryKey) {
            return new HiberanteQueryKeyWrapper(queryKey);
        }
        // 转换为 String 之后，在 HerodotusKeyConverter 中，直接走 String 类型的判断，防止与其它缓存内容混合在一起不好区分和处理
        // 原有修改 QueryKey 代码方式，也是通过将 QueryKey 以外的内容转换为 String 类型，从而可以简化 JetCache KeyConverter 处理逻辑
        return String.valueOf(key);
    }

    private Object get(Object key) {
        Cache.ValueWrapper value = cache.get(key);

        if (ObjectUtils.isNotEmpty(value)) {
            return value.get();
        }
        return null;
    }

    @Override
    public boolean contains(Object key) {
        Object value = this.get(wrapper(key));
        log.trace("[Herodotus] |- SPI - check is key : [{}] exist.", key);
        return ObjectUtils.isNotEmpty(value);
    }

    @Override
    public Object getFromCache(Object key, SharedSessionContractImplementor session) {
        Object value = this.get(wrapper(key));
        log.trace("[Herodotus] |- SPI - get from cache key is : [{}], value is : [{}]", key, value);
        return value;
    }

    @Override
    public void putIntoCache(Object key, Object value, SharedSessionContractImplementor session) {
        log.trace("[Herodotus] |- SPI - put into cache key is : [{}], value is : [{}]", key, value);
        cache.put(wrapper(key), value);
    }

    @Override
    public void removeFromCache(Object key, SharedSessionContractImplementor session) {
        log.trace("[Herodotus] |- SPI - remove from cache key is : [{}]", key);
        cache.evict(wrapper(key));
    }

    @Override
    public void evictData(Object key) {
        log.trace("[Herodotus] |- SPI - evict key : [{}] from cache.", key);
        cache.evict(wrapper(key));
    }

    @Override
    public void clearCache(SharedSessionContractImplementor session) {
        this.evictData();
    }

    @Override
    public void evictData() {
        cache.clear();
    }

    @Override
    public void release() {
        cache.invalidate();
    }
}
