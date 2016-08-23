package com.floatInvoice.fraudDetection;

public class SameSameDifferentKey extends BaseFraudDetectionKey {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String invoiceNo;

	private String invoicePONo;

	private double amount;
	
	private int buyerId;

	public SameSameDifferentKey(int supplierId, String invoiceNo,
			String invoicePONo, double amount, int buyerId) {
		super(supplierId);
		this.invoiceNo = invoiceNo;
		this.invoicePONo = invoicePONo;
		this.amount = amount;
		this.buyerId = buyerId;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoicePONo() {
		return invoicePONo;
	}

	public void setInvoicePONo(String invoicePONo) {
		this.invoicePONo = invoicePONo;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
		
	public int getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(int buyerId) {
		this.buyerId = buyerId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((invoiceNo == null) ? 0 : invoiceNo.hashCode());
		result = prime * result
				+ ((invoicePONo == null) ? 0 : invoicePONo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SameSameDifferentKey other = (SameSameDifferentKey) obj;
		if (Double.doubleToLongBits(amount) != Double
				.doubleToLongBits(other.amount))
			return false;
		if (invoiceNo == null) {
			if (other.invoiceNo != null)
				return false;
		} else if (!invoiceNo.equals(other.invoiceNo))
			return false;
		if (invoicePONo == null) {
			if (other.invoicePONo != null)
				return false;
		} else if (!invoicePONo.equals(other.invoicePONo))
			return false;
		if(buyerId==other.buyerId)
			return false;
		return true;
	}

	

	
}
