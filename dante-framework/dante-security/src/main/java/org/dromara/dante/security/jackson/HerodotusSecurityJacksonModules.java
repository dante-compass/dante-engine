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

package org.dromara.dante.security.jackson;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.jackson.SecurityJacksonModule;
import org.springframework.util.ClassUtils;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.cfg.MapperBuilder;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.jsontype.PolymorphicTypeValidator;
import tools.jackson.databind.module.SimpleModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This utility class will find all the Jackson modules contributed by Spring Security in
 * the classpath (except {@code WebauthnJacksonModule}), enable automatic inclusion of
 * type information and configure a {@link PolymorphicTypeValidator} that handles the
 * validation of class names.
 *
 * <p>
 * <pre>
 *     ClassLoader loader = getClass().getClassLoader();
 *     JsonMapper mapper = JsonMapper.builder()
 * 				.addModules(SecurityJacksonModules.getModules(loader))
 * 				.build();
 * </pre>
 * <p>
 * If needed, you can add custom classes to the validation handling.
 * <p>
 * <pre>
 *     ClassLoader loader = getClass().getClassLoader();
 *     BasicPolymorphicTypeValidator.Builder builder = BasicPolymorphicTypeValidator.builder()
 *     			.allowIfSubType(MyCustomType.class);
 *     JsonMapper mapper = JsonMapper.builder()
 * 				.addModules(SecurityJacksonModules.getModules(loader, builder))
 * 	   			.build();
 * </pre>
 *
 * @author Sebastien Deleuze
 * @author Jitendra Singh
 * @since 7.0
 */
public final class HerodotusSecurityJacksonModules {

    private static final Logger log = LoggerFactory.getLogger(HerodotusSecurityJacksonModules.class);

    private static final List<String> securityJacksonModuleClasses = Arrays.asList(
            "org.springframework.security.jackson.CoreJacksonModule",
            "org.springframework.security.web.jackson.WebJacksonModule",
            "org.springframework.security.web.server.jackson.WebServerJacksonModule");

    private static final String webServletJacksonModuleClass = "org.springframework.security.web.jackson.WebServletJacksonModule";

    private static final String oauth2ClientJacksonModuleClass = "org.springframework.security.oauth2.client.jackson.OAuth2ClientJacksonModule";

    private static final String oauth2AuthorizationServerJacksonModuleClass = "org.springframework.security.oauth2.server.authorization.jackson.OAuth2AuthorizationServerJacksonModule";

    private static final String ldapJacksonModuleClass = "org.springframework.security.ldap.jackson.LdapJacksonModule";

    private static final String saml2JacksonModuleClass = "org.springframework.security.saml2.jackson.Saml2JacksonModule";

    private static final String casJacksonModuleClass = "org.springframework.security.cas.jackson.CasJacksonModule";

    private static final String herodotusSecurityJacksonModuleClass = "org.dromara.dante.security.jackson.HerodotusJacksonModule";
    private static final String herodotusOAuth2JacksonModuleClass = "org.dromara.dante.oauth2.persistence.sas.jpa.jackson.OAuth2TokenJacksonModule";

    private static final boolean webServletPresent;

    private static final boolean oauth2ClientPresent;

    private static final boolean oauth2AuthorizationServerPresent;

    private static final boolean ldapJacksonPresent;

    private static final boolean saml2JacksonPresent;

    private static final boolean casJacksonPresent;

    private static final boolean herodotusSecurityJacksonPresent;

    private static final boolean herodotusOAuth2JacksonPresent;

    static {
        ClassLoader classLoader = HerodotusSecurityJacksonModules.class.getClassLoader();
        webServletPresent = ClassUtils.isPresent("jakarta.servlet.http.Cookie", classLoader);
        oauth2ClientPresent = ClassUtils.isPresent("org.springframework.security.oauth2.client.OAuth2AuthorizedClient",
                classLoader);
        oauth2AuthorizationServerPresent = ClassUtils
                .isPresent("org.springframework.security.oauth2.server.authorization.OAuth2Authorization", classLoader);
        ldapJacksonPresent = ClassUtils.isPresent(ldapJacksonModuleClass, classLoader);
        saml2JacksonPresent = ClassUtils.isPresent(saml2JacksonModuleClass, classLoader);
        casJacksonPresent = ClassUtils.isPresent(casJacksonModuleClass, classLoader);
        herodotusSecurityJacksonPresent = ClassUtils.isPresent(herodotusSecurityJacksonModuleClass, classLoader);
        herodotusOAuth2JacksonPresent = ClassUtils.isPresent(herodotusOAuth2JacksonModuleClass, classLoader);
    }

    private HerodotusSecurityJacksonModules() {
    }

    @SuppressWarnings("unchecked")
    private static @Nullable SecurityJacksonModule loadAndGetInstance(String className, ClassLoader loader) {
        try {
            Class<? extends SecurityJacksonModule> securityModule = (Class<? extends SecurityJacksonModule>) ClassUtils
                    .forName(className, loader);
            log.debug("Loaded module {}, now registering", className);
            return securityModule.getConstructor().newInstance();
        } catch (Exception ex) {
            log.debug("Cannot load module {}", className, ex);
        }
        return null;
    }

    /**
     * Return the list of available security modules in classpath, enable automatic
     * inclusion of type information and configure a default
     * {@link PolymorphicTypeValidator} that handles the validation of class names.
     *
     * @param loader the ClassLoader to use
     * @return List of available security modules in classpath
     * @see #getModules(ClassLoader, BasicPolymorphicTypeValidator.Builder)
     */
    public static List<JacksonModule> getModules(ClassLoader loader) {
        return getModules(loader, null);
    }

    /**
     * Return the list of available security modules in classpath, enable automatic
     * inclusion of type information and configure a default
     * {@link PolymorphicTypeValidator} customizable with the provided builder that
     * handles the validation of class names.
     *
     * @param loader               the ClassLoader to use
     * @param typeValidatorBuilder the builder to configure custom types allowed in
     *                             addition to Spring Security ones
     * @return List of available security modules in classpath.
     */
    public static List<JacksonModule> getModules(ClassLoader loader,
                                                 BasicPolymorphicTypeValidator.@Nullable Builder typeValidatorBuilder) {

        List<JacksonModule> modules = new ArrayList<>();
        for (String className : securityJacksonModuleClasses) {
            addToModulesList(loader, modules, className);
        }
        if (webServletPresent) {
            addToModulesList(loader, modules, webServletJacksonModuleClass);
        }
        if (oauth2ClientPresent) {
            addToModulesList(loader, modules, oauth2ClientJacksonModuleClass);
        }
        if (oauth2AuthorizationServerPresent) {
            addToModulesList(loader, modules, oauth2AuthorizationServerJacksonModuleClass);
        }
        if (ldapJacksonPresent) {
            addToModulesList(loader, modules, ldapJacksonModuleClass);
        }
        if (saml2JacksonPresent) {
            addToModulesList(loader, modules, saml2JacksonModuleClass);
        }
        if (casJacksonPresent) {
            addToModulesList(loader, modules, casJacksonModuleClass);
        }

        if (herodotusSecurityJacksonPresent) {
            addToModulesList(loader, modules, herodotusSecurityJacksonModuleClass);
        }

        if (herodotusOAuth2JacksonPresent) {
            addToModulesList(loader, modules, herodotusOAuth2JacksonModuleClass);
        }
        applyPolymorphicTypeValidator(modules, typeValidatorBuilder);
        return modules;
    }

    private static void applyPolymorphicTypeValidator(List<JacksonModule> modules, BasicPolymorphicTypeValidator.@Nullable Builder typeValidatorBuilder) {

        BasicPolymorphicTypeValidator.Builder builder = (typeValidatorBuilder != null) ? typeValidatorBuilder
                : BasicPolymorphicTypeValidator.builder();
        for (JacksonModule module : modules) {
            if (module instanceof SecurityJacksonModule securityModule) {
                securityModule.configurePolymorphicTypeValidator(builder);
            }
        }
        modules.add(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) {
                ((MapperBuilder<?, ?>) context.getOwner()).activateDefaultTyping(builder.build(),
                        DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
            }
        });
    }

    /**
     * @param loader    the ClassLoader to use
     * @param modules   list of the modules to add
     * @param className name of the class to instantiate
     */
    private static void addToModulesList(ClassLoader loader, List<JacksonModule> modules, String className) {
        SecurityJacksonModule module = loadAndGetInstance(className, loader);
        if (module != null) {
            modules.add(module);
        }
    }

}
