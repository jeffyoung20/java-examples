package com.jeffyoung20.testapp.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DatabaseConfig {
	@Value("${db.driverClassName:com.mysql.cj.jdbc.Driver}") //com.mysql.cj.jdbc.Driver
    private String driverClassName;
	
	@Value("${db.url:jdbc:mysql://127.0.0.1:3306/organization-db-spring}") 
    private String url;
	
	@Value("${db.username:root}") 
    private String username;

	@Value("${db.password:password}") 
    private String password;

    @Bean
    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);

        return dataSource;
    }
    
//    @Bean
//    public JdbcTemplate getJdbcTemplate() {
//      return new JdbcTemplate(mysqlDataSource());
//    }
}
