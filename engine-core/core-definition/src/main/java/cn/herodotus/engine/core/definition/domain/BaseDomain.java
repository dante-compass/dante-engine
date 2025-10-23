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

package cn.herodotus.engine.core.definition.domain;

import java.io.Serializable;

/**
 * <p>Description: 系统域对象通用定义 </p>
 * <p>
 * 额外增加 {@link BaseDomain} 定义，
 * 一方面，方便系统的很多操作的定义，例如：很多接口可以使用 {@link BaseDomain} 进行类型限定
 * 另一方面，方便统一控制实体的可序列化，以减少因缺少 {@link Serializable} 导致的很多不必要的不可序列化问题。
 * 再一方面，方便未来的统一修改，例如：需要给所有实体都加上某些操作，在该类中直接修改或定义即可。
 * 另外，使用接口比抽象类更加灵活，毕竟一个 Java 类可以实现多个接口，抽象类只能继承一个。
 * <p>
 *
 * @author : gengwei.zheng
 * @date : 2025/3/29 16:39
 */
public sealed interface BaseDomain extends Serializable permits BaseDto, BaseEntity, BaseModel, Feedback, Pagination, Response {
}
