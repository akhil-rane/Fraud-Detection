package com.floatinvoice.business.dao;

import java.util.Comparator;
import java.util.Date;

public class InvoicePoolMapping {

	private int invoiceId;
	private int supplierId;
	private int buyerId;
	private double amount;
	private Date endDt;
	private int poolId;
	private double poolAmount;
	private int poolStatus;

	public int getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}
	public int getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}
	public int getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(int buyerId) {
		this.buyerId = buyerId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Date getEndDt() {
		return endDt;
	}
	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}
		
	public int getPoolId() {
		return poolId;
	}
	
	public void setPoolId(int poolId) {
		this.poolId = poolId;
	}
		
	public double getPoolAmount() {
		return poolAmount;
	}
	
	public void setPoolAmount(double poolAmount) {
		this.poolAmount = poolAmount;
	}
	
	
	public int getPoolStatus() {
		return poolStatus;
	}
	public void setPoolStatus(int poolStatus) {
		this.poolStatus = poolStatus;
	}




	static class InvoiceDueDateComparator implements Comparator<InvoicePoolMapping>{

		@Override
		public int compare(InvoicePoolMapping o1, InvoicePoolMapping o2) {
			return o1.getEndDt().compareTo(o2.getEndDt());
		}
		
	}
}
