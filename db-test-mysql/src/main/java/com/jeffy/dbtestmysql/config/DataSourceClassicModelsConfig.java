package com.jeffy.dbtestmysql.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceClassicModelsConfig {
	@Autowired
	@Qualifier("connInfoClassicModels")
	ConnectionInfo connnectionInfo;
	
	@Primary
	@Bean(name="dataSourceClassicModels")
	public DataSource dataSourceClassicModels() {
	    DriverManagerDataSource dataSource = new DriverManagerDataSource();

	    //dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
	    dataSource.setUsername(connnectionInfo.getUsername());
	    dataSource.setPassword(connnectionInfo.getPassword());
	    dataSource.setUrl(connnectionInfo.getUrl()); 
	    return dataSource;
	}

}
