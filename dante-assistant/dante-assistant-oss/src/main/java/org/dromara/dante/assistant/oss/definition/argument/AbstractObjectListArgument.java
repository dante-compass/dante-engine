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

package org.dromara.dante.assistant.oss.definition.argument;

import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.herodotus.dante.core.constant.SymbolConstants;

/**
 * <p>Description: 对象列表相关通用请求参数 </p>
 *
 * @author : gengwei.zheng
 * @date : 2026/2/14 18:13
 */
public abstract class AbstractObjectListArgument extends AbstractObjectRequestPayerArgument {

    @Schema(name = "对象分隔符", description = "默认值 '/'")
    private String delimiter = SymbolConstants.FORWARD_SLASH;

    @Schema(name = "对象编码")
    private String encodingType;

    @Schema(name = "查询返回最大数量", description = "默认值：1000")
    private Integer maxKeys;

    @Schema(name = "查询条件")
    private String prefix;

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    }

    public Integer getMaxKeys() {
        return maxKeys;
    }

    public void setMaxKeys(Integer maxKeys) {
        this.maxKeys = maxKeys;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("delimiter", delimiter)
                .add("encodingType", encodingType)
                .add("maxKeys", maxKeys)
                .add("prefix", prefix)
                .addValue(super.toString())
                .toString();
    }
}
