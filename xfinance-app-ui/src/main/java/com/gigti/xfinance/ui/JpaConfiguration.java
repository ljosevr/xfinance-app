package com.gigti.xfinance.ui;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The entry point of the Spring Boot application.
 */
// @Configuration
// @EnableJpaRepositories(basePackages = "com.gigti.xfinance.backend")
// @EntityScan(basePackages = "com.gigti.xfinance.backend")
// @EnableTransactionManagement
public class JpaConfiguration {

//     @Bean 
//     public DataSource getDataSource() { 
//         DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create(); 
//         dataSourceBuilder.username("tis"); 
//         dataSourceBuilder.password("tis12020");
//         return dataSourceBuilder.build(); 
//     }
}
