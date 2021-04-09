package com.gigti.xfinance.ui;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
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

    // @Bean
    // public HibernateTransactionManager transactionManager(SessionFactory sf) {
    //     return new HibernateTransactionManager(sf);
    // }

    // @Bean
    // public LocalSessionFactoryBean  sessionFactory(DataSource ds) throws ClassNotFoundException {
    //     LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
    //     localSessionFactoryBean.setDataSource(dataSource());
    //     return localSessionFactoryBean;
    // }
    // @Bean
    // public DataSource dataSource() throws ClassNotFoundException {
    //     SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
    //     dataSource.setDriverClass((Class<? extends Driver>)Class.forName("com.mysql.jdbc.Driver"));
        
    //     return  dataSource;
    // }
}
