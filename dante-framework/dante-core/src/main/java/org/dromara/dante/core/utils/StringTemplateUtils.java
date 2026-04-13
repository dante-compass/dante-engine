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

package org.dromara.dante.core.utils;

import org.apache.commons.text.StringSubstitutor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Description: 字符串模版工具类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/5/13 16:50
 */
public class StringTemplateUtils {
    /**
     * 格式化 ${} 占位符字符串。
     *
     * @param template  包含占位符的字符串模版
     * @param variables 占位符变量对应的值
     * @return 替换后的字符串
     */
    public static String replace(String template, Map<String, Object> variables) {
        StringSubstitutor sub = new StringSubstitutor(variables);
        return sub.replace(template);
    }

    /**
     * 格式化 ${} 占位符字符串。
     *
     * @param template 包含占位符的字符串模版
     * @param key      变量名称
     * @param value    变量值
     * @return 替换后的字符串
     */
    public static String replace(String template, String key, Object value) {
        return replace(template, Map.of(key, value));
    }

    /**
     * 将 ${} 模版转换为正则表达式。使用正则表达式提取变量嘴硬的值
     *
     * @param template 包含占位符的字符串模版
     * @param source   字符串
     * @return 变量及对应的值 {@link Map}
     */
    private static Map<String, String> extractByRegex(String template, String source) {
        // 将模板中的占位符转换为正则捕获组（如 ${code} → (?<code>\w+)）
        String regex = template.replaceAll("\\$\\{(\\w+)}", "(?<$1>\\\\w+)");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);

        Map<String, String> result = new HashMap<>();
        if (matcher.find()) {
            // 提取所有命名捕获组的值
            Matcher placeholderMatcher = Pattern.compile("\\$\\{(\\w+)}").matcher(template);
            while (placeholderMatcher.find()) {
                String variableName = placeholderMatcher.group(1);
                result.put(variableName, matcher.group(variableName));
            }
        }
        return result;
    }

    /**
     * 根据 ${} 字符串模版，反向提取占位符对应变量的值
     *
     * @param template 包含占位符的字符串模版
     * @param source   字符串
     * @return 变量及对应的值 {@link Map}
     */
    public static Map<String, String> extract(String template, String source) {
        // 生成变量值映射
        Map<String, String> values = extractByRegex(template, source);

        // 使用 StringSubstitutor 验证替换结果是否匹配输入
        StringSubstitutor substitutor = new StringSubstitutor(values);
        String replaced = substitutor.replace(template);
        return replaced.equals(source) ? values : Map.of();
    }
}
