package com.floatinvoice.common;

public enum InvoicePoolStatus {

	INCOMPLETE(0, "INCOMPLETE"),
	COMPLETE(1, "COMPLETE"),
	FUNDED(2, "FUNDED");
	
	private int code;
	private String status;
	
	private InvoicePoolStatus(int code, String status){
		this.code = code;
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
