/******************************************************************************
 *
 *  (C) 2024 thmoon. All rights reserved.
 *  Any part of this source code can not be copied with any method without
 *  prior written permission from the author or authorized person.
 *
 ******************************************************************************/

package com.moais.todo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableJpaRepositories(
        basePackages = "com.moais.todo.datasource.todolist",
        entityManagerFactoryRef = "todoEntityManager",
        transactionManagerRef = "todoTransactionManager"
)
public class TodoDataSourceConfig {

    @Value("${spring.datasource.todolist.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.todolist.jdbc-url}")
    private String todoJdbcUrl;

    @Value("${spring.datasource.todolist.username}")
    private String todoUserName;

    @Value("${spring.datasource.todolist.password}")
    private String todoPassword;

    @Value("${spring.datasource.hikari.connection-timeout}")
    private Long connectionTimeout;

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private int maxPoolSize;

    @Value("${spring.datasource.hikari.max-lifetime}")
    private Long maxLifeTime;

    @Value("${spring.datasource.hikari.idle-timeout}")
    private Long idleTimeout;

    @Bean
    @Primary
    public PlatformTransactionManager todoTransactionManager(){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(todoEntityManager().getObject());
        return transactionManager;
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean todoEntityManager(){
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        HikariDataSource dataSource = todoHikariDataSource();

        em.setDataSource(dataSource);
        em.setPackagesToScan("com.moais.todo.datasource.todolist");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        return em;
    }

    @Bean
    @Primary
    public HikariDataSource todoHikariDataSource(){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(todoJdbcUrl);
        dataSource.setUsername(todoUserName);
        dataSource.setPassword(todoPassword);
        dataSource.setConnectionTimeout(connectionTimeout);
        dataSource.setMaximumPoolSize(maxPoolSize);
        dataSource.setMaxLifetime(maxLifeTime);
        dataSource.setIdleTimeout(idleTimeout);
        return dataSource;
    }
}
