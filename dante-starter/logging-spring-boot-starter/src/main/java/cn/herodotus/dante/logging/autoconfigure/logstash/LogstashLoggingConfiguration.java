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

package cn.herodotus.dante.logging.autoconfigure.logstash;

import ch.qos.logback.classic.LoggerContext;
import cn.herodotus.dante.core.constant.SymbolConstants;
import cn.herodotus.dante.logging.autoconfigure.LoggingProperties;
import cn.herodotus.dante.logging.autoconfigure.logging.LogbackConfigurator;
import cn.herodotus.engine.core.definition.utils.JacksonUtils;
import com.google.common.base.MoreObjects;
import jakarta.annotation.PostConstruct;
import net.logstash.logback.LogstashFormatter;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;
import org.apache.skywalking.apm.toolkit.log.logback.v1.x.TraceIdPatternLogbackLayout;
import org.apache.skywalking.apm.toolkit.log.logback.v1.x.logstash.TraceIdJsonProvider;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.logging.LogLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * <p>Description: 基础设置日志配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/2/5 19:01
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnLogstashEnabled
@ConditionalOnClass({LogstashFormatter.class, TraceIdPatternLogbackLayout.class})
@EnableConfigurationProperties({LogstashLoggingProperties.class})
public class LogstashLoggingConfiguration {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(LogstashLoggingConfiguration.class);

    @Value("${spring.application.name:-}")
    private String applicationName;

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Module [Logstash Logging] Configure.");
    }

    @Bean
    public LogstashTcpSocketAppender logstashTcpSocketAppender(LoggingProperties loggingProperties, LogstashLoggingProperties logstashLoggingProperties) {

        LogbackConfigurator configurator = new LogbackConfigurator(LoggerFactory.getILoggerFactory());

        LoggerContext loggerContext = configurator.getContext();

        String destination = logstashLoggingProperties.getHost() + SymbolConstants.COLON + logstashLoggingProperties.getPort();

        LogstashTcpSocketAppender logstashTcpSocketAppender = new LogstashTcpSocketAppender();
        logstashTcpSocketAppender.setName("LOGSTASH");
        logstashTcpSocketAppender.addDestination(destination);
        logstashTcpSocketAppender.setKeepAliveDuration(logstashLoggingProperties.getKeepAliveDuration());
        logstashTcpSocketAppender.setReconnectionDelay(logstashLoggingProperties.getReconnectionDelay());
        logstashTcpSocketAppender.setWriteTimeout(logstashLoggingProperties.getWriteTimeout());
        logstashTcpSocketAppender.setContext(loggerContext);

        TraceIdJsonProvider traceIdJsonProvider = new TraceIdJsonProvider();
        traceIdJsonProvider.setContext(loggerContext);

        CustomFields customFields = new CustomFields();
        customFields.setService(applicationName);

        LogstashEncoder logstashEncoder = new LogstashEncoder();
        logstashEncoder.setCustomFields(JacksonUtils.toJson(customFields));
        logstashEncoder.addProvider(traceIdJsonProvider);

        logstashTcpSocketAppender.setEncoder(logstashEncoder);

        // 添加日志输出配置
        Map<String, LogLevel> loggers = loggingProperties.getLoggers();
        loggers.forEach(configurator::logger);

        // 将 logstashTcpSocketAppender 添加到 Logback 日志中
        configurator.root(loggingProperties.getLevel(), logstashTcpSocketAppender);
        configurator.start(logstashTcpSocketAppender);

        return logstashTcpSocketAppender;
    }

    static class CustomFields {

        private String service;

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("service", service)
                    .toString();
        }
    }
}
