package com.floatinvoice.common;

public enum BankAccountType {

	SAVINGS(1, "SAVINGS"),
	CHECKING(2, "CHECKING");
	
	int code;
	String text;
	
	BankAccountType(int code, String text){
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
	
	 public static BankAccountType fromCode(int x) {
	      switch(x) {
	        case 1:
	            return SAVINGS;
	        case 2:
	            return CHECKING;
	      }
	      return null;
	}
}
