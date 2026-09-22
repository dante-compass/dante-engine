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

package cn.herodotus.dante.oauth2.authorization.expression;

import cn.herodotus.dante.security.domain.McpContext;
import org.jspecify.annotations.Nullable;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.AbstractSecurityExpressionHandler;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.function.Supplier;

/**
 * <p>Description: MCP Security 表达式处理器默认实现 </p>
 *
 * @author : gengwei_zheng
 * @date : 2026/9/22 13:19
 */
public class DefaultMcpSecurityExpressionHandler extends AbstractSecurityExpressionHandler<McpContext> implements SecurityExpressionHandler<McpContext> {

    private static final String DEFAULT_ROLE_PREFIX = "ROLE_";

    private String defaultRolePrefix = DEFAULT_ROLE_PREFIX;

    @Override
    public EvaluationContext createEvaluationContext(Supplier<? extends @Nullable Authentication> authentication, McpContext context) {
        McpSecurityExpressionRoot root = createSecurityExpressionRoot(authentication, context);
        StandardEvaluationContext ctx = new StandardEvaluationContext(root);
        ctx.setBeanResolver(getBeanResolver());
        return ctx;
    }

    @Override
    protected SecurityExpressionOperations createSecurityExpressionRoot(@Nullable Authentication authentication, McpContext context) {
        return createSecurityExpressionRoot(() -> authentication, context);
    }

    private McpSecurityExpressionRoot createSecurityExpressionRoot(
            Supplier<? extends @Nullable Authentication> authentication, McpContext context) {
        McpSecurityExpressionRoot root = new McpSecurityExpressionRoot(authentication, context);
        root.setAuthorizationManagerFactory(getAuthorizationManagerFactory());
        root.setPermissionEvaluator(getPermissionEvaluator());
        if (!DEFAULT_ROLE_PREFIX.equals(this.defaultRolePrefix)) {
            // Ensure SecurityExpressionRoot can strip the custom role prefix
            root.setDefaultRolePrefix(this.defaultRolePrefix);
        }
        return root;
    }
}
