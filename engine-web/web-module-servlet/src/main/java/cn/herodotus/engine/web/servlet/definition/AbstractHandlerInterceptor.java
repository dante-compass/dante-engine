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

package cn.herodotus.engine.web.servlet.definition;

import cn.herodotus.engine.core.definition.constant.SymbolConstants;
import cn.herodotus.engine.web.core.servlet.utils.SessionUtils;
import cn.hutool.v7.crypto.SecureUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * <p>Description: 基础拦截器 </p>
 * <p>
 * 定义拦截器通用方法
 *
 * @author : gengwei.zheng
 * @date : 2022/10/18 21:40
 */
public abstract class AbstractHandlerInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AbstractHandlerInterceptor.class);

    protected String generateRequestKey(HttpServletRequest request) {

        String sessionId = SessionUtils.analyseSessionId(request);

        String url = request.getRequestURI();
        String method = request.getMethod();

        if (StringUtils.isNotBlank(sessionId)) {
            String key = SecureUtil.md5(sessionId + SymbolConstants.COLON + url + SymbolConstants.COLON + method);
            log.debug("[Herodotus] |- IdempotentInterceptor key is [{}].", key);
            return key;
        } else {
            log.warn("[Herodotus] |- IdempotentInterceptor cannot create key, because sessionId is null.");
            return null;
        }
    }
}
