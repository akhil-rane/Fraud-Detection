package com.floatinvoice.common;

public enum RegistrationStatusEnum {

	LOGIN(1, "Login"),
	ORG(2, "OrgInfo"),
	USER(3, "UserInfo"),
	HOME(4, "Home");
	
	int code;
	String text;
	
	private RegistrationStatusEnum(int code,
						String text) {
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
