package com.jeffy.multipledbs.baseball.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.jeffy.multipledbs.config.ConnectionInfo;

@Configuration
public class DataSourceBaseballConfig {
	@Autowired
	@Qualifier("connInfoBaseBall")
	ConnectionInfo connnectionInfo;
	
	@Bean(name="dataSourceBaseball")
	//@Qualifier("dataSourceBaseball")
	public DataSource dataSourceBaseball() {
	    DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setUsername(connnectionInfo.getUsername());
	    dataSource.setPassword(connnectionInfo.getPassword());
	    dataSource.setUrl(connnectionInfo.getUrl()); 
	    return dataSource;
	}

}
