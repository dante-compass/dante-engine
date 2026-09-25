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
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.authorization.ExpressionAuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import java.util.function.Supplier;

/**
 * <p>Description: MCP 表达式认证管理器 </p>
 *
 * @author : gengwei_zheng
 * @date : 2026/9/22 13:15
 */
public class McpExpressionAuthorizationManager implements AuthorizationManager<McpContext> {

    private SecurityExpressionHandler<McpContext> expressionHandler = new DefaultMcpSecurityExpressionHandler();

    private final Expression expression;

    /**
     * Creates an instance.
     *
     * @param expressionString the raw expression string to parse
     */
    public McpExpressionAuthorizationManager(String expressionString) {
        Assert.hasText(expressionString, "expressionString cannot be empty");
        this.expression = this.expressionHandler.getExpressionParser().parseExpression(expressionString);
    }

    private McpExpressionAuthorizationManager(String expressionString, SecurityExpressionHandler<McpContext> expressionHandler) {
        Assert.hasText(expressionString, "expressionString cannot be empty");
        this.expressionHandler = expressionHandler;
        this.expression = expressionHandler.getExpressionParser().parseExpression(expressionString);
    }

    /**
     * Determines the access by evaluating the provided expression.
     *
     * @param authentication the {@link Supplier} of the {@link Authentication} to check
     * @param context        the {@link McpContext} to check
     * @return an {@link ExpressionAuthorizationDecision} based on the evaluated
     * expression
     */
    @Override
    public AuthorizationResult authorize(Supplier<? extends @Nullable Authentication> authentication, McpContext context) {
        EvaluationContext ctx = this.expressionHandler.createEvaluationContext(authentication, context);
        boolean granted = ExpressionUtils.evaluateAsBoolean(this.expression, ctx);
        return new ExpressionAuthorizationDecision(granted, this.expression);
    }

    @Override
    public String toString() {
        return "WebExpressionAuthorizationManager[expression='" + this.expression + "']";
    }

    /**
     * Use a {@link DefaultMcpSecurityExpressionHandler} to create
     * {@link McpExpressionAuthorizationManager} instances.
     *
     * <p>
     * Note that publishing the {@link Builder} as a bean will allow the default
     * expression handler to be configured with a bean provider so that expressions can
     * reference beans
     *
     * @return a {@link Builder} for constructing
     * {@link McpExpressionAuthorizationManager} instances
     * @since 7.0
     */
    public static Builder withDefaults() {
        return new Builder();
    }

    /**
     * Use this {@link SecurityExpressionHandler} to create
     * {@link McpExpressionAuthorizationManager} instances
     *
     * @param expressionHandler 表达式处理器
     * @return a {@link Builder} for constructing
     * {@link McpExpressionAuthorizationManager} instances
     * @since 7.0
     */
    public static Builder withExpressionHandler(SecurityExpressionHandler<McpContext> expressionHandler) {
        return new Builder(expressionHandler);
    }

    /**
     * A {@link Builder} for constructing {@link McpExpressionAuthorizationManager}
     * instances.
     *
     * <p>
     * May be reused to create multiple instances.
     *
     * @author Josh Cummings
     * @since 7.0
     */
    public static final class Builder implements ApplicationContextAware {

        private final SecurityExpressionHandler<McpContext> expressionHandler;

        private final boolean defaultExpressionHandler;

        private Builder() {
            this.expressionHandler = new DefaultMcpSecurityExpressionHandler();
            this.defaultExpressionHandler = true;
        }

        private Builder(SecurityExpressionHandler<McpContext> expressionHandler) {
            this.expressionHandler = expressionHandler;
            this.defaultExpressionHandler = false;
        }

        /**
         * Create a {@link McpExpressionAuthorizationManager} using this
         * {@code expression}
         *
         * @param expression the expression to evaluate
         * @return the resulting {@link AuthorizationManager}
         */
        public McpExpressionAuthorizationManager expression(String expression) {
            return new McpExpressionAuthorizationManager(expression, this.expressionHandler);
        }

        @Override
        public void setApplicationContext(ApplicationContext context) throws BeansException {
            if (this.defaultExpressionHandler) {
                ((DefaultMcpSecurityExpressionHandler) this.expressionHandler).setApplicationContext(context);
            }
        }

    }
}
