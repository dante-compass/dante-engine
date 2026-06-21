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

package cn.herodotus.dante.oauth2.authorization.autoconfigure;

import cn.herodotus.dante.message.autoconfigure.message.MessageSendingDispatcher;
import cn.herodotus.dante.oauth2.authorization.autoconfigure.condition.ConditionalOnMessageService;
import cn.herodotus.dante.oauth2.authorization.autoconfigure.listener.LocalMessageSendingListener;
import cn.herodotus.dante.oauth2.authorization.autoconfigure.listener.RemoteMessageSendingListener;
import cn.herodotus.dante.spring.condition.ConditionalOnArchitecture;
import cn.herodotus.dante.spring.enums.Architecture;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.annotation.Bean;

/**
 * <p>Description: Message 服务消息配置 </p>
 * <p>
 * 说明：
 * 当前的配置内容以及相关的监听器等内容，逻辑上放在 stirrup-message-autoconfigure 中更合适。之所以没有这样做，主要存在一些尴尬点：
 * 1. Spring Cloud BUS 如果要使用 Event 形式，那么具体发送 Event 服务要使用 {@link RemoteApplicationEventScan} 注解扫描到对应的 Remote Event。如果要将所有内容移动到 stirrup-message-autoconfigure 模块中，那么 RemoteMessageSendingEvent 也需要同步移动。
 * RemoteMessageSendingEvent 依赖 spring-cloud-bus 模块，而整个系统 spring-cloud-bus 是通过 facility-spring-boot-starter 来传递的依赖，意味着就 stirrup-message-autoconfigure 就需要依赖 facility-spring-boot-starter 模块。会增加模块的耦合性
 * 2. stirrup-message-autoconfigure 相对通用，如果本类中的相关模块都防止在该模块，同时该模块被多个不同的服务引用，那么在接收消息时可能就会出现接收端选择的问题
 * <p>
 * 所以，将相关内容全部放在 oauth2 的模块中，目前看是最合理的方式。因为：
 * 一方面，stirrup-authorization-autoconfigure 本身就必须要依赖 spring-cloud-bus 模块，解决的依赖的问题
 * 另一方面，MessageSending 本身就是为 message 意外的其它服务服务的，放在 stirrup-authorization-autoconfigure 中，那么所有资源服务器都可以灵活使用。
 *
 * @author : gengwei.zheng
 * @date : 2024/10/27 20:48
 */
@AutoConfiguration
@ConditionalOnMessageService
public class MessageServiceMessageAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MessageServiceMessageAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [OAuth2 Message Service] Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    public LocalMessageSendingListener localMessageSendingListener(MessageSendingDispatcher messageSendingDispatcher) {
        LocalMessageSendingListener listener = new LocalMessageSendingListener(messageSendingDispatcher);
        log.trace("[Herodotus] |- Bean [Local Message Sending Listener] Configure.");
        return listener;
    }

    @Bean
    @ConditionalOnArchitecture(Architecture.DISTRIBUTED)
    public RemoteMessageSendingListener remoteMessageSendingListener(MessageSendingDispatcher messageSendingDispatcher) {
        RemoteMessageSendingListener listener = new RemoteMessageSendingListener(messageSendingDispatcher);
        log.trace("[Herodotus] |- Bean [Remote Message Sending Listener] Configure.");
        return listener;
    }

//    @Configuration(proxyBeanMethods = false)
//    @ConditionalOnClass(SecuritySocketAcceptorInterceptor.class)
//    @ConditionalOnReactiveApplication
//    @AutoConfigureAfter(IntegrationAutoConfiguration.class)
//    public static class OAuth2RSocketAutoConfiguration {
//
//        @Bean
//        @ConditionalOnMissingBean
//        public PayloadSocketAcceptorInterceptor payloadSocketAcceptorInterceptor(RSocketSecurity rsocketSecurity) {
//            rsocketSecurity
//                    .authorizePayload(spec -> spec
//                            .setup().authenticated()
//                            .anyRequest().authenticated()
//                            .anyExchange().authenticated()
//                    )
//                    .simpleAuthentication(Customizer.withDefaults());
//
//            return rsocketSecurity.build();
//        }
//    }
}
