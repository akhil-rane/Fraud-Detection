package com.floatInvoice.fraudDetection;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.floatInvoice.fraudDetection.config.AppConfig;
import com.floatInvoice.fraudDetection.config.FraudDetectionDataSourceConfig;
import com.floatInvoice.fraudDetection.config.ListenerSchedulingConfig;

public class FraudDetectionLauncher {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		try {
			ctx.register(FraudDetectionDataSourceConfig.class,
					ListenerSchedulingConfig.class/*,
					AppConfig.class*/);
			ctx.refresh();
			ctx.start();
		} catch (Exception e) {
			e.printStackTrace();
			ctx.close();
		} 
	}

}
