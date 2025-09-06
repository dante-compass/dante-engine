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

package cn.herodotus.engine.data.tenant.configuration;

import cn.herodotus.engine.data.tenant.annotation.ConditionalOnDatabaseApproach;
import cn.herodotus.engine.data.tenant.datasource.MultiTenantDataSourceFactory;
import cn.herodotus.engine.data.tenant.hibernate.DatabaseMultiTenantConnectionProvider;
import cn.herodotus.engine.data.tenant.hibernate.HerodotusHibernatePropertiesProvider;
import cn.herodotus.engine.data.tenant.properties.MultiTenantProperties;
import jakarta.annotation.PostConstruct;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jdbc.SchemaManagementProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

/**
 * <p>Description: 独立数据库多租户方式配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/3/28 23:37
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnDatabaseApproach
@EnableTransactionManagement
@EntityScan(basePackages = {
        "cn.herodotus.engine.data.tenant.entity",
})
@EnableJpaRepositories(basePackages = {
        "cn.herodotus.engine.data.tenant.repository",
})
public class DatabaseApproachConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DatabaseApproachConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- Module [Database Approach] Auto Configure.");
    }

    @Bean
    public HibernatePropertiesCustomizer databaseMultiTenantConnectionProvider(DataSource dataSource) {
        DatabaseMultiTenantConnectionProvider provider = new DatabaseMultiTenantConnectionProvider(dataSource);
        log.debug("[Herodotus] |- Bean [Multi Tenant Connection Provider] Auto Configure.");
        return provider;
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, HibernateProperties hibernateProperties,
                                                                       JpaProperties jpaProperties, JpaVendorAdapter jpaVendorAdapter,
                                                                       ConfigurableListableBeanFactory beanFactory,
                                                                       ObjectProvider<SchemaManagementProvider> providers,
                                                                       ObjectProvider<PhysicalNamingStrategy> physicalNamingStrategy,
                                                                       ObjectProvider<ImplicitNamingStrategy> implicitNamingStrategy,
                                                                       ObjectProvider<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers,
                                                                       MultiTenantProperties multiTenantProperties
    ) {

        HerodotusHibernatePropertiesProvider provider = new HerodotusHibernatePropertiesProvider(dataSource, hibernateProperties, jpaProperties, beanFactory, providers, physicalNamingStrategy, implicitNamingStrategy, hibernatePropertiesCustomizers);
        Map<String, Object> properties = provider.getVendorProperties();

        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        //此处不能省略，哪怕你使用了 @EntityScan，实际上 @EntityScan 会失效
        entityManagerFactory.setPackagesToScan(multiTenantProperties.getPackageToScan());
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactory.setJpaPropertyMap(properties);
        return entityManagerFactory;
    }

    @Primary
    @Bean
    @ConditionalOnClass({LocalContainerEntityManagerFactoryBean.class})
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactory.getObject()));
    }

    @Bean
    @ConditionalOnClass({LocalContainerEntityManagerFactoryBean.class})
    public MultiTenantDataSourceFactory multiTenantDataSourceFactory() {
        MultiTenantDataSourceFactory multiTenantDataSourceFactory = new MultiTenantDataSourceFactory();
        log.debug("[Herodotus] |- Bean [Multi Tenant DataSource Factory] Auto Configure.");
        return multiTenantDataSourceFactory;
    }
}
