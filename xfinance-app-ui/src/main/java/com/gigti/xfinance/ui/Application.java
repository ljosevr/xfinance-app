package com.gigti.xfinance.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
@ComponentScan({"com.gigti.xfinance"})
@EnableJpaRepositories(basePackages = "com.gigti.xfinance.backend.repositories")
@EntityScan(basePackages = "com.gigti.xfinance.backend.data")
@EnableTransactionManagement
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
