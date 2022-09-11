package com.jeffy.multipledbs.config;



import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


@Configuration
public class ConnectionInfoConfigs {
	
	@Bean(name="connInfoBaseBall")
	@ConfigurationProperties(prefix = "db.baseball.datasource")
	public ConnectionInfo connInfoBaseBall() {
		return new ConnectionInfo();
	}

	@Bean(name="connInfoClassicModels")
	@ConfigurationProperties(prefix = "db.classicmodels.datasource")
	public ConnectionInfo connInfoClassicModels() {
		return new ConnectionInfo();
	}
}
