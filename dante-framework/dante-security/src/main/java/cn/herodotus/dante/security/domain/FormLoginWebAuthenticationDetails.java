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

package cn.herodotus.dante.security.domain;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * <p>Description: 表单登录 Details 定义 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/4/12 10:32
 */
public class FormLoginWebAuthenticationDetails extends WebAuthenticationDetails {

    /**
     * 验证码是否关闭
     */
    private final Boolean enabled;
    /**
     * 请求中，验证码对应的表单参数名。对应UI Properties 里面的 captcha parameter
     */
    private final String parameterName;
    /**
     * 验证码分类
     */
    private final String category;
    private final String code;

    public FormLoginWebAuthenticationDetails(String remoteAddress, String sessionId, Boolean enabled, String parameterName, String category, String code) {
        super(remoteAddress, sessionId);
        this.enabled = enabled;
        this.parameterName = parameterName;
        this.category = category;
        this.code = code;
    }

    public FormLoginWebAuthenticationDetails(HttpServletRequest request, Boolean enabled, String parameterName, String category, String code) {
        super(request);
        this.enabled = enabled;
        this.parameterName = parameterName;
        this.category = category;
        this.code = code;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public String getCategory() {
        return category;
    }

    public String getCode() {
        return code;
    }

    public String getParameterName() {
        return parameterName;
    }
}
