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

package cn.herodotus.dante.data.hibernate.generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.uuid.StandardRandomStrategy;
import org.hibernate.id.uuid.UuidValueGenerator;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.type.descriptor.java.UUIDJavaType;

import java.lang.reflect.Member;
import java.util.UUID;

/**
 * <p>Description: 基于 Hibernate 6 的自定义 Uuid 生成器 </p>
 * <p>
 * 主要解决手动设置实体 ID 不生效问题。
 *
 * @author : gengwei.zheng
 * @date : 2022/11/7 17:15
 */
public abstract class AbstractIdGeneratorType implements IdentifierGenerator {

    private final UuidValueGenerator generator;
    private final UUIDJavaType.ValueTransformer valueTransformer;

    public AbstractIdGeneratorType(Member member) {
        generator = StandardRandomStrategy.INSTANCE;

        final Class<?> propertyType = ReflectHelper.getPropertyType(member);
        this.valueTransformer = determineProperTransformer(propertyType);
    }

    private UUIDJavaType.ValueTransformer determineProperTransformer(Class<?> propertyType) {
        if (UUID.class.isAssignableFrom(propertyType)) {
            return UUIDJavaType.PassThroughTransformer.INSTANCE;
        }

        if (String.class.isAssignableFrom(propertyType)) {
            return UUIDJavaType.ToStringTransformer.INSTANCE;
        }

        if (byte[].class.isAssignableFrom(propertyType)) {
            return UUIDJavaType.ToBytesTransformer.INSTANCE;
        }

        throw new HibernateException("Unanticipated return type [" + propertyType.getName() + "] for UUID conversion");
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        return valueTransformer.transform(generator.generateUuid(session));
    }

    @Override
    public boolean allowAssignedIdentifiers() {
        return true;
    }
}
