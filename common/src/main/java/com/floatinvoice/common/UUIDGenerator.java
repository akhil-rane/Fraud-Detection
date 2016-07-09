package com.floatinvoice.common;

import java.util.UUID;

public final class UUIDGenerator {
	
	private UUIDGenerator(){
		
	}
	
	public static String newRefId(){
		
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}

}
