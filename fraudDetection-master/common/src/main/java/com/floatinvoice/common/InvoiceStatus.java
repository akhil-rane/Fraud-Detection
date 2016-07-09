package com.floatinvoice.common;

public enum InvoiceStatus {

	UNPOOLED(0, "UNPOOLED"),
	ACTIVE(1, "ACTIVE"),
	INACTIVE(2, "INACTIVE"),
	ACCEPTED(3, "ACCEPTED"),
	SANCTIONED(4, "SANCTIONED"),
	PAID(5, "PAID"),
	POOLED(6, "POOLED");
	
	private int code;
	private String status;
	
	private InvoiceStatus(int code, String status){
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
