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

package cn.herodotus.dante.autoconfigure.context;

import cn.herodotus.dante.web.context.ServiceContextHolderInitializerListener;
import cn.herodotus.dante.web.properties.EndpointProperties;
import cn.herodotus.dante.web.properties.PlatformProperties;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.server.autoconfigure.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: ServiceContextHolder 配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2025/12/2 12:30
 */
@AutoConfiguration
@ConditionalOnWebApplication
@EnableConfigurationProperties({EndpointProperties.class, PlatformProperties.class, ServerProperties.class})
public class ServletServiceContextAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ServletServiceContextAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [Servlet Service Context] Configure.");
    }

    /**
     * 尝试过几种 ServiceContextHolder 的初始化的方式，但都是会出现“时机”的不正确，导致 ServiceContextHolder 没有正常初始化，而导致抛错
     * 1. 利用 @AutoConfiguration 控制 Configuration 的前后顺序。这种方式始终会出现 WebSocket 先一步ServiceContextHolder初始化导致抛错。
     * 2. 利用 @AutoConfigureOrder 指定 Configuration 顺序，也是同样问题。
     * 3. 使用 Bean 的方式来构建 ServiceContextHolderBuilder，并且完成 ServiceContextHolder 的初始化的方式，始终不成功会出现时机不对的问题，导致抛错。跟踪过代码，发现使用 Bean 的方式，构建 ServiceContextHolderBuilder 根本就不执行。
     * <p>
     * 最终使用构造函数的方式，可以确保时机正确，几个参数对象设置正确，最终保证 ServiceContextHolder 的初始化时机合理
     * <p>
     * 目测 @ComponentScan 注解的优先级比 @Configuration 中 @Bean 的优先级更高。
     */
    @Configuration(proxyBeanMethods = false)
    @ComponentScan({
            "cn.herodotus.dante.autoconfigure.context.initializer"
    })
    static class ServiceContextConfiguration {

        @Bean
        public ServiceContextHolderInitializerListener serviceContextHolderListener() {
            ServiceContextHolderInitializerListener listener = new ServiceContextHolderInitializerListener();
            log.trace("[Herodotus] |- Bean [Service Context Holder Initializer Listener] Configure.");
            return listener;
        }
    }
}
