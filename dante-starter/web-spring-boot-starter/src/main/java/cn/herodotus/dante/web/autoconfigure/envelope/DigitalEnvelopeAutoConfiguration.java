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

package cn.herodotus.dante.web.autoconfigure.envelope;

import jakarta.annotation.PostConstruct;
import cn.herodotus.dante.web.support.crypto.AsymmetricCryptoProcessor;
import cn.herodotus.dante.web.support.crypto.SymmetricCryptoProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: 数字信封自动配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/5/2 15:26
 */
@AutoConfiguration
@EnableConfigurationProperties(DigitalEnvelopeProperties.class)
public class DigitalEnvelopeAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DigitalEnvelopeAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [Digital Envelope] Configure.");
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnCryptoStrategy(CryptoStrategy.SM)
    static class SMCryptoConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public AsymmetricCryptoProcessor asymmetricCryptoProcessor() {
            SM2CryptoProcessor processor = new SM2CryptoProcessor();
            log.trace("[Herodotus] |- Strategy [SM Asymmetric SM2 Crypto Processor] Configure.");
            return processor;
        }

        @Bean
        @ConditionalOnMissingBean
        public SymmetricCryptoProcessor symmetricCryptoProcessor() {
            SM4CryptoProcessor processor = new SM4CryptoProcessor();
            log.trace("[Herodotus] |- Strategy [SM Symmetric SM4 Crypto Processor] Configure.");
            return processor;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnCryptoStrategy(CryptoStrategy.STANDARD)
    static class StandardCryptoConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public AsymmetricCryptoProcessor asymmetricCryptoProcessor() {
            RSACryptoProcessor processor = new RSACryptoProcessor();
            log.trace("[Herodotus] |- Strategy [Standard Asymmetric RSA Crypto Processor] Configure.");
            return processor;
        }

        @Bean
        @ConditionalOnMissingBean
        public SymmetricCryptoProcessor symmetricCryptoProcessor() {
            AESCryptoProcessor processor = new AESCryptoProcessor();
            log.trace("[Herodotus] |- Strategy [Standard Symmetric AES Crypto Processor] Configure.");
            return processor;
        }
    }
}
