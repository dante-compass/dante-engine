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

package cn.herodotus.engine.web.api.servlet;

import cn.herodotus.dante.core.domain.BaseEntity;
import cn.herodotus.dante.core.domain.Result;
import cn.herodotus.dante.data.commons.service.BasePageService;
import cn.herodotus.engine.web.core.annotation.Idempotent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;

/**
 * <p>Description: 通用可读可写接口定义 </p>
 * <p>
 * 如果继承该类将会自动创建相关接口并生成权限数据，所以当前仅提供基础保存和删除接口，以避免生成不必要的接口。
 *
 * @param <E>  实体
 * @param <ID> 实体 ID
 * @param <S>  Service
 * @author : gengwei.zheng
 * @date : 2025/3/29 23:16
 */
public abstract class AbstractWriteableController<E extends BaseEntity, ID extends Serializable, S extends BasePageService<E, ID>> extends AbstractReadableController<E, ID, S> {

    @Idempotent
    @Operation(summary = "保存或更新数据", description = "接收JSON数据，转换为实体，进行保存或更新",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            responses = {
                    @ApiResponse(description = "已保存数据", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    @Parameters({
            @Parameter(name = "domain", required = true, description = "可转换为实体的json数据")
    })
    @PostMapping
    @Override
    public Result<E> save(@RequestBody E domain) {
        return super.save(domain);
    }

    @Idempotent
    @Operation(summary = "删除数据", description = "根据实体ID删除数据，以及相关联的关联数据",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            responses = {
                    @ApiResponse(description = "操作消息", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    @Parameters({
            @Parameter(name = "id", required = true, in = ParameterIn.PATH, description = "实体ID，@Id注解对应的实体属性")
    })
    @DeleteMapping("/{id}")
    @Override
    public Result<String> delete(@PathVariable ID id) {
        return super.delete(id);
    }
}
