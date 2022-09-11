package com.jeffy.multipledbs.baseball.config;

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
  basePackages = {"com.jeffy.multipledbs.baseball.services"},
  entityManagerFactoryRef = "baseballEntityManagerFactory",
  transactionManagerRef = "baseballTransactionManager"
)
public class JpaConfigBaseball {
    @Bean
    public LocalContainerEntityManagerFactoryBean baseballEntityManagerFactory(
      @Qualifier("dataSourceBaseball") DataSource dataSource ,
      EntityManagerFactoryBuilder builder) {
        return builder
          .dataSource(dataSource)
          .packages("com.jeffy.multipledbs.baseball.models")
          .build();
    }

    /*
    @Bean
    public LocalContainerEntityManagerFactoryBean baseballEntityManagerFactory(
      @Qualifier("dataSourceBaseball") DataSource dataSource,
      EntityManagerFactoryBuilder builder) {
    	return null;
    }*/

    @Bean
    public PlatformTransactionManager baseballTransactionManager(
      @Qualifier("baseballEntityManagerFactory") LocalContainerEntityManagerFactoryBean baseballEntityManagerFactory) {
        return new JpaTransactionManager(baseballEntityManagerFactory.getObject());
    }
}
