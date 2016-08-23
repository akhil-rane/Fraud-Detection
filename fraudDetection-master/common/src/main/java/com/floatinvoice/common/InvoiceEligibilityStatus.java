package com.floatinvoice.common;

public enum InvoiceEligibilityStatus {

	UPLOADED(-1, "Uploaded Invoice"),
	FRAUD(-2, "Fraud Invoice"),
	PENDING_APPROVAL(2, "Pending Approval"),
	ELIGIBLE(0, "Eligible Invoice"),
	REJECTED(3, "Rejected Invoice"),
	PROCESSED(1, "Processed Invoice");
	
	int code;
	String text;
	
	InvoiceEligibilityStatus(int code,	String text){
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
