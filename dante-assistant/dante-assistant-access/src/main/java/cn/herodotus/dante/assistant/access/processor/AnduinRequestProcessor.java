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

package cn.herodotus.dante.assistant.access.processor;

import cn.herodotus.dante.assistant.access.exception.AccessConfigErrorException;
import cn.herodotus.dante.assistant.access.exception.IllegalAccessSourceException;
import cn.herodotus.dante.assistant.access.properties.AnduinProperties;
import cn.herodotus.dante.assistant.access.stamp.AnduinStateStampManager;
import cn.hutool.v7.core.util.EnumUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.dromara.anduin.config.AuthConfig;
import org.dromara.anduin.config.AuthDefaultSource;
import org.dromara.anduin.request.*;
import org.dromara.anduin.utils.AuthStateUtils;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>Description: Anduin请求的生成器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/5/22 11:23
 */
public class AnduinRequestProcessor {

    private AnduinProperties anduinProperties;
    private AnduinStateStampManager anduinStateStampManager;

    public AnduinRequestProcessor(AnduinProperties anduinProperties, AnduinStateStampManager anduinStateStampManager) {
        this.anduinProperties = anduinProperties;
        this.anduinStateStampManager = anduinStateStampManager;
    }

    private AnduinStateStampManager getAnduinStateRedisCache() {
        return anduinStateStampManager;
    }

    public AuthRequest getAuthRequest(String source) {
        AuthDefaultSource authDefaultSource = parseAuthDefaultSource(source);
        AuthConfig authConfig = getAuthConfig(authDefaultSource);
        return getAuthRequest(authDefaultSource, authConfig);
    }

    public AuthRequest getAuthRequest(String source, AuthConfig authConfig) {
        AuthDefaultSource authDefaultSource = parseAuthDefaultSource(source);
        return getAuthRequest(authDefaultSource, authConfig);
    }

    /**
     * 返回带state参数的授权url，授权回调时会带上这个state
     *
     * @param source 第三方登录的类别 {@link AuthDefaultSource}
     * @return 返回授权地址
     */
    public String getAuthorizeUrl(String source) {
        AuthRequest authRequest = this.getAuthRequest(source);
        return authRequest.authorize(AuthStateUtils.createState());
    }

    public String getAuthorizeUrl(String source, AuthConfig authConfig) {
        AuthRequest authRequest = this.getAuthRequest(source, authConfig);
        return authRequest.authorize(AuthStateUtils.createState());
    }

    public Map<String, String> getAuthorizeUrls() {
        Map<String, AuthConfig> configs = getConfigs();
        return configs.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> getAuthorizeUrl(entry.getKey(), entry.getValue())));
    }

    private Map<String, AuthConfig> getConfigs() {
        Map<String, AuthConfig> configs = anduinProperties.getConfigs();
        if (MapUtils.isEmpty(configs)) {
            throw new AccessConfigErrorException();
        }
        return configs;
    }


    private AuthConfig getAuthConfig(AuthDefaultSource authDefaultSource) {
        Map<String, AuthConfig> configs = getConfigs();

        AuthConfig authConfig = configs.get(authDefaultSource.name());
        // 找不到对应关系，直接返回空
        if (ObjectUtils.isEmpty(authConfig)) {
            throw new AccessConfigErrorException();
        }
        return authConfig;
    }

    private static AuthDefaultSource parseAuthDefaultSource(String source) {
        AuthDefaultSource authDefaultSource;

        try {
            authDefaultSource = EnumUtil.fromString(AuthDefaultSource.class, source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalAccessSourceException();
        }
        return authDefaultSource;
    }

    private AuthRequest getAuthRequest(AuthDefaultSource authDefaultSource, AuthConfig authConfig) {
        return switch (authDefaultSource) {
            case GITHUB -> new AuthGithubRequest(authConfig, this.getAnduinStateRedisCache());
            case WEIBO -> new AuthWeiboRequest(authConfig, this.getAnduinStateRedisCache());
            case GITEE -> new AuthGiteeRequest(authConfig, this.getAnduinStateRedisCache());
            case DINGTALK -> new AuthDingTalkRequest(authConfig, this.getAnduinStateRedisCache());
            case BAIDU -> new AuthBaiduRequest(authConfig, this.getAnduinStateRedisCache());
//            case CSDN -> new AuthCsdnRequest(authConfig, this.getAnduinStateRedisCache());
            case CODING -> new AuthCodingRequest(authConfig, this.getAnduinStateRedisCache());
            case OSCHINA -> new AuthOschinaRequest(authConfig, this.getAnduinStateRedisCache());
            case ALIPAY ->
                    new AuthAlipayRequest(authConfig, anduinProperties.getAlipayPublicKey(), this.getAnduinStateRedisCache());
            case QQ -> new AuthQqRequest(authConfig, this.getAnduinStateRedisCache());
            case WECHAT_MP -> new AuthWeChatMpRequest(authConfig, this.getAnduinStateRedisCache());
            case WECHAT_OPEN -> new AuthWeChatOpenRequest(authConfig, this.getAnduinStateRedisCache());
            case WECHAT_ENTERPRISE ->
                    new AuthWeChatEnterpriseQrcodeRequest(authConfig, this.getAnduinStateRedisCache());
            case WECHAT_ENTERPRISE_WEB ->
                    new AuthWeChatEnterpriseWebRequest(authConfig, this.getAnduinStateRedisCache());
            case TAOBAO -> new AuthTaobaoRequest(authConfig, this.getAnduinStateRedisCache());
            case GOOGLE -> new AuthGoogleRequest(authConfig, this.getAnduinStateRedisCache());
            case FACEBOOK -> new AuthFacebookRequest(authConfig, this.getAnduinStateRedisCache());
            case DOUYIN -> new AuthDouyinRequest(authConfig, this.getAnduinStateRedisCache());
            case LINKEDIN -> new AuthLinkedinRequest(authConfig, this.getAnduinStateRedisCache());
            case MICROSOFT -> new AuthMicrosoftRequest(authConfig, this.getAnduinStateRedisCache());
            case MI -> new AuthMiRequest(authConfig, this.getAnduinStateRedisCache());
            case TOUTIAO -> new AuthToutiaoRequest(authConfig, this.getAnduinStateRedisCache());
            case TEAMBITION -> new AuthTeambitionRequest(authConfig, this.getAnduinStateRedisCache());
            case RENREN -> new AuthRenrenRequest(authConfig, this.getAnduinStateRedisCache());
            case PINTEREST -> new AuthPinterestRequest(authConfig, this.getAnduinStateRedisCache());
            case STACK_OVERFLOW -> new AuthStackOverflowRequest(authConfig, this.getAnduinStateRedisCache());
            case HUAWEI_V3 -> new AuthHuaweiV3Request(authConfig, this.getAnduinStateRedisCache());
            case GITLAB -> new AuthGitlabRequest(authConfig, this.getAnduinStateRedisCache());
            case KUJIALE -> new AuthKujialeRequest(authConfig, this.getAnduinStateRedisCache());
            case ELEME -> new AuthElemeRequest(authConfig, this.getAnduinStateRedisCache());
            case MEITUAN -> new AuthMeituanRequest(authConfig, this.getAnduinStateRedisCache());
            case TWITTER -> new AuthTwitterRequest(authConfig, this.getAnduinStateRedisCache());
            case FEISHU -> new AuthFeishuRequest(authConfig, this.getAnduinStateRedisCache());
            case JD -> new AuthJdRequest(authConfig, this.getAnduinStateRedisCache());
            case ALIYUN -> new AuthAliyunRequest(authConfig, this.getAnduinStateRedisCache());
            case XMLY -> new AuthXmlyRequest(authConfig, this.getAnduinStateRedisCache());
            default -> null;
        };
    }
}
