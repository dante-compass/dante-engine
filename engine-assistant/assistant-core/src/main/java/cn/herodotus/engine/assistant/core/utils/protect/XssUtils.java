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

package cn.herodotus.engine.assistant.core.utils.protect;

import cn.herodotus.engine.assistant.core.utils.ResourceResolver;
import cn.herodotus.engine.assistant.definition.constants.SymbolConstants;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.dromara.hutool.json.JSONUtil;
import org.owasp.validator.html.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

/**
 * <p>Description: Antisamy 单例 工具类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/8/29 16:15
 */
public class XssUtils {

    private static final Logger log = LoggerFactory.getLogger(XssUtils.class);

    private static volatile XssUtils INSTANCE;
    private final AntiSamy antiSamy;
    private final String nbsp;
    private final String quot;

    private XssUtils() {
        Policy policy = createPolicy();
        this.antiSamy = ObjectUtils.isNotEmpty(policy) ? new AntiSamy(policy) : new AntiSamy();
        this.nbsp = cleanHtml(SymbolConstants.NBSP);
        this.quot = cleanHtml(SymbolConstants.QUOTE);
    }

    private static XssUtils getInstance() {
        if (ObjectUtils.isEmpty(INSTANCE)) {
            synchronized (XssUtils.class) {
                if (ObjectUtils.isEmpty(INSTANCE)) {
                    INSTANCE = new XssUtils();
                }
            }
        }

        return INSTANCE;
    }

    public static String cleaning(String taintedHTML) {
        // 对转义的HTML特殊字符（<、>、"等）进行反转义，因为AntiSamy调用scan方法时会将特殊字符转义
        String cleanHtml = StringEscapeUtils.unescapeHtml4(getInstance().cleanHtml(taintedHTML));

        if (StringUtils.startsWith(cleanHtml, SymbolConstants.NEW_LINE)) {
            // StringEscapeUtils.unescapeHtml4 转换某些内容时，会在开头增加 \n。去除之后才好判断，否则下面判断是否是 json 会出错。
            cleanHtml = StringUtils.removeStart(cleanHtml, SymbolConstants.NEW_LINE);
        }

        if (JSONUtil.isTypeJSON(cleanHtml) && StringUtils.contains(cleanHtml, SymbolConstants.NEW_LINE)) {
            // AntiSamy会把“ ”转换 \n。如果出现时间字符串，中间包含空格就会出现错误"
            cleanHtml = cleanHtml.replaceAll(SymbolConstants.NEW_LINE, SymbolConstants.SPACE);
        }
        // AntiSamy会把“&nbsp;”转换成乱码，把双引号转换成"&quot;" 先将&nbsp;的乱码替换为空，双引号的乱码替换为双引号
        String temp = cleanHtml.replaceAll(getInstance().nbsp, SymbolConstants.BLANK);
        String result = temp.replaceAll(getInstance().quot, SymbolConstants.QUOTE);
        log.trace("[Herodotus] |- Antisamy process value from [{}] to [{}]", taintedHTML, result);
        return result;
    }

    private Policy createPolicy() {
        try {
            URL url = ResourceResolver.getURL("classpath:antisamy/antisamy-anythinggoes.xml");
            return Policy.getInstance(url);
        } catch (IOException | PolicyException e) {
            log.warn("[Herodotus] |- Antisamy create policy error! {}", e.getMessage());
            return null;
        }
    }

    private CleanResults scan(String taintedHtml) throws ScanException, PolicyException {
        return antiSamy.scan(taintedHtml);
    }

    private String cleanHtml(String taintedHtml) {
        try {
            // 使用AntiSamy清洗数据
            final CleanResults cleanResults = scan(taintedHtml);
            return cleanResults.getCleanHTML();
        } catch (ScanException | PolicyException e) {
            log.error("[Herodotus] |- Antisamy scan catch error! {}", e.getMessage());
            return taintedHtml;
        }
    }
}
