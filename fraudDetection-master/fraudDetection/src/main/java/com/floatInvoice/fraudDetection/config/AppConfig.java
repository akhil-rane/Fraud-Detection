package com.floatInvoice.fraudDetection.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.floatInvoice.fraudDetection.FraudDetection;

@Configuration
public class AppConfig {

	@Bean
	FraudDetection fraudDetection(){
		return new FraudDetection();
	}
}
