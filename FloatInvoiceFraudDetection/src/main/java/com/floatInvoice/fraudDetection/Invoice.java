package com.floatInvoice.fraudDetection;

import java.util.Date;



public class Invoice {
	
	private String invoiceID;
	private Date invoiceDate;
	private Date paymentDueDate;
	private String buyerID;
	private String sellerID;
	private double invoiceAmount;
	private String purchaseOrderID;
	
	
	@Override
	public String toString() {
		return "Invoice [invoiceID=" + invoiceID + ", invoiceDate="
				+ invoiceDate + ", paymentDueDate=" + paymentDueDate
				+ ", buyerID=" + buyerID + ", sellerID=" + sellerID
				+ ", invoiceAmount=" + invoiceAmount + ", purchaseOrderID="
				+ purchaseOrderID + "]";
	}
	public String getInvoiceID() {
		return invoiceID;
	}
	public void setInvoiceID(String invoiceID) {
		this.invoiceID = invoiceID;
	}
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public Date getPaymentDueDate() {
		return paymentDueDate;
	}
	public void setPaymentDueDate(Date paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}
	public String getBuyerID() {
		return buyerID;
	}
	public void setBuyerID(String buyerID) {
		this.buyerID = buyerID;
	}
	public String getSellerID() {
		return sellerID;
	}
	public void setSellerID(String sellerID) {
		this.sellerID = sellerID;
	}
	public double getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public String getPurchaseOrderID() {
		return purchaseOrderID;
	}
	public void setPurchaseOrderID(String purchaseOrderID) {
		this.purchaseOrderID = purchaseOrderID;
	}
	
	
}



