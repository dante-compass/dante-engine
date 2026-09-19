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

package cn.herodotus.dante.oauth2.authorization.attribute;

import cn.herodotus.dante.cache.utils.JetCacheUtils;
import cn.herodotus.dante.messaging.domain.SecurityAttribute;
import cn.herodotus.dante.oauth2.authorization.cache.HerodotusMcp;
import cn.herodotus.dante.oauth2.authorization.definition.HerodotusSecurityAttribute;
import cn.herodotus.dante.oauth2.commons.constant.OAuth2Constants;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: MCP 接口权限元数据分析处理器 </p>
 *
 * @author : gengwei_zheng
 * @date : 2026/9/19 0:12
 */
class McpSecurityAttributeAnalyzer extends AbstractSecurityAttributeAnalyzer {

    private static final Logger log = LoggerFactory.getLogger(McpSecurityAttributeAnalyzer.class);

    /**
     * 直接索引权限缓存，主要存储全路径权限
     * 该种权限，直接通过 Map Key 进行获取
     */
    private final Cache<HerodotusMcp, List<HerodotusSecurityAttribute>> indexable;

    public McpSecurityAttributeAnalyzer() {
        this.indexable = JetCacheUtils.create(OAuth2Constants.CACHE_NAME__SECURITY_ATTRIBUTE_MCP_INDEXABLE, CacheType.BOTH, null, true);
    }

    /**
     * 从 indexable 缓存中读取数据
     *
     * @param herodotusMcp 自定义扩展的 AntPathRequestMatchers {@link HerodotusMcp}
     * @return 权限配置属性对象集合
     */
    private List<HerodotusSecurityAttribute> readFromIndexable(HerodotusMcp herodotusMcp) {
        return this.indexable.get(herodotusMcp);
    }

    /**
     * 向 indexable 缓存中添加需请求权限映射。
     *
     * @param herodotusMcp 请求匹配对象 {@link HerodotusMcp}
     * @param attributes   权限配置 {@link HerodotusSecurityAttribute}
     */
    private void writeToIndexable(HerodotusMcp herodotusMcp, List<HerodotusSecurityAttribute> attributes) {
        this.indexable.put(herodotusMcp, attributes);
    }

    /**
     * 向 indexable 缓存中添加请求权限映射Map。
     *
     * @param attributes 请求权限映射Map
     */
    private void writeToIndexable(Map<HerodotusMcp, List<HerodotusSecurityAttribute>> attributes) {
        this.indexable.putAll(attributes);
    }

    public List<HerodotusSecurityAttribute> findAttribute(String name, String feature, String className, String methodName) {
        HerodotusMcp herodotusGrpc = new HerodotusMcp(name, feature, className, methodName);
        return readFromIndexable(herodotusGrpc);
    }

    @Override
    public void postAttributeDistributionProcess(List<SecurityAttribute> securityAttributes) {
        Map<HerodotusMcp, List<HerodotusSecurityAttribute>> result = new LinkedHashMap<>();

        securityAttributes.forEach(attribute -> {
            HerodotusMcp herodotusGrpc = new HerodotusMcp(attribute.getName(), attribute.getRequestMethod(), attribute.getClassName(), attribute.getMethodName());
            result.put(herodotusGrpc, analysis(attribute));
        });

        log.debug("[Herodotus] |- Grouping security metadata by category.");
        if (MapUtils.isNotEmpty(result)) {
            writeToIndexable(result);
        }
    }
}
