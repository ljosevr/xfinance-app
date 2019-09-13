package com.gigti.xfinance.ui;

import com.vaadin.flow.server.VaadinServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
@ComponentScan({"com.gigti.xfinance"})
@EnableJpaRepositories(basePackages = "com.gigti.xfinance.backend.repositories")
@EntityScan(basePackages = "com.gigti.xfinance.backend.data")
//public class Application extends SpringBootServletInitializer {
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
