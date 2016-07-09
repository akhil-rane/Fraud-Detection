package com.floatInvoice.fraudDetection.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.floatInvoice.fraudDetection.FraudDetectionPollster;
import com.floatInvoice.fraudDetection.jdbc.JdbcDispatcher;



@Configuration
@EnableScheduling
public class ListenerSchedulingConfig {

	@Autowired
	private FraudDetectionDataSourceConfig dsConfig;
	
	@Bean
	public FraudDetectionPollster fraudDetectionPollster(){
		return new FraudDetectionPollster(jdbcDispatcher());
	}
	
	@Bean
	public JdbcDispatcher jdbcDispatcher(){
		return new JdbcDispatcher(new NamedParameterJdbcTemplate(dsConfig.dataSource()));
		
	}
	
}

