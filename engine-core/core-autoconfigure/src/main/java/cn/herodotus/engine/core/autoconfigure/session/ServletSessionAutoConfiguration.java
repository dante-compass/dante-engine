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

package cn.herodotus.engine.core.autoconfigure.session;

import cn.herodotus.engine.core.autoconfigure.jackson2.Jackson2AutoConfiguration;
import cn.herodotus.engine.core.foundation.condition.ConditionalOnServletApplication;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.SessionRepositoryCustomizer;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

/**
 * <p>Description: Servlet 环境 Spring Session 自动配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/4/6 16:03
 */
@AutoConfiguration(after = Jackson2AutoConfiguration.class)
@ConditionalOnServletApplication
@ConditionalOnClass({SessionRepository.class})
@EnableRedisIndexedHttpSession
public class ServletSessionAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ServletSessionAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [Servlet Spring Session] Configure.");
    }

    @Bean
    public SpringSessionRememberMeServices springSessionRememberMeServices() {
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        log.trace("[Herodotus] |- Bean [Spring Session Remember Me Services] Configure.");
        return rememberMeServices;
    }

    @Bean
    public SessionRepositoryCustomizer<RedisIndexedSessionRepository> sessionRepositoryCustomizer() {
        // 关闭 Spring Session 自身的清理任务，让 Redis 自行清理过期内容
        // 如果不关闭，经常会出现 java.lang.IllegalStateException: LettuceConnectionFactory has been STOPPED. Use start() to initialize it 问题
        // cleanup-cron 参数默认值通常是 "0 * * * * *"（每分钟跑一次）。通过将其设置为一个无效的 cron 表达式（如 "* * * 31 2 ?"），Spring Session 就不会创建那个后台调度任务，自然也就不会在关闭时发生冲突了
        // * * * 31 2 ? 此表达式是指2月31日执行，但是该日期不存在，所以永远不会执行
        return (sessionRepository) -> sessionRepository.setCleanupCron("* * * 31 2 ?");
    }

    @Configuration(proxyBeanMethods = false)
    @EnableSpringHttpSession
    static class SpringHttpSessionConfiguration {

    }
}
