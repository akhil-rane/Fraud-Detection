package com.floatinvoice.common;

public enum OrgType {
	
	SELLER(1, "SELLER"),
	BANK(2, "BANK"),
	BUYER(3, "BUYER"),
	ADMIN(4, "ADMIN");

	private int code;
	private String text;
	
	private OrgType(int code, String text){
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
	
}
