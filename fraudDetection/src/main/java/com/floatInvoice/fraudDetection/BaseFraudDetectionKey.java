package com.floatInvoice.fraudDetection;

import java.io.Serializable;

public class BaseFraudDetectionKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int supplierId;

	public BaseFraudDetectionKey(int supplierId) {
		super();
		this.supplierId = supplierId;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + supplierId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseFraudDetectionKey other = (BaseFraudDetectionKey) obj;
		if (supplierId != other.supplierId)
			return false;
		return true;
	}
	
}
