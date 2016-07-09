package com.floatinvoice.common;

public enum PaymentStatus {

	UNPAID(0, "UNPAID"),
	QUEUED(1, "QUEUED"),
	PAID(2, "PAID");
	
	int code;
	String text;
	
	PaymentStatus( int code, String text){
		this.code = code;
		this.text = text;
	}
	
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	 public static PaymentStatus fromCode(int x) {
	      switch(x) {
	      	case 0: 
	    	  return UNPAID;
	        case 1:
	            return QUEUED;
	        case 2:
	            return PAID;
	      }
	      return null;
	}
}
