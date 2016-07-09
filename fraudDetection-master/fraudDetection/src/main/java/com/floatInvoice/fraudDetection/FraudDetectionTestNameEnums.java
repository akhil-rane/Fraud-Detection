package com.floatInvoice.fraudDetection;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum FraudDetectionTestNameEnums {

	DifferentRequestIP(1, "DIFFERENT_REQUEST_IP"),
	Benfords(2, "BENFORDS_LAW"),
	PurchaseOrder(3, "PURCHASE_ORDER_MISSING"),
	RoundedAmts(4, "ROUNDED_AMOUNTS"),
	EvenAmts(5, "EVEN_AMOUNTS"),
	LatePayments(6, "LATE_PAYMENTS"),
	WeekendPayments(7, "WEEKEND_PAYMENTS"),
	SSS(8, "SAME_SAME_SAME"),
	SSD(8, "SAME_SAME_DIFFERENT");
	
	
	 private static final Map<Integer,FraudDetectionTestNameEnums> lookup = new HashMap<>();

	static {
	     for(FraudDetectionTestNameEnums s : EnumSet.allOf(FraudDetectionTestNameEnums.class))
	          lookup.put(s.getCode(), s);
	}
	
	public static FraudDetectionTestNameEnums get(int code) { 
	      return lookup.get(code); 
	}
	
	
	int code;
	String name;
	
	FraudDetectionTestNameEnums(int code, String name){
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
