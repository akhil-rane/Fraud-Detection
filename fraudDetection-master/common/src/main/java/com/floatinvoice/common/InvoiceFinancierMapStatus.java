package com.floatinvoice.common;

public enum InvoiceFinancierMapStatus {

	LINKED(1, "Linked"),
	UNLINKED(2, "Unlinked");
	
	private int code;
	private String status;
	
	InvoiceFinancierMapStatus() {
	}
	
	InvoiceFinancierMapStatus(int code, String status) {
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
