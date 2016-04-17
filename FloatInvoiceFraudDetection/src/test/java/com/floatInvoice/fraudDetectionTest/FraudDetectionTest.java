package com.floatInvoice.fraudDetectionTest;

import com.floatInvoice.fraudDetection.FraudDetection;
import com.floatInvoice.fraudDetection.Result;

public class FraudDetectionTest {
	
	public static void main(String[] args) {
		
		String path="D:\\dataset1.csv";
		FraudDetection fraudDetection=new FraudDetection();
		Result result = fraudDetection.getResults(path);
		System.out.println("----------------------------------------------------------------------------------------------------");
		System.out.println("OUTPUT:");
		System.out.println(result);
		System.out.println("----------------------------------------------------------------------------------------------------");
		
	}
	
}
