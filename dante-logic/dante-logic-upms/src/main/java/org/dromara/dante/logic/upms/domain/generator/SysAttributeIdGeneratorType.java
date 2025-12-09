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

package org.dromara.dante.logic.upms.domain.generator;

import org.apache.commons.lang3.StringUtils;
import org.dromara.dante.data.hibernate.generator.AbstractIdGeneratorType;
import org.dromara.dante.logic.upms.entity.security.SysAttribute;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.GeneratorCreationContext;

import java.lang.reflect.Member;

/**
 * <p>Description: 自定义UUID生成器 </p>
 * <p>
 * 使得保存实体类时可以在保留主键生成策略的情况下自定义表的主键
 *
 * @author : gengwei.zheng
 * @date : 2021/8/4 3:20
 */
public class SysAttributeIdGeneratorType extends AbstractIdGeneratorType {

    public SysAttributeIdGeneratorType(SysAttributeIdGenerator config, Member member, GeneratorCreationContext context) {
        super(member);
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {

        SysAttribute sysAttribute = (SysAttribute) object;

        if (StringUtils.isEmpty(sysAttribute.getAttributeId())) {
            return super.generate(session, object);
        } else {
            return sysAttribute.getAttributeId();
        }
    }
}
