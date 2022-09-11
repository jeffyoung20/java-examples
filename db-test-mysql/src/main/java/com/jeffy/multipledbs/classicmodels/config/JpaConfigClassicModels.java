package com.jeffy.multipledbs.classicmodels.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
  basePackages = {"com.jeffy.multipledbs.classicmodels.services"},
  entityManagerFactoryRef = "classicmodelsEntityManagerFactory",
  transactionManagerRef = "classicmodelsTransactionManager"
)
public class JpaConfigClassicModels {
    @Bean
    public LocalContainerEntityManagerFactoryBean classicmodelsEntityManagerFactory(
      @Qualifier("dataSourceClassicModels") DataSource dataSource ,
      EntityManagerFactoryBuilder builder) {
        return builder
          .dataSource(dataSource)
          .packages("com.jeffy.multipledbs.classicmodels.models")
          .build();
    }

    @Bean
    public PlatformTransactionManager classicmodelsTransactionManager(
      @Qualifier("classicmodelsEntityManagerFactory") LocalContainerEntityManagerFactoryBean classicmodelsEntityManagerFactory) {
        return new JpaTransactionManager(classicmodelsEntityManagerFactory.getObject());
    }
}
