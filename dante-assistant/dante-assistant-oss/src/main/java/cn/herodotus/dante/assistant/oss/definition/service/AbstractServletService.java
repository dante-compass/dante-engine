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

package cn.herodotus.dante.assistant.oss.definition.service;

import org.springframework.core.convert.converter.Converter;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * <p>Description: 阻塞式操作通用方法 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/22 18:04
 */
public abstract class AbstractServletService {

    /**
     * 响应式对象存储操作通用处理方法
     *
     * @param argument    实际应用请求参数
     * @param toRequest   实际应用请求参数转换为AWS SDK V2 XXXRequest 实体转换器
     * @param toResult    AWS SDK V2 XXXResponse 转换为实际应用响应结果实体转换器
     * @param handler     AWS SDK V2 操作API
     * @param <ArgumentA> 实际应用请求参数类型
     * @param <RequestR>  AWS SDK V2 XXXRequest 实体类型
     * @param <ResponseR> AWS SDK V2 XXXResponse 实体类型
     * @param <ResultR>   实际应用响应结果类型
     * @return 阻塞式对象存储操作返回数据
     */
    protected <ArgumentA, RequestR, ResponseR, ResultR> ResultR process(ArgumentA argument, Converter<ArgumentA, RequestR> toRequest, Converter<ResponseR, ResultR> toResult, Function<RequestR, CompletableFuture<ResponseR>> handler) {
        CompletableFuture<ResponseR> future = handler.apply(toRequest.convert(argument));
        return toResult.convert(future.join());
    }
}
