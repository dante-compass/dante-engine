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

package org.dromara.dante.oauth2.authorization.attribute;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import org.apache.commons.collections4.MapUtils;
import org.dromara.dante.cache.jetcache.utils.JetCacheUtils;
import org.dromara.dante.oauth2.authorization.matcher.HerodotusPathPatternRequestMatcher;
import org.dromara.dante.oauth2.authorization.matcher.HerodotusRequestMatcher;
import org.dromara.dante.oauth2.commons.constants.OAuth2Constants;
import org.dromara.dante.security.domain.HerodotusRequest;
import org.dromara.dante.security.domain.HerodotusSecurityAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>Description: SecurityAttribute 本地存储 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/7/30 15:05
 */
public class RestSecurityAttributeStorage {

    private static final Logger log = LoggerFactory.getLogger(RestSecurityAttributeStorage.class);
    private static final String KEY_COMPATIBLE = "COMPATIBLE";
    /**
     * 模式匹配权限缓存。主要存储 包含 "*"、"?" 和 "{"、"}" 等特殊字符的路径权限。
     * 该种权限，需要通过遍历，利用 AntPathRequestMatcher 机制进行匹配
     */
    private final Cache<String, LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>>> compatible;
    /**
     * 直接索引权限缓存，主要存储全路径权限
     * 该种权限，直接通过 Map Key 进行获取
     */
    private final Cache<HerodotusRequest, List<HerodotusSecurityAttribute>> indexable;

    public RestSecurityAttributeStorage() {
        this.compatible = JetCacheUtils.create(OAuth2Constants.CACHE_NAME_SECURITY_METADATA_COMPATIBLE, CacheType.BOTH, null, true, true);
        this.indexable = JetCacheUtils.create(OAuth2Constants.CACHE_NAME_SECURITY_METADATA_INDEXABLE, CacheType.BOTH, null, true, true);
    }

    /**
     * 从 compatible 缓存中读取数据。
     *
     * @return 需要进行模式匹配的权限数据
     */
    private LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> readFromCompatible() {
        LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> compatible = this.compatible.get(KEY_COMPATIBLE);
        if (MapUtils.isNotEmpty(compatible)) {
            return compatible;
        }
        return new LinkedHashMap<>();

    }

    /**
     * 写入 compatible 缓存
     *
     * @param compatible 请求路径和权限配置属性映射Map
     */
    private void writeToCompatible(LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> compatible) {
        this.compatible.put(KEY_COMPATIBLE, compatible);
    }

    /**
     * 从 indexable 缓存中读取数据
     *
     * @param herodotusRequest 自定义扩展的 AntPathRequestMatchers {@link HerodotusRequest}
     * @return 权限配置属性对象集合
     */
    private List<HerodotusSecurityAttribute> readFromIndexable(HerodotusRequest herodotusRequest) {
        return this.indexable.get(herodotusRequest);
    }

    /**
     * 写入 indexable 缓存
     *
     * @param herodotusRequest 自定义扩展的 AntPathRequestMatchers {@link HerodotusRequest}
     * @param configAttributes 权限配置属性
     */
    private void writeToIndexable(HerodotusRequest herodotusRequest, List<HerodotusSecurityAttribute> configAttributes) {
        this.indexable.put(herodotusRequest, configAttributes);
    }

    /**
     * 根据请求的 url 和 method 获取权限对象
     *
     * @param url     请求 URL
     * @param method  请求 method
     * @param verison API 版本
     * @return 与请求url 和 method 匹配的权限数据，或者是空集合
     */
    public List<HerodotusSecurityAttribute> getConfigAttribute(String url, String method, String verison) {
        HerodotusRequest herodotusRequest = new HerodotusRequest(url, method, verison);
        return readFromIndexable(herodotusRequest);
    }

    /**
     * 从 compatible 缓存中获取全部不需要路径匹配的（包含*号的url）请求权限映射Map
     *
     * @return 如果缓存中存在，则返回请求权限映射Map集合，如果不存在则返回一个空的{@link LinkedHashMap}
     */
    public LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> getCompatible() {
        return readFromCompatible();
    }

    /**
     * 向 compatible 缓存中添加需要路径匹配的（包含*号的url）请求权限映射Map。
     * <p>
     * 如果缓存中不存在以{@link RequestMatcher}为Key的数据，那么添加数据
     * 如果缓存中存在以{@link RequestMatcher}为Key的数据，那么合并数据
     *
     * @param herodotusRequest 请求匹配对象 {@link HerodotusRequest}
     * @param configAttributes 权限配置 {@link HerodotusSecurityAttribute}
     */
    private void appendToCompatible(HerodotusRequest herodotusRequest, List<HerodotusSecurityAttribute> configAttributes) {
        LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> compatible = this.getCompatible();
//        compatible.merge(requestMatcher, configAttributes, (oldConfigAttributes, newConfigAttributes) -> {
//            newConfigAttributes.addAll(oldConfigAttributes);
//            return newConfigAttributes;
//        });

        // 使用merge会让整个功能的设计更加复杂，暂时改为直接覆盖已有数据，后续视情况再做变更。
        compatible.put(herodotusRequest, configAttributes);
        log.trace("[Herodotus] |- Append [{}] to Compatible cache, current size is [{}]", herodotusRequest, compatible.size());
        writeToCompatible(compatible);
    }

    /**
     * 向 compatible 缓存中添加需要路径匹配的（包含*号的url）请求权限映射Map。
     * <p>
     * 如果缓存中不存在以{@link RequestMatcher}为Key的数据，那么添加数据
     * 如果缓存中存在以{@link RequestMatcher}为Key的数据，那么合并数据
     *
     * @param configAttributes 请求权限映射Map
     */
    private void appendToCompatible(LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> configAttributes) {
        configAttributes.forEach(this::appendToCompatible);
    }

    /**
     * 向 indexable 缓存中添加需请求权限映射。
     * <p>
     * 如果缓存中不存在以{@link HerodotusRequest}为Key的数据，那么添加数据
     * 如果缓存中存在以{@link HerodotusRequest}为Key的数据，那么合并数据
     *
     * @param herodotusRequest 请求匹配对象 {@link HerodotusRequest}
     * @param configAttributes 权限配置 {@link HerodotusSecurityAttribute}
     */
    private void appendToIndexable(HerodotusRequest herodotusRequest, List<HerodotusSecurityAttribute> configAttributes) {
        writeToIndexable(herodotusRequest, configAttributes);
    }

    /**
     * 向 indexable 缓存中添加请求权限映射Map。
     *
     * @param configAttributes 请求权限映射Map
     */
    private void appendToIndexable(LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> configAttributes) {
        configAttributes.forEach(this::appendToIndexable);
    }

    /**
     * 将权限数据添加至本地存储
     *
     * @param configAttributes 权限数据
     * @param isIndexable      true 存入 indexable cache；false 存入 compatible cache
     */
    public void addToStorage(LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> configAttributes, boolean isIndexable) {
        if (MapUtils.isNotEmpty(configAttributes)) {
            if (isIndexable) {
                appendToIndexable(configAttributes);
            } else {
                appendToCompatible(configAttributes);
            }
        }
    }


    /**
     * 将权限数据添加至本地存储，存储之前进行规则冲突校验
     *
     * @param matchers    校验资源
     * @param attributes  权限数据
     * @param isIndexable true 存入 indexable cache；false 存入 compatible cache
     */
    public void addToStorage(LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> matchers, LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> attributes, boolean isIndexable) {
        LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> result = new LinkedHashMap<>();
        if (MapUtils.isNotEmpty(matchers)) {
            if (MapUtils.isNotEmpty(attributes)) {
                result = checkConflict(matchers, attributes);
            }
        } else {
            // 静态权限修改为聚合方式之后，可能会出现 matchers 为空的情况
            // 如果 matcher 为空将会丢失权限数据。所以增加一个保护措施，直接存储原有数据。
            result = attributes;
        }

        addToStorage(result, isIndexable);
    }

    /**
     * 规则冲突校验
     * <p>
     * 如存在规则冲突，则保留可支持最大化范围规则，冲突的其它规则则不保存
     *
     * @param matchers         校验资源
     * @param configAttributes 权限数据
     * @return 去除冲突的权限数据
     */
    private LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> checkConflict(LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> matchers, LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> configAttributes) {

        LinkedHashMap<HerodotusRequest, List<HerodotusSecurityAttribute>> result = new LinkedHashMap<>(configAttributes);

        for (HerodotusRequest matcher : matchers.keySet()) {
            for (HerodotusRequest item : configAttributes.keySet()) {
                // 如果是修改的是占位符类型的接口的权限，同时 matchers 中也包含该占位符权限，那么就会因为配到而导致被删除，最终导致该接口的权限无法更新保存。
                // 例如：被检测请求为 /iot/product-category/{id}，而 matchers 中也存在 /iot/product-category/{id}，那么就会被从 result 中删掉。而导致无法更新 /iot/product-category/{id} 的权限
                if (!matcher.equals(item)) {
                    HerodotusRequestMatcher requestMatcher = HerodotusPathPatternRequestMatcher.withDefaults().matcher(matcher);
                    if (requestMatcher.matches(item)) {
                        result.remove(item);
                        log.trace("[Herodotus] |- Pattern [{}] is conflict with [{}], so remove it.", item.getPattern(), matcher.getPattern());
                    }
                }
            }
        }

        return result;
    }
}
