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

package cn.herodotus.dante.core.enums;

import cn.herodotus.dante.core.domain.Dictionary;
import cn.herodotus.dante.core.domain.DictionaryEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 签名方法 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/4/8 22:43
 */
@Schema(name = "签名方法")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SignatureMethod implements DictionaryEnum {

    HMAC_MD5("hmacmd5", "Hmac Md5"),
    HMAC_SHA1("hmacsha1", "Hmac SHA1"),
    HMAC_SHA256("hmacsha256", "Hmac SHA256");

    @Schema(name = "枚举值")
    private final String value;
    @Schema(name = "说明")
    private final String label;

    private static final Map<String, SignatureMethod> INDEX_MAP = new HashMap<>();
    private static final List<Dictionary> DICTIONARIES = new ArrayList<>();

    static {
        for (SignatureMethod signatureMethod : SignatureMethod.values()) {
            INDEX_MAP.put(signatureMethod.getValue(), signatureMethod);
            DICTIONARIES.add(signatureMethod.getDictionary(signatureMethod.name(), signatureMethod.ordinal()));
        }
    }

    SignatureMethod(String value, String label) {
        this.label = label;
        this.value = value;
    }

    public static SignatureMethod get(String index) {
        return INDEX_MAP.get(index);
    }

    public static List<Dictionary> getDictionaries() {
        return DICTIONARIES;
    }

    @Override
    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
