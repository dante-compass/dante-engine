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

package org.dromara.dante.persistence.sys.jpa.definition;

import org.dromara.dante.persistence.commons.definition.AbstractAuditRecord;
import org.springframework.core.convert.converter.Converter;

/**
 * <p>Description: OAuth2Secure 转换器抽象定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/1/3 17:54
 */
public abstract class AbstractOAuth2ToAuditRecordConverter<S extends AbstractOAuth2AuditRecord, T extends AbstractAuditRecord> implements Converter<S, T> {

    /**
     * 获取最终生成对象实例
     *
     * @param source 源对象。传递源对象，方便参数设置
     * @return 转换后的对象实例
     */
    protected abstract T getInstance(S source);

    /**
     * 实体转换
     *
     * @param source 统一定义请求参数
     * @return 转换后的对象实例
     */
    @Override
    public T convert(S source) {

        T target = getInstance(source);

        target.setPrincipalName(source.getPrincipalName());
        target.setClientId(source.getClientId());
        target.setIp(source.getIp());
        target.setMobile(source.getMobile());
        target.setBrowserName(source.getBrowserName());
        target.setMobileBrowser(source.getMobileBrowser());
        target.setBrowserVersion(source.getBrowserVersion());
        target.setPlatformName(source.getPlatformName());
        target.setOsName(source.getOsName());
        target.setOsVersion(source.getOsVersion());
        target.setBrowserEngineName(source.getBrowserEngineName());
        target.setBrowserEngineVersion(source.getBrowserEngineVersion());
        return target;
    }
}
