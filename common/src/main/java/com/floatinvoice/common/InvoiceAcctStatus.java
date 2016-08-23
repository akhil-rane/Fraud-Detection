package com.floatinvoice.common;

public enum InvoiceAcctStatus {

	INACTIVE(0, "INACTIVE"),
	ACTIVE(1, "ACTIVE"),
	CLOSED(2, "CLOSED");
	
	int code;
	String text;
	
	InvoiceAcctStatus(int code, String text){
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
	
	 public static InvoiceAcctStatus fromCode(int x) {
	      switch(x) {
	      	case 0: 
	    	  return INACTIVE;
	        case 1:
	            return ACTIVE;
	        case 2:
	            return CLOSED;
	      }
	      return null;
	}
}
