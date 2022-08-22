package com.jeffy.dbtestmysql.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceConfig {
	@Autowired
	AppConfig config;
	
	@Bean
	public DataSource dataSource() {
	    DriverManagerDataSource dataSource = new DriverManagerDataSource();

	    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
	    dataSource.setUsername(config.getUsername());
	    dataSource.setPassword(config.getPassword());
	    dataSource.setUrl(config.getUrl()); 
	    return dataSource;
	}

}
