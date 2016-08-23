package com.floatinvoice.business.dao;

public class InvoicePool {

	int tmpPoolId;	
	InvoicePoolKey invoicePoolKey;
	double invoicePoolAmount;
	
	public InvoicePool(){
		
	}
	
	public InvoicePool(int tmpPoolId, InvoicePoolKey invoicePoolKey){
		this.tmpPoolId = tmpPoolId;
		this.invoicePoolKey = invoicePoolKey;
	}

	public int getTmpPoolId() {
		return tmpPoolId;
	}

	public void setTmpPoolId(int tmpPoolId) {
		this.tmpPoolId = tmpPoolId;
	}

	public InvoicePoolKey getInvoicePoolKey() {
		return invoicePoolKey;
	}

	public void setInvoicePoolKey(InvoicePoolKey invoicePoolKey) {
		this.invoicePoolKey = invoicePoolKey;
	}
	
	public double getInvoicePoolAmount() {
		return invoicePoolAmount;
	}

	public void setInvoicePoolAmount(double invoicePoolAmount) {
		this.invoicePoolAmount = invoicePoolAmount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((invoicePoolKey == null) ? 0 : invoicePoolKey.hashCode());
		result = prime * result + tmpPoolId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof InvoicePool)) {
			return false;
		}
		InvoicePool other = (InvoicePool) obj;
		if (invoicePoolKey == null) {
			if (other.invoicePoolKey != null) {
				return false;
			}
		} else if (!invoicePoolKey.equals(other.invoicePoolKey)) {
			return false;
		}
		if (tmpPoolId != other.tmpPoolId) {
			return false;
		}
		return true;
	}
	
	
}
