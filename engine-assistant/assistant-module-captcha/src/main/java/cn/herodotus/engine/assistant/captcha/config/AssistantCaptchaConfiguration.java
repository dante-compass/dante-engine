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

package cn.herodotus.engine.assistant.captcha.config;

import cn.herodotus.engine.assistant.captcha.properties.CaptchaProperties;
import cn.herodotus.engine.assistant.captcha.provider.ResourceProvider;
import cn.herodotus.engine.assistant.captcha.renderer.behavior.JigsawCaptchaRenderer;
import cn.herodotus.engine.assistant.captcha.renderer.behavior.WordClickCaptchaRenderer;
import cn.herodotus.engine.assistant.captcha.renderer.graphic.*;
import cn.herodotus.engine.assistant.captcha.renderer.hutool.CircleCaptchaRenderer;
import cn.herodotus.engine.assistant.captcha.renderer.hutool.GifCaptchaRenderer;
import cn.herodotus.engine.assistant.captcha.renderer.hutool.LineCaptchaRenderer;
import cn.herodotus.engine.assistant.captcha.renderer.hutool.ShearCaptchaRenderer;
import cn.herodotus.engine.core.foundation.enums.CaptchaCategory;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: 辅助模块 Captcha 配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/7/21 10:02
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CaptchaProperties.class)
public class AssistantCaptchaConfiguration {

    private static final Logger log = LoggerFactory.getLogger(AssistantCaptchaConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- Module [Assistant Captcha] Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    public ResourceProvider resourceProvider(CaptchaProperties captchaProperties) {
        ResourceProvider resourceProvider = new ResourceProvider(captchaProperties);
        log.trace("[Herodotus] |- Bean [Resource Provider] Configure.");
        return resourceProvider;
    }

    @Configuration(proxyBeanMethods = false)
    static class BehaviorCaptchaConfiguration {

        @Bean(CaptchaCategory.JIGSAW_CAPTCHA)
        public JigsawCaptchaRenderer jigsawCaptchaRenderer(ResourceProvider resourceProvider) {
            JigsawCaptchaRenderer jigsawCaptchaRenderer = new JigsawCaptchaRenderer(resourceProvider);
            log.trace("[Herodotus] |- Bean [Jigsaw Captcha Renderer] Configure.");
            return jigsawCaptchaRenderer;
        }

        @Bean(CaptchaCategory.WORD_CLICK_CAPTCHA)
        public WordClickCaptchaRenderer wordClickCaptchaRenderer(ResourceProvider resourceProvider) {
            WordClickCaptchaRenderer wordClickCaptchaRenderer = new WordClickCaptchaRenderer(resourceProvider);
            log.trace("[Herodotus] |- Bean [Word Click Captcha Renderer] Configure.");
            return wordClickCaptchaRenderer;
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class GraphicCaptchaConfiguration {

        @Bean(CaptchaCategory.ARITHMETIC_CAPTCHA)
        public ArithmeticCaptchaRenderer arithmeticCaptchaRenderer(ResourceProvider resourceProvider) {
            ArithmeticCaptchaRenderer arithmeticCaptchaRenderer = new ArithmeticCaptchaRenderer(resourceProvider);
            log.trace("[Herodotus] |- Bean [Arithmetic Captcha Renderer] Configure.");
            return arithmeticCaptchaRenderer;
        }

        @Bean(CaptchaCategory.CHINESE_CAPTCHA)
        public ChineseCaptchaRenderer chineseCaptchaRenderer(ResourceProvider resourceProvider) {
            ChineseCaptchaRenderer chineseCaptchaRenderer = new ChineseCaptchaRenderer(resourceProvider);
            log.trace("[Herodotus] |- Bean [Chinese Captcha Renderer] Configure.");
            return chineseCaptchaRenderer;
        }

        @Bean(CaptchaCategory.CHINESE_GIF_CAPTCHA)
        public ChineseGifCaptchaRenderer chineseGifCaptchaRenderer(ResourceProvider resourceProvider) {
            ChineseGifCaptchaRenderer chineseGifCaptchaRenderer = new ChineseGifCaptchaRenderer(resourceProvider);
            log.trace("[Herodotus] |- Bean [Chinese Gif Captcha Renderer] Configure.");
            return chineseGifCaptchaRenderer;
        }

        @Bean(CaptchaCategory.SPEC_GIF_CAPTCHA)
        public SpecGifCaptchaRenderer specGifCaptchaRenderer(ResourceProvider resourceProvider) {
            SpecGifCaptchaRenderer specGifCaptchaRenderer = new SpecGifCaptchaRenderer(resourceProvider);
            log.trace("[Herodotus] |- Bean [Spec Gif Captcha Renderer] Configure.");
            return specGifCaptchaRenderer;
        }

        @Bean(CaptchaCategory.SPEC_CAPTCHA)
        public SpecCaptchaRenderer specCaptchaRenderer(ResourceProvider resourceProvider) {
            SpecCaptchaRenderer specCaptchaRenderer = new SpecCaptchaRenderer(resourceProvider);
            log.trace("[Herodotus] |- Bean [Spec Captcha Renderer] Configure.");
            return specCaptchaRenderer;
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class HutoolCaptchaConfiguration {

        @Bean(CaptchaCategory.HUTOOL_LINE_CAPTCHA)
        public LineCaptchaRenderer lineCaptchaRenderer(ResourceProvider resourceProvider) {
            LineCaptchaRenderer lineCaptchaRenderer = new LineCaptchaRenderer(resourceProvider);
            log.trace("[Herodotus] |- Bean [Hutool Line Captcha Renderer] Configure.");
            return lineCaptchaRenderer;
        }

        @Bean(CaptchaCategory.HUTOOL_CIRCLE_CAPTCHA)
        public CircleCaptchaRenderer circleCaptchaRenderer(ResourceProvider resourceProvider) {
            CircleCaptchaRenderer circleCaptchaRenderer = new CircleCaptchaRenderer(resourceProvider);
            log.trace("[Herodotus] |- Bean [Hutool Circle Captcha Renderer] Configure.");
            return circleCaptchaRenderer;
        }

        @Bean(CaptchaCategory.HUTOOL_SHEAR_CAPTCHA)
        public ShearCaptchaRenderer shearCaptchaRenderer(ResourceProvider resourceProvider) {
            ShearCaptchaRenderer shearCaptchaRenderer = new ShearCaptchaRenderer(resourceProvider);
            log.trace("[Herodotus] |- Bean [Hutool Shear Captcha Renderer] Configure.");
            return shearCaptchaRenderer;
        }

        @Bean(CaptchaCategory.HUTOOL_GIF_CAPTCHA)
        public GifCaptchaRenderer gifCaptchaRenderer(ResourceProvider resourceProvider) {
            GifCaptchaRenderer gifCaptchaRenderer = new GifCaptchaRenderer(resourceProvider);
            log.trace("[Herodotus] |- Bean [Hutool Gif Captcha Renderer] Configure.");
            return gifCaptchaRenderer;
        }
    }
}
