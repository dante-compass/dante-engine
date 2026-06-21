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

package cn.herodotus.dante.data.tenant.autoconfigure.hibernate;

import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.hibernate.autoconfigure.HibernateProperties;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.boot.hibernate.autoconfigure.HibernateSettings;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.SchemaManagement;
import org.springframework.boot.jdbc.SchemaManagementProvider;
import org.springframework.boot.jpa.autoconfigure.JpaProperties;
import org.springframework.orm.jpa.hibernate.SpringBeanContainer;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

/**
 * <p>Description: HibernateProperties 提供者 </p>
 * <p>
 * 参考 {@code HibernateJpaConfiguration} 的配置方式，解决 Database 多租户模式下，定义 {@code LocalContainerEntityManagerFactoryBean} Bean 所需 Hibernate Properties 的生成
 *
 * @author : gengwei.zheng
 * @date : 2024/5/2 9:50
 */
public class HerodotusHibernatePropertiesProvider {

    private final DataSource dataSource;

    private final HibernateProperties hibernateProperties;
    private final JpaProperties jpaProperties;

    private final HibernateDefaultDdlAutoProvider defaultDdlAutoProvider;

    private final List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers;

    public HerodotusHibernatePropertiesProvider(DataSource dataSource,
                                                HibernateProperties hibernateProperties,
                                                JpaProperties jpaProperties,
                                                ConfigurableListableBeanFactory beanFactory,
                                                ObjectProvider<SchemaManagementProvider> providers,
                                                ObjectProvider<PhysicalNamingStrategy> physicalNamingStrategy,
                                                ObjectProvider<ImplicitNamingStrategy> implicitNamingStrategy,
                                                ObjectProvider<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers) {
        this.dataSource = dataSource;
        this.hibernateProperties = hibernateProperties;
        this.jpaProperties = jpaProperties;
        this.defaultDdlAutoProvider = new HibernateDefaultDdlAutoProvider(providers);
        this.hibernatePropertiesCustomizers = determineHibernatePropertiesCustomizers(
                physicalNamingStrategy.getIfAvailable(), implicitNamingStrategy.getIfAvailable(), beanFactory,
                hibernatePropertiesCustomizers.orderedStream().toList());
    }

    public Map<String, Object> getVendorProperties() {
        Supplier<String> defaultDdlMode = () -> this.defaultDdlAutoProvider.getDefaultDdlAuto(dataSource);
        return new LinkedHashMap<>(this.hibernateProperties.determineHibernateProperties(
                jpaProperties.getProperties(), new HibernateSettings().ddlAuto(defaultDdlMode)
                        .hibernatePropertiesCustomizers(this.hibernatePropertiesCustomizers)));
    }

    private List<HibernatePropertiesCustomizer> determineHibernatePropertiesCustomizers(
            PhysicalNamingStrategy physicalNamingStrategy, ImplicitNamingStrategy implicitNamingStrategy,
            ConfigurableListableBeanFactory beanFactory,
            List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers) {
        List<HibernatePropertiesCustomizer> customizers = new ArrayList<>();
        if (ClassUtils.isPresent("org.hibernate.resource.beans.container.spi.BeanContainer",
                getClass().getClassLoader())) {
            customizers.add((properties) -> properties.put(AvailableSettings.BEAN_CONTAINER,
                    new SpringBeanContainer(beanFactory)));
        }
        if (physicalNamingStrategy != null || implicitNamingStrategy != null) {
            customizers
                    .add(new NamingStrategiesHibernatePropertiesCustomizer(physicalNamingStrategy, implicitNamingStrategy));
        }
        customizers.addAll(hibernatePropertiesCustomizers);
        return customizers;
    }

    private static class HibernateDefaultDdlAutoProvider implements SchemaManagementProvider {
        private final Iterable<SchemaManagementProvider> providers;

        HibernateDefaultDdlAutoProvider(Iterable<SchemaManagementProvider> providers) {
            this.providers = providers;
        }

        String getDefaultDdlAuto(DataSource dataSource) {
            if (!EmbeddedDatabaseConnection.isEmbedded(dataSource)) {
                return "none";
            }
            SchemaManagement schemaManagement = getSchemaManagement(dataSource);
            if (SchemaManagement.MANAGED.equals(schemaManagement)) {
                return "none";
            }
            return "create-drop";
        }

        @Override
        public SchemaManagement getSchemaManagement(DataSource dataSource) {
            return StreamSupport.stream(this.providers.spliterator(), false)
                    .map((provider) -> provider.getSchemaManagement(dataSource))
                    .filter(SchemaManagement.MANAGED::equals)
                    .findFirst()
                    .orElse(SchemaManagement.UNMANAGED);
        }
    }

    private static class NamingStrategiesHibernatePropertiesCustomizer implements HibernatePropertiesCustomizer {

        private final PhysicalNamingStrategy physicalNamingStrategy;

        private final ImplicitNamingStrategy implicitNamingStrategy;

        NamingStrategiesHibernatePropertiesCustomizer(PhysicalNamingStrategy physicalNamingStrategy,
                                                      ImplicitNamingStrategy implicitNamingStrategy) {
            this.physicalNamingStrategy = physicalNamingStrategy;
            this.implicitNamingStrategy = implicitNamingStrategy;
        }

        @Override
        public void customize(Map<String, Object> hibernateProperties) {
            if (this.physicalNamingStrategy != null) {
                hibernateProperties.put("hibernate.physical_naming_strategy", this.physicalNamingStrategy);
            }
            if (this.implicitNamingStrategy != null) {
                hibernateProperties.put("hibernate.implicit_naming_strategy", this.implicitNamingStrategy);
            }
        }

    }
}
