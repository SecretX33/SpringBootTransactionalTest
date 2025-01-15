package com.secretx33.springboottransactionaltest.configuration

import com.zaxxer.hikari.HikariDataSource
import liquibase.integration.spring.SpringLiquibase
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@PropertySources(PropertySource("classpath:transactional-test-db.properties"))
@EnableJpaRepositories(
    basePackages = ["com.secretx33.springboottransactionaltest.repository"],
    entityManagerFactoryRef = "transactionalTestEntityManagerFactory",
    transactionManagerRef = "transactionalTestTransactionManager",
)
class TransactionalTestDatabaseConfig {

    @Bean
    @ConfigurationProperties("app.transactional-test-datasource")
    fun transactionalTestDataSourceProperties(): DataSourceProperties = DataSourceProperties()

    @Bean
    fun transactionalTestDataSource(): DataSource = transactionalTestDataSourceProperties()
        .initializeDataSourceBuilder()
        .type(HikariDataSource::class.java)
        .build()

    @Bean
    fun transactionalTestLiquibase(): SpringLiquibase = SpringLiquibase().apply {
        dataSource = transactionalTestDataSource()
        changeLog = "classpath:db/changelog/changelog-master.yaml"
        databaseChangeLogTable = "transactional_test_databasechangelog"
        databaseChangeLogLockTable = "transactional_test_databasechangeloglock"
        contexts = "transactional-test"
        defaultSchema = "public"
        labelFilter = "transactional-test"
    }

    @Bean
    @ConfigurationProperties("spring.transactional-test-jpa.properties")
    @Qualifier("transactionalTestEntityManagerFactory")
    fun transactionalTestEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
    ): LocalContainerEntityManagerFactoryBean = builder
        .dataSource(transactionalTestDataSource())
        .packages("com.secretx33.springboottransactionaltest.dao")
        .persistenceUnit("transactional-test-persistence-unit")
        .build()

    @Bean
    fun transactionalTestTransactionManager(
        @Qualifier("transactionalTestEntityManagerFactory") transactionalTestEntityManagerFactory: LocalContainerEntityManagerFactoryBean,
    ): PlatformTransactionManager = JpaTransactionManager(transactionalTestEntityManagerFactory.`object`!!)
}
