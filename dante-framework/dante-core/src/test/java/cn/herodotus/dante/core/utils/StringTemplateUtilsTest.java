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

package cn.herodotus.dante.core.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: {@link StringTemplateUtils} 测试 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/5/4 22:54
 */
public class StringTemplateUtilsTest {

    @Test
    void testReplace() throws Exception {
        String format = "sys/${productKey}/${deviceName}/thing/event/${identifier}";
        String source = "sys/aaa/bbb/thing/event/property";

        Map<String, Object> variables = new HashMap<>();
        variables.put("productKey", "aaa");
        variables.put("deviceName", "bbb");
        variables.put("identifier", "property");

        String result = StringTemplateUtils.replace(format, variables);
        System.out.println(result);

        Assertions.assertEquals(source, result, "根据模版提取占位符变量结果出错");
    }


    @Test
    void testExtract() throws Exception {
        String format = "sys/${productKey}/${deviceName}/thing/event/${identifier}";
        String source = "sys/aaa/bbb/thing/event/property";

        Map<String, String> result = StringTemplateUtils.extract(format, source);

        Assertions.assertNotNull(result, "根据模版提取占位符变量出错");// 输出 {code=1234, time=5}
        System.out.println(result);

        Assertions.assertEquals("{identifier=property, productKey=aaa, deviceName=bbb}", result.toString(), "根据模版提取占位符变量结果出错");
    }

    @Test
    void testSpecialSymbols() throws Exception {
        String format = "sys/${productKey}/${deviceName}/thing/event/${identifier}";
        String source = "sys/pk123@abc/device-01:test/thing/event/property";

        Map<String, String> result = StringTemplateUtils.extract(format, source);

        Assertions.assertNotNull(result, "根据模版提取占位符变量出错");// 输出 {code=1234, time=5}
        System.out.println(result);

        Assertions.assertEquals("{identifier=property, productKey=pk123@abc, deviceName=device-01:test}", result.toString(), "根据模版提取占位符变量结果出错");
    }

}
