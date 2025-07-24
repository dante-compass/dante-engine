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

package cn.herodotus.engine.data.core.identifier;

import cn.herodotus.engine.data.core.annotation.SnowflakeIdGenerator;
import cn.hutool.v7.core.data.id.IdUtil;
import cn.hutool.v7.core.data.id.Snowflake;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.factory.spi.CustomIdGeneratorCreationContext;
import org.hibernate.internal.util.ReflectHelper;

import java.lang.reflect.Member;

/**
 * 雪花主键生成器，使用 hutool 的雪花主键生成器
 *
 * @author lkhsh
 * @date 2023-07-14
 */
public class SnowflakeIdGeneratorType implements IdentifierGenerator {

    private final Snowflake snowflake;
    private final Class<?> propertyType;

    public SnowflakeIdGeneratorType(SnowflakeIdGenerator config, Member member, CustomIdGeneratorCreationContext context) {
        // 工具获取的主键生成器是个单例，也就是同一个运行实例，理论上 dataCenter 和 workerId 不会重复
        snowflake = IdUtil.getSnowflake();
        // 初始化主键的类型
        propertyType = ReflectHelper.getPropertyType(member);
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        if (String.class.isAssignableFrom(propertyType)) {
            return snowflake.nextStr();
        }
        return snowflake.next();
    }

    @Override
    public boolean allowAssignedIdentifiers() {
        return true;
    }
}
