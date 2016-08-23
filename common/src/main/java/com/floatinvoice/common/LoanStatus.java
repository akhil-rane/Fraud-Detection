package com.floatinvoice.common;

public enum LoanStatus {

	DEFAULT(0, "DEFAULT"),
	ACTIVE(1, "ACTIVE"),
	PAID(2, "PAID");
	
	int code;
	String text;
	
	LoanStatus(int code, String text){
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
	
	 public static LoanStatus fromCode(int x) {
	      switch(x) {
	      	case 0: 
	    	  return DEFAULT;
	        case 1:
	            return ACTIVE;
	        case 2:
	            return PAID;
	      }
	      return null;
	}
}
