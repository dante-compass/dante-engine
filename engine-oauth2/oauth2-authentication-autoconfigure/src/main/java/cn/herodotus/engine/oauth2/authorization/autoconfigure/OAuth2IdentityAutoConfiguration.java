/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2020-2030 郑庚伟 ZHENGGENGWEI (码匠君), <herodotus@aliyun.com> Licensed under the AGPL License
 *
 * This file is part of Herodotus Stirrup.
 *
 * Herodotus Stirrup is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Herodotus Stirrup is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.herodotus.vip>.
 */

package cn.herodotus.engine.oauth2.authorization.autoconfigure;

import cn.herodotus.engine.logic.identity.config.LogicIdentityConfiguration;
import cn.herodotus.engine.oauth2.extension.config.OAuth2ExtensionConfiguration;
import cn.herodotus.engine.rest.servlet.identity.config.RestServletIdentityConfiguration;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * <p>Description: OAuth2 身份认证自动配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2024/3/15 22:32
 */
@AutoConfiguration(after = OAuth2AuthorizationServerAutoConfiguration.class)
@Import({
        OAuth2ExtensionConfiguration.class, LogicIdentityConfiguration.class, RestServletIdentityConfiguration.class
})
public class OAuth2IdentityAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OAuth2IdentityAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Auto [OAuth2 Identity] Configure.");
    }
}
