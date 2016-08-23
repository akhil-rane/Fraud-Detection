package com.floatinvoice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.floatinvoice.business.listeners.FileProcessor;
import com.floatinvoice.business.listeners.InvoicePoolNotifier;
import com.floatinvoice.business.listeners.InvoicePoolProcessor;

@Configuration
@EnableScheduling
public class ListenerSchedulingConfig {

	@Autowired
	ReadServicesConfig readServicesConfig;
	
	
	@Bean
	public FileProcessor fileProcessor(){
		return new FileProcessor(readServicesConfig.invoiceFileUploadDao());
	}
	
	@Bean
	public InvoicePoolProcessor invoicePoolProcessor(){
		return new InvoicePoolProcessor(readServicesConfig.invoiceInfoReadDao());
	}

	@Bean
	public InvoicePoolNotifier invoicePoolNotifier(){
		return new InvoicePoolNotifier(readServicesConfig.invoiceInfoReadDao());
	}
}
